# GPC Demonstrator Replacement

A project to replace the GPC (GP Connect) demonstrator with a Postman collection that can be run as a mock server to return static responses.

## Project Overview

This project converts GP Connect acceptance test data into a Postman 
collection that can be used as a mock server. The mock server returns 
static responses based on the original test data, allowing developers 
to test their applications against the GP Connect API without needing 
the actual GPC demonstrator.

## Project Structure

- `src/`: Source code
  - `ProcessGpcAcceptanceTestData.js`: Processes GPC acceptance test data from XML files
  - `CreateCollection.js`: Creates a Postman collection from processed test data
  - `Postman.js`: Example usage of the Postman SDK for further documentation see [postmanlabs](https://www.postmanlabs.com/postman-collection/)
- `ExampleData/`: Contains example test data
  - A small sample of various test scenario directories with HttpContext.xml files
  - `output/`: Generated JSON files and Postman collections

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


## Import The Postman Collection

1. Open Postman and click on `Collections`. 

2. Either click and drag the collection json file into the large space next to collections icon, or click `Import` and find your file, or drag and drop the collection file into this popup. 
   
   
## Running The Mock Server

To use the generated collection as a mock server (in order to hit the endpoints from your test application):


1. In Postman, right click on the imported collection and select "Mock" from the context menu (it may be in `More`)
2. Configure the mock server settings:
   - Name: Give your mock server a name
   - Environment: Select an environment if needed
   - Save responses: Enable this to save responses
3. Click "Create Mock Server"
4. Postman will then provide a URL for your mock server

The mock server will now return the example responses included in the collection when matching requests are made.

## Configuring / Updating Predefined Responses

The mock server uses the example responses included in the Postman collection. These responses are created from the original GPC acceptance test data.

To customize the responses:

1. Open the collection in Postman
2. Navigate to a request
3. Expand the request tree, and you'll see the `Examples`
4. Select the mocked response you'd like to modify
5. Modify the response body, headers, or status code as needed
6. Save the changes

The mock server will use these updated responses when matching requests are made.




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
