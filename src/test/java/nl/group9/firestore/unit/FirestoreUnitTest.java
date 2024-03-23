package nl.group9.firestore.unit;

import com.google.cloud.NoCredentials;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.testcontainers.containers.FirestoreEmulatorContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static nl.group9.firestore.unit.FirestoreUnit.assertFirestoreJson;
import static nl.group9.firestore.unit.FirestoreUnit.assertFirestoreYaml;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
public class FirestoreUnitTest {

    private static final String CORRECT_JSON = "json/correct.json";
    private static final String CORRECT_YAML = "yaml/correct.yaml";

    @Container
    public static FirestoreEmulatorContainer emulator = new FirestoreEmulatorContainer(
            DockerImageName.parse("gcr.io/google.com/cloudsdktool/google-cloud-cli:441.0.0-emulators")
    );

    @BeforeAll
    public static void fillFirestore() throws Exception {
        try (Firestore firestore = connection()) {
            CollectionReference testcollection = firestore.collection("testcollection");

            DocumentReference testdoc1 = testcollection.document("testdoc1");
            Map<String, Object> testdoc1Fields = new HashMap<>();
            testdoc1Fields.put("testArray", List.of(false, 2.0, 30, "Hello array"));
            testdoc1Fields.put("testBoolean", true);
            testdoc1Fields.put("testFloat", 1.0);
            testdoc1Fields.put("testInteger", 20);
            testdoc1Fields.put("testMap", Map.of(
                    "field1", 10,
                    "field2", "text"
            ));
            // "2024-03-22T12:13:14.123Z"
            Calendar testDateTime = Calendar.getInstance();
            testDateTime.set(2024, Calendar.MARCH, 22, 12, 13, 14);
            testDateTime.set(Calendar.MILLISECOND, 123);
            testdoc1Fields.put("testDateTime", Timestamp.of(testDateTime.getTime()));

            testdoc1Fields.put("testReference", testcollection.document("ref1"));
            testdoc1Fields.put("testText", "Hello world");
            testdoc1.set(testdoc1Fields).get();

            CollectionReference subcollection = testdoc1.collection("subcollection");

            DocumentReference testdoc2 = subcollection.document("testdoc2");
            Map<String, Object> testdoc2Fields = new HashMap<>();
            testdoc2Fields.put("testText", "Hello Firestore");
            testdoc2.set(testdoc2Fields).get();
        }
    }

