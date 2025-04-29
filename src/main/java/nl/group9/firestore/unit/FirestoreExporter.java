package nl.group9.firestore.unit;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.BiConsumer;

import static org.junit.jupiter.api.Assertions.fail;

public class FirestoreExporter {

    private final Firestore firestore;
    private final DateTimeFormatter formatter;
    private final FirestoreUnit.Options options;
    private final ObjectMapper objectMapper;

    public FirestoreExporter(Firestore firestore, FirestoreUnit.Options options, ObjectMapper objectMapper) {
        this.firestore = firestore;
        this.options = options;
        this.objectMapper = objectMapper;
        this.formatter = DateTimeFormatter.ISO_DATE_TIME;
    }

    public ObjectNode exportDocument(String path) {
        return export(path, this::exportSingleDocument);
    }

    public ObjectNode exportTree(String path) {
        return export(path, this::exportDocumentRecursive);
    }

    private ObjectNode export(String path, BiConsumer<DocumentReference, ObjectNode> exporter) {
        DocumentReference docRef = firestore.document(path);
        ObjectNode root = objectMapper.createObjectNode();

        ObjectNode child = nodesForPath(root, path);
        exporter.accept(docRef, child);

        return root;

    }

    private ObjectNode nodesForPath(ObjectNode root, String path) {
        String[] segments = path.split("/");
        String[] nodeNames = new String[segments.length];
        for (int i = 0; i < segments.length; i++) {
            if (i % 2 == 0) {
                nodeNames[i] = FirestoreTester.COLLECTION_PREFIX + segments[i];
            } else {
                nodeNames[i] = segments[i];
            }
        }

        ObjectNode currentNode = root;
        for (String nodeName : nodeNames) {
            currentNode = currentNode.withObject(nodeName);
        }

        return currentNode;
    }

    private void exportDocumentRecursive(DocumentReference docRef, ObjectNode node) {
        exportSingleDocument(docRef, node);

        docRef.listCollections().forEach(collection -> {
            var childNode = node.withObject(FirestoreTester.COLLECTION_PREFIX + collection.getId());
            exportCollectionRecursive(collection, childNode);
        });
    }

    private void exportCollectionRecursive(CollectionReference collection, ObjectNode node) {
        collection.listDocuments().forEach(childDoc -> {
            var docNode = node.withObject(childDoc.getId());
            exportDocumentRecursive(childDoc, docNode);
        });
    }

    private void exportSingleDocument(DocumentReference docRef, ObjectNode node) {
        try {
            var snapshot = docRef.get().get();

            var data = snapshot.getData();
            if (data != null) {
                data.forEach((key, value) -> {
                    try {
                        node.set(key, exportValue(value));
                    } catch (Error e) {
                        fail("Invalid document type encountered for document" + docRef.getPath() + " and key " + key, e);
                    }
                });
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private JsonNode exportValue(Object value) {
        JsonNodeFactory factory = objectMapper.getNodeFactory();

        if (value == null) {
            return factory.nullNode();
        } else if (value instanceof Boolean b) {
            return factory.booleanNode(b);
        } else  if (value instanceof Double d) {
            return factory.numberNode(d);
        } else  if (value instanceof Long l) {
            return factory.numberNode(l);
        } else  if (value instanceof Map<?,?> m) {
            ObjectNode child = factory.objectNode();
            m.forEach((key, val) -> child.set(key.toString(), exportValue(val)));
            return child;
        } else  if (value instanceof String v) {
            return factory.textNode(v);
        } else  if (value instanceof Timestamp t) {
            int nanos = (t.getNanos() / 1_000) * 1_000; // Round to milliseconds
            ZonedDateTime dt = ZonedDateTime.ofInstant(Instant.ofEpochSecond(t.getSeconds(), nanos), options.getZoneId());
            String dateStr = formatter.format(dt);
            return factory.textNode(dateStr);
        } else  if (value instanceof DocumentReference) {
            return factory.textNode(((DocumentReference) value).getPath());
        } else if (value instanceof List<?> l) {
            ArrayNode child = factory.arrayNode();
            l.forEach(item -> child.add(exportValue(item)));
            return child;
        } else {
            fail();
            return null;
        }
    }


}
