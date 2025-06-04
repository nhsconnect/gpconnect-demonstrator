# GPC Demonstrator Replacement

A project to replace the GPC (GP Connect) demonstrator with a Postman collection that can be run as a mock server to return static responses.

## Project Overview

This project converts GP Connect acceptance test data into a Postman 
collection that can be used as a mock server. The mock server returns 
static responses based on the original test data, allowing developers 
to test their applications against the GP Connect API without needing 
the actual GPC demonstrator.

## Features

- Processes GPC acceptance test data from XML files
- Converts test data into Postman collections
- Categorizes tests as passed, failed
- Includes example responses with the original request information
- Supports JWT tokens and claims required

If you just want to run mock server you need Postman and you can skip 
to the [Running as a Mock Server](#running-as-a-mock-server) section

## Prerequisites

- Node.js (v12 or higher)
- Postman (for running the collection and mock server)

## Installation

1. Clone this repository:
   ```
   git clone -b postman-replacement https://github.com/nhsconnect/gpconnect-demonstrator.git
   ```

2. Decide which version of demonstrator you're testing against
3. Locate the postman collection for this version in the folder tree
      a. `FullDataSet` > `Version` > `_output` > `GpcAcceptTestEndpoints.json`

## Usage

### Processing GPC Acceptance Test Data

Process the example data to generate JSON files:

```
npm run pd [customPath]
```

- Without arguments, it uses the default path (`./ExampleData`)
- The Example data is the output data from tests in the [gp-connect-provider-test](https://github.com/nhsconnect/gpconnect-provider-testing) project 
- With a custom path argument, it processes data from that location

### Creating a Postman Collection

Generate a Postman collection from the processed test data:

```
npm run cc [testDataFilePath]
```

- Without arguments, it uses the default path (`./ExampleData/output/gpcAcceptanceTestData.passed.json`)
- With a custom path argument, it uses that file as the source

## Running as a Mock Server

To use the generated collection as a mock server:

1. Import the generated collection file (`GpcAcceptTestEndpoints.json`) into Postman
2. In Postman, click on the collection and select "Mock" from the right sidebar
3. Click "Create a mock server"
4. Configure the mock server settings:
   - Name: Give your mock server a name
   - Environment: Select an environment if needed
   - Save responses: Enable this to save responses
5. Click "Create Mock Server"
6. Postman will provide a URL for your mock server

The mock server will now return the example responses included in the collection when matching requests are made.

## Configuring Static Responses

The mock server uses the example responses included in the Postman collection. These responses are created from the original GPC acceptance test data.

To customize the responses:

1. Open the collection in Postman
2. Navigate to a request
3. In the "Examples" tab, you can view and edit the example responses
4. Modify the response body, headers, or status code as needed
5. Save the changes

The mock server will use these updated responses when matching requests are made.

## Project Structure

- `src/`: Source code
  - `ProcessGpcAcceptanceTestData.js`: Processes GPC acceptance test data from XML files
  - `CreateCollection.js`: Creates a Postman collection from processed test data
  - `Postman.js`: Example usage of the Postman SDK for further documentation see [postmanlabs](https://www.postmanlabs.com/postman-collection/)
- `ExampleData/`: Contains example test data
  - Various test scenario directories with HttpContext.xml files
  - `output/`: Generated JSON files and Postman collections

# Developer Corner

If you want to generate some example data / test a smaller set of scenarios, you can either use the `./ExampleData` folder which has a subset of test data to use, or utilise your own test data. 

**_Note:_** You may also want to do this if new acceptance tests are created, and the responses need adding to the assurance process.

To build test data (used by the Postman collections) you can use the following commands:

```bash
# build from default Example Data
npm run pd

# build from custom dataset
npm run pd <location>
```

On Successfully creating the response example data, run the following command (this will create the postman collection)
```bash
npm run cc
```
## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