    @Test
    void testJsonString() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreJson(firestore, asString(CORRECT_JSON));
        }
    }

    @Test
    void testJsonFile() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreJson(firestore, asFile(CORRECT_JSON));
        }
    }

    @Test
    void testJsonUrl() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreJson(firestore, asURL(CORRECT_JSON));
        }
    }

    @Test
    void testJsonReader() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreJson(firestore, asReader(CORRECT_JSON));
        }
    }

    @Test
    void testJsonInputStream() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreJson(firestore, asInputStream(CORRECT_JSON));
        }
    }

    @Test
    void testYamlString() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreYaml(firestore, asString(CORRECT_YAML));
        }
    }

    @Test
    void testYamlFile() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreYaml(firestore, asFile(CORRECT_YAML));
        }
    }

    @Test
    void testYamlUrl() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreYaml(firestore, asURL(CORRECT_YAML));
        }
    }

    @Test
    void testYamlReader() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreYaml(firestore, asReader(CORRECT_YAML));
        }
    }

    @Test
    void testYamlInputStream() throws Exception {
        try (Firestore firestore = connection()) {
            assertFirestoreYaml(firestore, asInputStream(CORRECT_YAML));
        }
    }

    @Test
    void testArrayDifferentElements() {
        testInvalidFile("json/array_diff_element.json", "Field does not have the expected value at testcollection/testdoc1/testArray[0] ==> expected: <true> but was: <false>");
    }

    @Test
    void testArrayDifferenSize() {
        testInvalidFile("json/array_diff_size.json", "Array field does not contain the same number of elements at testcollection/testdoc1/testArray ==> expected: <5> but was: <4>");
    }

    @Test
    void testIncorrectBoolean() {
        testInvalidFile("json/incorrect_boolean.json", "");
    }

    @Test
    void testIncorrectDateTime() {
        testInvalidFile("json/incorrect_datetime.json", "Field does not have the expected value at testcollection/testdoc1/testDateTime ==> expected: <1924-03-22T12:13:14.123000000Z> but was: <2024-03-22T11:13:14.123000000Z>");
    }

    @Test
    void testIncorrectFloat() {
        testInvalidFile("json/incorrect_float.json", "Field does not have the expected value at testcollection/testdoc1/testFloat ==> expected: <3.0> but was: <1.0>");
    }

    @Test
    void testIncorrectInt() {
        testInvalidFile("json/incorrect_int.json", "Field does not have the expected value at testcollection/testdoc1/testInteger ==> expected: <50> but was: <20>");
    }

    @Test
    void testIncorrectReference() {
        testInvalidFile("json/incorrect_reference.json", "Field does not have the expected value at testcollection/testdoc1/testReference ==> expected: <testcollection/otherref> but was: <testcollection/ref1>");
    }

    @Test
    void testIncorrectText() {
        testInvalidFile("json/incorrect_text.json", "Field does not have the expected value at testcollection/testdoc1/testText ==> expected: <Hello other world> but was: <Hello world>");
    }

    @Test
    void testMissingCollection() {
        testInvalidFile("json/missing_collection.json", "The document was not found at testcollection/testdoc1/subcollection2/testdoc3 ==> expected: <true> but was: <false>");
    }

    @Test
    void testMissingDoc() {
        testInvalidFile("json/missing_doc.json", "The document was not found at testcollection/testdoc1/subcollection/testdoc3 ==> expected: <true> but was: <false>");
    }

    @Test
    void testInvalidValueType() {
        testInvalidFile("json/null_value.json", "Invalid JSON Node type encountered for document path testcollection/testdoc1/testBoolean");
    }

    @Test
    void testTypeMismatch() {
        testInvalidFile("json/invalid_type.json", "Field is not of extected type class java.lang.Double at testcollection/testdoc1/testBoolean ==> Unexpected type, expected: <java.lang.Double> but was: <java.lang.Boolean>");
    }

    private void testInvalidFile(String file, String errorMessage) {
        try (Firestore firestore = connection()) {
            try {
                assertFirestoreJson(firestore, asInputStream(file));
                fail();
            } catch (AssertionFailedError e) {
                assertEquals(errorMessage, e.getMessage());
            }
        } catch (Exception e) {
            fail(e);
        }
    }

    // TODO: Invalid json/yaml
    // TODO: Incorrect values


    private String asString(String file) throws IOException {
        StringBuilder rslt;
        try (BufferedReader in = asReader(file)) {
            String line;
            rslt = new StringBuilder();
            while ((line = in.readLine()) != null) {
                rslt.append(line).append(System.lineSeparator());
            }
        }
        return rslt.toString();
    }

    private File asFile(String file) throws IOException {
        try {
            return Paths.get(asURL(file).toURI()).toFile();
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    private URL asURL(String file) {
        return this.getClass().getClassLoader().getResource(file);
    }

    private InputStream asInputStream(String file) throws IOException {
        return asURL(file).openStream();
    }

    private BufferedReader asReader(String file) throws IOException {
        return new BufferedReader(new InputStreamReader(asInputStream(file)));
    }

    private static Firestore connection() {
        FirestoreOptions options = FirestoreOptions
                .getDefaultInstance()
                .toBuilder()
                .setHost(emulator.getEmulatorEndpoint())
                .setCredentials(NoCredentials.getInstance())
                .setProjectId("test-project")
                .build();
        return options.getService();
    }

}
