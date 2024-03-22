package nl.group9.firestore.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.common.util.concurrent.MoreExecutors;
import org.opentest4j.AssertionFailedError;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Internal class to perform the actual validation of the JSON tree vs the Firestore contents
 */
class FirestoreTester {

    private static final String COLLECTION_PREFIX = "_";
    private final Firestore firestore;
    private final JsonNode tree;
    private final Executor executor;
    private final DateTimeFormatter formatter;

    public FirestoreTester(Firestore firestore, JsonNode tree) {
        this.firestore = firestore;
        this.tree = tree;
        this.executor = MoreExecutors.directExecutor();
        this.formatter = DateTimeFormatter.ISO_DATE_TIME;
    }

    public void validate() {
        try {
            traverseCollections(firestore::collection, tree).get();
        } catch (InterruptedException e) {
            fail(e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof AssertionFailedError) {
                throw (AssertionFailedError) e.getCause();
            } else {
                fail(e.getCause());
            }
        }
    }

    private ApiFuture<?> validateCollection(CollectionReference collectionReference, JsonNode node) {
        List<ApiFuture<?>> futures = new ArrayList<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> fieldEntry = it.next();

            DocumentReference doc = collectionReference.document(fieldEntry.getKey());
            futures.add(validateDocument(doc, fieldEntry.getValue()));
        }
        return ApiFutures.allAsList(futures);
    }

    private ApiFuture<?> validateDocument(DocumentReference docRef, JsonNode node) {
        ApiFuture<?> childFuture = traverseCollections(docRef::collection, node);

        ApiFuture<DocumentSnapshot> docFuture = docRef.get();
        ApiFuture<DocumentSnapshot> result = ApiFutures.transform(
                docFuture,
                (DocumentSnapshot snapshot) -> validateDocument(snapshot, node),
                executor
        );

        return ApiFutures.allAsList(Arrays.asList(childFuture, result));
    }

    private DocumentSnapshot validateDocument(DocumentSnapshot snapshot, JsonNode node) {
        // VALIDATE: document exists
        String path = snapshot.getReference().getPath();

        assertTrue(snapshot.exists(), "The document was not found at " + path);
        validateFields(node, path, snapshot::contains, snapshot::get);

        return snapshot;
    }

    private void validateFields(JsonNode node,
                                String parentPath,
                                Function<String, Boolean> fieldExists,
                                Function<String, Object> fieldAccessor) {
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> fieldEntry = it.next();

            if (!isCollectionFieldEntry(fieldEntry)) {
                String name = fieldEntry.getKey();
                JsonNode value = fieldEntry.getValue();
                String fieldPath = parentPath + "/" + name;

                // VALIDATE: Field exists and has correct value
                assertTrue(fieldExists.apply(name), "Field does not exist at field path " + fieldPath);
                validateField(value, fieldAccessor.apply(name), fieldPath);
            }
        }
    }

    private void validateField(JsonNode value, Object docValue, String fieldPath) {
        // https://firebase.google.com/docs/firestore/manage-data/data-types
        if (value.isArray()) { // Array
            assertArrayValue(value, docValue, fieldPath);
        } else if (value.isBoolean()) { // Boolean
            assertPrimitiveValue(value, docValue, Boolean.class, JsonNode::asBoolean, fieldPath);
        } else if (value.isBinary()) { // Bytes
            fail("Not supported yet at " + fieldPath);
        } else if (value.isFloatingPointNumber()) { // Floating point number
            assertPrimitiveValue(value, docValue, Double.class, JsonNode::asDouble, fieldPath);
        } else if (value.isIntegralNumber()) { // Integer
            assertPrimitiveValue(value, docValue, Long.class, JsonNode::asLong, fieldPath);
        } else if (value.isObject()) { // Map
            assertMapValue(value, docValue, fieldPath);
        } else if (value.isTextual()) {
            if (docValue instanceof Timestamp) {
                // Date and time
                assertPrimitiveValue(value, docValue, Timestamp.class, this::jsonDateTimeToTimestamp, fieldPath);
            } else if (docValue instanceof DocumentReference) {
                assertDocumentReference(value, docValue, fieldPath);
            } else {
                // Text string
                assertPrimitiveValue(value, docValue, String.class, JsonNode::asText, fieldPath);
            }
        } else {
            fail("Invalid JSON Node type encountered for document path " + fieldPath);
        }

        // TODO: Geographical point not supported
    }

    private void assertDocumentReference(JsonNode value, Object docValue, String fieldPath) {
        assertType(docValue, DocumentReference.class, fieldPath);

        DocumentReference refDocValue = (DocumentReference) docValue;
        assertEquals(value.asText(), refDocValue.getPath(), () -> invalidValueMessage(fieldPath));
    }

    private <T> void assertPrimitiveValue(JsonNode value, Object docValue, Class<T> type, Function<JsonNode, T> nodeValue, String fieldPath) {
        assertType(docValue, type, fieldPath);
        assertEquals(nodeValue.apply(value), type.cast(docValue), () -> invalidValueMessage(fieldPath));
    }

    private void assertArrayValue(JsonNode value, Object docValue, String fieldPath) {
        assertType(docValue, List.class, fieldPath);

        ArrayNode arrayNode = (ArrayNode) value;
        List<?> docListValue = (List<?>) docValue;
        assertEquals(arrayNode.size(), docListValue.size(),
                "Array field does not contain the same number of elements at " + fieldPath);

        for (int i = 0; i < docListValue.size(); i++) {
            JsonNode arrayValue = arrayNode.get(i);
            Object docArrayValue = docListValue.get(i);
            String subPath = fieldPath + "[" + i + "]";

            validateField(arrayValue, docArrayValue, subPath);
        }
    }

    @SuppressWarnings("unchecked")
    private void assertMapValue(JsonNode value, Object docValue, String fieldPath) {
        assertType(docValue, Map.class, fieldPath);

        Map<String, Object> mapDocValue = (Map<String, Object>) docValue;
        validateFields(value, fieldPath, mapDocValue::containsKey, mapDocValue::get);
    }

    private Timestamp jsonDateTimeToTimestamp(JsonNode value) {
        Date date = Date.from(LocalDateTime
                .parse(value.asText(), this.formatter)
                .atZone(ZoneId.systemDefault())
                .toInstant());
        return Timestamp.of(date);
    }

    private void assertType(Object docValue, Class<?> type, String fieldPath) {
        assertInstanceOf(type, docValue, () -> invalidTypeMessage(fieldPath, type));
    }

    private String invalidTypeMessage(String fieldPath, Class<?> expectedType) {
        return "Field is not of extected type " + expectedType + " at " + fieldPath;
    }

    private String invalidValueMessage(String fieldPath) {
        return "Field does not have the expected value at " + fieldPath;
    }

    private ApiFuture<?> traverseCollections(Function<String, CollectionReference> accessor, JsonNode node) {
        List<ApiFuture<?>> futures = new ArrayList<>();
        for (Iterator<Map.Entry<String, JsonNode>> it = node.fields(); it.hasNext(); ) {
            Map.Entry<String, JsonNode> fieldEntry = it.next();

            if (isCollectionFieldEntry(fieldEntry)) {
                String name = toCollectionName(fieldEntry);
                CollectionReference ref = accessor.apply(name);
                futures.add(validateCollection(ref, fieldEntry.getValue()));
            }
        }
        return ApiFutures.allAsList(futures);
    }

    private boolean isCollectionFieldEntry(Map.Entry<String, JsonNode> fieldEntry) {
        return fieldEntry.getKey().startsWith(COLLECTION_PREFIX);
    }

    private String toCollectionName(Map.Entry<String, JsonNode> fieldEntry) {
        return fieldEntry.getKey().substring(COLLECTION_PREFIX.length());
    }
}