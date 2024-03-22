package nl.group9.firestore.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.cloud.firestore.Firestore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Main entry point of FirestoreUnit assertions. Each of the assertFirestore*() methods can be
 * used to validate the contents of the Firestore database with the reference file.
 */
public class FirestoreUnit {

    /**
     * TODO: Create javadoc
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as string
     */
    public static void assertFirestoreJson(Firestore firestore, String json) {
        assertFirestore(firestore, new ObjectMapper(), json);
    }

    /**
     * Validate using a JSON File
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as file
     */
    public static void assertFirestoreJson(Firestore firestore, File json) {
        assertFirestore(firestore, new ObjectMapper(), json);
    }

    /**
     * Validate using a JSON URL
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as URL
     */
    public static void assertFirestoreJson(Firestore firestore, URL json) {
        assertFirestore(firestore, new ObjectMapper(), json);
    }

    /**
     * Validate using a JSON Reader
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as Reader
     */
    public static void assertFirestoreJson(Firestore firestore, Reader json) {
        assertFirestore(firestore, new ObjectMapper(), json);
    }

    /**
     * Validate using a JSON InputStream
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as InputStream
     */
    public static void assertFirestoreJson(Firestore firestore, InputStream json) {
        assertFirestore(firestore, new ObjectMapper(), json);
    }

    /**
     * Validate using a YAML String
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as String
     */
    public static void assertFirestoreYaml(Firestore firestore, String yaml) {
        assertFirestore(firestore, new YAMLMapper(), yaml);
    }

    /**
     * Validate using a YAML File
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as file
     */
    public static void assertFirestoreYaml(Firestore firestore, File yaml) {
        assertFirestore(firestore, new YAMLMapper(), yaml);
    }

    /**
     * Validate using a YAML URL
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as URL
     */
    public static void assertFirestoreYaml(Firestore firestore, URL yaml) {
        assertFirestore(firestore, new YAMLMapper(), yaml);
    }

    /**
     * Validate using a YAML Reader
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as Reader
     */
    public static void assertFirestoreYaml(Firestore firestore, Reader yaml) {
        assertFirestore(firestore, new YAMLMapper(), yaml);
    }

    /**
     * Validate using a YAML InputStream
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as InputStream
     */
    public static void assertFirestoreYaml(Firestore firestore, InputStream yaml) {
        assertFirestore(firestore, new YAMLMapper(), yaml);
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, String contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, mapper.readTree(contents));
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, File contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, URL contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, Reader contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, InputStream contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, JsonNode tree) {
        FirestoreTester tester = new FirestoreTester(firestore, tree);
        tester.validate();
    }

}
