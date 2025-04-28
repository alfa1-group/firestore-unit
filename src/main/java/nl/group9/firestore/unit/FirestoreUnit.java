package nl.group9.firestore.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.google.cloud.firestore.Firestore;

import java.io.*;
import java.net.URL;
import java.time.ZoneId;
import java.util.function.Supplier;

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
     * Export a single document to JSON. Only a single document will be exported as indicated by the path parameter. The
     * data will be exported in a format which is also accepted by the various assertFirestore*() methods.
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param os The output stream
     */
    public static void exportDocumentJson(Firestore firestore, Options options, String path, OutputStream os) {
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new ObjectMapper(), os);
    }

    /**
     * Export single document as JSON
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param w The writer
     */
    public static void exportDocumentJson(Firestore firestore, Options options, String path, Writer w) {
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new ObjectMapper(), w);
    }

    /**
     * Export single document as JSON
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param f The file
     */
    public static void exportDocumentJson(Firestore firestore, Options options, String path, File f) {
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new ObjectMapper(), f);
    }

    /**
     * Export single document as JSON
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param d The data output
     */
    public static void exportDocumentJson(Firestore firestore, Options options, String path, DataOutput d) {
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new ObjectMapper(), d);
    }

    /**
     * Export single document as YAML
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param os The output stream
     */
    public static void exportDocumentYaml(Firestore firestore, Options options,  String path, OutputStream os){
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new YAMLMapper(), os);
    }

    /**
     * Export single document as YAML
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param w The writer
     */
    public static void exportDocumentYaml(Firestore firestore, Options options,  String path, Writer w){
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new YAMLMapper(), w);
    }

    /**
     * Export single document as YAML
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param f The file
     */
    public static void exportDocumentYaml(Firestore firestore, Options options,  String path, File f){
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new YAMLMapper(), f);
    }

    /**
     * Export single document as YAML
     * @see #exportDocumentJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param d The dataoutput
     */
    public static void exportDocumentYaml(Firestore firestore, Options options,  String path, DataOutput d){
        export(transformToNodes(firestore, options, new ObjectMapper(), path), new YAMLMapper(), d);
    }

    /**
     * Export a tree of documents to JSON. Export will start recursively from the document indicated by the path parameter.
     *  The data will be exported in a format which is also accepted by the various assertFirestore*() methods.
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param os The output stream
     */
    public static void exportRecursiveJson(Firestore firestore, Options options,  String path, OutputStream os) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new ObjectMapper(), os);
    }

    /**
     * Export a tree of documents to JSON.
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param w The writer
     */
    public static void exportRecursiveJson(Firestore firestore, Options options,  String path, Writer w) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new ObjectMapper(), w);
    }

    /**
     * Export a tree of documents to JSON.
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param f The file
     */
    public static void exportRecursiveJson(Firestore firestore, Options options,  String path, File f) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new ObjectMapper(), f);
    }

    /**
     * Export a tree of documents to JSON.
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param d The data output
     */
    public static void exportRecursiveJson(Firestore firestore, Options options,  String path, DataOutput d) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new ObjectMapper(), d);
    }

    /**
     * Export document/collection tree as YAML
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param os The output stream
     */
    public static void exportRecursiveYaml(Firestore firestore, Options options,  String path, OutputStream os) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new YAMLMapper(), os);
    }

    /**
     * Export document/collection tree as YAML
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param w The writer
     */
    public static void exportRecursiveYaml(Firestore firestore, Options options,  String path, Writer w) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new YAMLMapper(), w);
    }

    /**
     * Export document/collection tree as YAML
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param f The file
     */
    public static void exportRecursiveYaml(Firestore firestore, Options options,  String path, File f) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new YAMLMapper(), f);
    }

    /**
     * Export document/collection tree as YAML
     * @see #exportRecursiveJson(Firestore, Options, String, OutputStream)
     * @param firestore The firestore instance to read from
     * @param options The options for exporting
     * @param path The path in the document/collection tree to export
     * @param d The dataoutput
     */
    public static void exportRecursiveYaml(Firestore firestore, Options options,  String path, DataOutput d) {
        export(transformToNodesRecursive(firestore, options, new ObjectMapper(), path), new YAMLMapper(), d);
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

    private static void export(Supplier<ObjectNode> nodeSupplier, ObjectMapper mapper, OutputStream os) {
        try {
            mapper.writeValue(os, nodeSupplier.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void export(Supplier<ObjectNode> nodeSupplier, ObjectMapper mapper, Writer w) {
        try {
            mapper.writeValue(w, nodeSupplier.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void export(Supplier<ObjectNode> nodeSupplier, ObjectMapper mapper, File f) {
        try {
            mapper.writeValue(f, nodeSupplier.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void export(Supplier<ObjectNode> nodeSupplier, ObjectMapper mapper, DataOutput o) {
        try {
            mapper.writeValue(o, nodeSupplier.get());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Supplier<ObjectNode> transformToNodes(Firestore firestore, Options options, ObjectMapper mapper, String path) {
        return () -> {
            FirestoreExporter exporter = new FirestoreExporter(firestore, options, mapper);
            return exporter.exportDocument(path);
        };
    }

    private static Supplier<ObjectNode> transformToNodesRecursive(Firestore firestore, Options options, ObjectMapper mapper, String path) {
        return () -> {
            FirestoreExporter exporter = new FirestoreExporter(firestore, options, mapper);
            return exporter.exportTree(path);
        };
    }

}
