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
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * Main entry point of FirestoreUnit assertions. Each of the assertFirestore*() methods can be
 * used to validate the contents of the Firestore database with the reference file.
 */
public class FirestoreUnit {

    /**
     * Private default constructor
     */
    private FirestoreUnit() {}

    /**
     * Validate the contents of the Firestore database is equal to the contents of the JSON provided. The default
     * options are used.
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as string
     */
    public static void assertFirestoreJson(Firestore firestore, String json) {
        assertFirestoreJson(firestore, options(), json);
    }

    /**
     * Validate the contents of the Firestore database is equal to the contents of the JSON provided.
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param json The JSON reference data as String
     */
    public static void assertFirestoreJson(Firestore firestore, Options options, String json) {
        assertFirestore(firestore, new ObjectMapper(), options, json);
    }

    /**
     * Validate using a JSON File
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as file
     */
    public static void assertFirestoreJson(Firestore firestore, File json) {
        assertFirestoreJson(firestore, options(), json);
    }

    /**
     * Validate using a JSON File
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param json The JSON reference data as File
     */
    public static void assertFirestoreJson(Firestore firestore, Options options, File json) {
        assertFirestore(firestore, new ObjectMapper(), options, json);
    }

    /**
     * Validate using a JSON URL
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as URL
     */
    public static void assertFirestoreJson(Firestore firestore, URL json) {
        assertFirestoreJson(firestore, options(), json);
    }

    /**
     * Validate using a JSON URL
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param json The JSON reference data as URL
     */
    public static void assertFirestoreJson(Firestore firestore, Options options, URL json) {
        assertFirestore(firestore, new ObjectMapper(), options, json);
    }

    /**
     * Validate using a JSON Reader
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as Reader
     */
    public static void assertFirestoreJson(Firestore firestore, Reader json) {
        assertFirestoreJson(firestore, options(), json);
    }

    /**
     * Validate using a JSON Reader
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param json The JSON reference data as Reader
     */
    public static void assertFirestoreJson(Firestore firestore, Options options, Reader json) {
        assertFirestore(firestore, new ObjectMapper(), options, json);
    }

    /**
     * Validate using a JSON InputStream
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param json The JSON reference data as InputStream
     */
    public static void assertFirestoreJson(Firestore firestore, InputStream json) {
        assertFirestoreJson(firestore, options(), json);
    }

    /**
     * Validate using a JSON InputStream
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param json The JSON reference data as InputStream
     */
    public static void assertFirestoreJson(Firestore firestore, Options options, InputStream json) {
        assertFirestore(firestore, new ObjectMapper(), options, json);
    }

    /**
     * Validate using a YAML String
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as String
     */
    public static void assertFirestoreYaml(Firestore firestore, String yaml) {
        assertFirestoreYaml(firestore, options(), yaml);
    }

    /**
     * Validate using a YAML String
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param yaml The YAML reference data as String
     */
    public static void assertFirestoreYaml(Firestore firestore, Options options, String yaml) {
        assertFirestore(firestore, new YAMLMapper(), options, yaml);
    }

    /**
     * Validate using a YAML File
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as file
     */
    public static void assertFirestoreYaml(Firestore firestore, File yaml) {
        assertFirestoreYaml(firestore, options(), yaml);
    }

    /**
     * Validate using a YAML URL
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param yaml The YAML reference data as File
     */
    public static void assertFirestoreYaml(Firestore firestore, Options options, File yaml) {
        assertFirestore(firestore, new YAMLMapper(), options, yaml);
    }

    /**
     * Validate using a YAML URL
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as URL
     */
    public static void assertFirestoreYaml(Firestore firestore, URL yaml) {
        assertFirestoreYaml(firestore, options(), yaml);
    }

    /**
     * Validate using a YAML Reader
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param yaml The YAML reference data as URL
     */
    public static void assertFirestoreYaml(Firestore firestore, Options options, URL yaml) {
        assertFirestore(firestore, new YAMLMapper(), options, yaml);
    }

    /**
     * Validate using a YAML Reader
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as Reader
     */
    public static void assertFirestoreYaml(Firestore firestore, Reader yaml) {
        assertFirestoreYaml(firestore, options(), yaml);
    }

    /**
     * Validate using a YAML Reader
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param yaml The YAML reference data as Reader
     */
    public static void assertFirestoreYaml(Firestore firestore, Options options, Reader yaml) {
        assertFirestore(firestore, new YAMLMapper(), options, yaml);
    }

    /**
     * Validate using a YAML InputStream
     * @see #assertFirestoreJson(Firestore, String)
     * @param firestore The firestore instance to read from
     * @param yaml The YAML reference data as InputStream
     */
    public static void assertFirestoreYaml(Firestore firestore, InputStream yaml) {
        assertFirestoreYaml(firestore, options(), yaml);
    }

    /**
     * Validate using a YAML InputStream
     * @see #assertFirestoreJson(Firestore, Options, String)
     * @param firestore The firestore instance to read from
     * @param options The options for validation
     * @param yaml The YAML reference data as InputStream
     */
    public static void assertFirestoreYaml(Firestore firestore, Options options, InputStream yaml) {
        assertFirestore(firestore, new YAMLMapper(), options, yaml);
    }

    /**
     * Return the default options
     * @return the options
     */
    public static Options options() {
        return new Options();
    }

    /**
     * Options to configure the behaviour of the testing setup.
     */
    public static class Options {
        private final ZoneId zoneId;

        /**
         * Default constructor, sets default values for options
         */
        private Options() {
            zoneId = ZoneId.of("UTC");
        }

        /**
         * Updating constructor. Used in the with*() methods.
         * @param zoneId The zone id.
         */
        private Options(ZoneId zoneId) {
            this.zoneId = zoneId;
        }

        /**
         * Configure the zoneId to use when comparing timestamps
         * @param zoneId The name of the zone Id
         * @return The new options
         */
        public Options withZoneId(String zoneId) {
            return withZoneId(ZoneId.of(zoneId));
        }

        /**
         * Configure the zoneId to use when comparing timestamps
         * @param zoneId The zone Id
         * @return The new options
         */
        public Options withZoneId(ZoneId zoneId) {
            return new Options(zoneId);
        }

        ZoneId getZoneId() {
            return zoneId;
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, Options options, String contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, options, mapper.readTree(contents));
        } catch (JsonProcessingException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, Options options, File contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, options, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, Options options, URL contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, options, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, Options options, Reader contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, options, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, ObjectMapper mapper, Options options, InputStream contents) {
        try {
            FirestoreUnit.assertFirestore(firestore, options, mapper.readTree(contents));
        } catch (IOException e) {
            fail(e);
        }
    }

    private static void assertFirestore(Firestore firestore, Options options, JsonNode tree) {
        FirestoreTester tester = new FirestoreTester(firestore, options, tree);
        tester.validate();
    }

}
