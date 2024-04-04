# README #

This README documents the steps which are necessary to use this library.

## What is this repository for? ##

FirestoreUnit is a testing library in the style of DBUnit to validate the contents of
a Google Cloud Firestore database (or emulator) versus reference files. These files
can be defined in either JSON or YAML format.

Use the `assertFirestore*` methods in `FiresstoreUnit` to perform the validation. 

## How do I get set up? ##

### Including the dependency ###

Include this library as a dependency for your project:

**Maven**
```xml
    <dependency>
        <groupId>nl.group9</groupId>
        <artifactId>firestore-unit</artifactId>
        <version>1.0</version>
    </dependency>
```

**Gradle**
```groovy
    testImplementation "nl.group9:firestore-unit:1.0"
```

### Define the validation files ###

You can choose to either define the validation files as JSON or as YAML. Both formats
offer equal features.

**Example in JSON:**

```json
{
  "_testcollection": {
    "testdoc1": {
      "testArray": [
        false,
        2.0,
        30,
        "Hello array"
      ],
      "testBoolean": true,
      "testFloat": 1.0,
      "testInteger": 20,
      "testMap": {
        "field1": 10,
        "field2": "text"
      },
      "testDateTime": "2024-03-22T12:13:14.123Z",
      "testReference": "testcollection/ref1",
      "testText": "Hello world",
      "_subcollection": {
        "testdoc2": {
          "testText": "Hello Firestore"
        }
      }
    },
    "_testdoc3": {
      "_subcollection": {
        "testdoc4": {
          "testText": "Hello Firestore"
        }
      }
    }
  }
}
```
Example in YAML
```yaml
_testcollection:
  testdoc1:
    testArray:
      - false
      - 2.0
      - 30
      - "Hello array"
    testBoolean: true
    testFloat: 1.0
    testInteger: 20
    testMap:
      field1: 10
      field2: "text"
    testDateTime: "2024-03-22T12:13:14.123Z"
    testReference: "testcollection/ref1"
    testText: "Hello world"
    _subcollection:
      testdoc2:
        testText: "Hello Firestore"
  _testdoc3:
    _subcollection:
      testdoc3:
        testText: "Hello Firestore"
```

Note that in this example:
* Collections always need to be prefixed with an underscore "_". This is to let the library
  differentiate between the Map datatype and collections.
* Date/time values need to be defined in ISO 8601 format
* In case a document should be skipped for checking (both for existance and the fields), prefix it with an underscore
  "_". In this case the document "testcollection/testdoc3" is optional. Firestore allows you to skip definition of all
  intermediate documents in a path.

### Use the library in your test ###

The library expects you to provide an instance of the `Firestore` class from the Google Client libraries to allow
access to the Firestore database. Note that you are responsible to ensure the correct authentication is set to allow 
the library to execute queries and retrieve data from the Firestore instance.

In an emulator scenario, the setup provided by Google normally provides a specific user (`owner`) which has 
unlimited access to the database.

The use one of the `assertFirestoreJson()` or `assertFirestoreYaml()` methods (depending on the format of your 
reference data) to validate the contents of your database. In case the data does not match, an `AssertionError` will
be thrown in a regular JUnit style.

### Limitations ###

This library has the following limitations:
* Currently, the `Geographical point` data type is not supported for validation
* Binary is not supported for validation
* The validation is lenient in style validation: additional fields or documents in the database are ignored.

### Future extensions ###

The following extensions *may* be defined in the future:
* Strict style validation 
* Specification of executors to use for async execution of data retrieval
* Functions for dynamic validation of data elements

### Contribution guidelines ###

In case you want to solve bugs or add features, you are encouraged to create a fork and open a merge request.
These will be reviewed and if appropriate merged into the main release.

### Who do I talk to? ###

For contact, see the developers section in the `pom.xml` file.

### Releasing a new version ###

Run: `mvn clean deploy -P release`