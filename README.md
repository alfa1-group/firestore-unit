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
* Date/time values need to be defined in ISO 8601 format. You can also specify timezone information in your test data,
  for example "2024-03-22T12:13:14.123+02:00".
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

### Options ###

(New feature since 0.3)

You can now specify options to customize the testing behaviour. Each of the default methods now has a variant which
takes an `Options` object. This `Options`-object will affect the way the library works. Updating the options can be done 
in a fluent way. As default, the Options object will use "UTC" as the default timezone.

```java
class Tester {
    
    void test() {
      assertFirestoreJson(
              firestore, 
              FirestoreUnit.options()
                .withZoneId("UTC"), 
              my_file
      );
    }
}
```

To specify the data to take the currently specified timezone, you need to enter the expected value in the JSON (or YAML)
document *without* the timezone information (so without the `Z` or `+02:00`); for example:

```json
{
  "_testcollection" : {
    "timezoned": {
      "testTimezoned": "2024-03-22T12:13:14.123"
    }
  }
}
```

#### Specify the timezone to use for testing ####

Use the `Options.withZoneId()` method to specify the `ZoneId` to use when validating timestamps. In case your application
stores dates in the database with a specific local timezone (i.e. not as UTC or the system default), then comparing dates
will fail as this will use the `ZoneId.systemDefault()`. You can override the zone id to use using this option.

This setting is convenient when your application code automatically interprets dates using a local timezone. In case that
timezone also features daylight-saving time (DST), the actual timezone offset may differ depending on when the test is
executed. By specifying a timezone for comparison, dates can be specified as if timezones are ignored (e.g. as 
`"2024-03-22T12:13:14.123Z"`). The tester will then automatically assume the same timezone as specified in the options. 

### Exporting ###

You can export the data in your Firestore datebase. This can be done for a single document or for a tree of documents and collections. The export can be written as JSON or as YAML. It uses the same format
as accepted by the various `assert*` methods.

To export a document or tree, use the various `export*()` method of `FirestoreUnit`. 

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