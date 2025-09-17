/**
 * GPC Acceptance Test Data Postman Collection Creator
 *
 * This script processes GPC acceptance parsed test data collected by ProcessGpcAcceptanceTestData.js
 * and generates a Postman Collection json file that can be imported into postman.
 *
 * Usage:
 *   node CreateCollection.js [testDataFilePath] [collectionName]
 *
 * Examples:
 *   node CreateCollection.js                                                   # Uses default path (ExampleData/_output/gpcAcceptanceTestData.passed.json)
 *   node CreateCollection.js c:\gpcAcceptanceTestData.passed.json              # Uses custom path
 *   node CreateCollection.js c:\gpcAcceptanceTestData.passed.json MyCollection  # Uses custom path and collection name
 *
 * Or using npm scripts:
 *   npm run cc                                         # Uses default path
 *   npm run cc c:\gpcAcceptanceTestData\               # Uses custom path
 *   npm run cc c:\gpcAcceptanceTestData\ MyCollection  # Uses custom path and collection name
 */

const sdk = require('postman-collection');
const fs = require('fs');
const path = require('path');
const jwt = require('jsonwebtoken');
const { getReasonPhrase } = require('http-status-codes');

/**
 * Reads the NHSNoMap.csv file and creates a mapping of NHS numbers to patient identifiers
 * @returns {Map} - Map of NHS numbers to patient identifiers
 */
function readNHSNoMap() {
  const nhsNoMapPath = 'NHSNoMap.csv';
  console.log(`Reading NHS number mapping from: ${nhsNoMapPath}`);

  try {
    const nhsNoMapData = fs.readFileSync(nhsNoMapPath, 'utf8');
    const lines = nhsNoMapData.split('\n');

    // Create a map of NHS numbers to patient identifiers
    const nhsNoMap = new Map();
    // Create an array to store the raw mappings for collection variables
    const nhsMappings = [];

    // Skip the header line
    for (let i = 1; i < lines.length; i++) {
      const line = lines[i].trim();
      if (line) {
        const [patientId, nhsNo] = line.split(',');
        nhsNoMap.set(nhsNo, `{{${patientId}}}`);

        // Add the mapping to the array for collection variables
        nhsMappings.push({
          patientId,
          nhsNo,
        });
      }
    }

    console.log(`Loaded ${nhsNoMap.size} NHS number mappings`);
    return { nhsNoMap, nhsMappings };
  } catch (error) {
    console.error('Error reading NHS number mapping:', error);
    return { nhsNoMap: new Map(), nhsMappings: [] };
  }
}

/**
 * Creates a Postman collection from test data
 * @param {string} testDataFilePath - Path to the test data file (optional)
 */
function createCollection(testDataFilePath, collectionName) {
  // Set default file path if not provided
  const filePath =
    testDataFilePath || 'ExampleData/_output/gpcAcceptanceTestData.passed.json';

  console.log(`Reading test data from: ${filePath}`);

  try {
    // Read NHS number mapping
    const { nhsNoMap, nhsMappings } = readNHSNoMap();

    // test file exists
    if (!fs.existsSync(filePath)) {
      throw new Error(`Test data file does not exist: ${filePath}`);
    }

    // Read and parse the test data file
    const testData = JSON.parse(fs.readFileSync(filePath, 'utf8'));

    // Create collection variables from NHS mappings
    const collectionVariables = nhsMappings.map((mapping) => ({
      key: mapping.patientId,
      value: mapping.nhsNo,
      type: 'string',
    }));

    // Create a new collection
    const collection = new sdk.Collection({
      info: {
        name: collectionName ?? 'GpcAcceptTestEndpoints',
      },
      item: [],
      variable: collectionVariables,
    });

    // Map to store folders by name to avoid duplicates
    const folderMap = new Map();

    // Loop through all objects in the test data
    testData.forEach((testItem) => {
      if (!testItem.requestName) {
        console.warn('Test item missing requestName, skipping...');
        return;
      }

      // Split requestName to get folder structure
      // Example: "Patient/$gpc.getstructuredrecord" -> ["Patient", "$gpc.getstructuredrecord"]
      const pathParts = testItem.data.projectName.split('/').reverse();

      // Create or get main folder
      let mainFolder;
      if (folderMap.has(pathParts[0])) {
        mainFolder = folderMap.get(pathParts[0]);
      } else {
        mainFolder = new sdk.ItemGroup({
          name: pathParts[0],
          item: [],
        });
        folderMap.set(pathParts[0], mainFolder);
        collection.items.add(mainFolder);
      }

      // If there's a subfolder path
      if (pathParts.length > 1) {
        // Create subfolder
        const subFolderName = pathParts.slice(1).join('/');
        let subFolder;

        // Check if subfolder already exists in main folder
        const existingSubFolder = mainFolder.items.find(
          (item) => item.name === subFolderName
        );

        if (existingSubFolder) {
          subFolder = existingSubFolder;
        } else {
          subFolder = new sdk.ItemGroup({
            name: subFolderName,
            description: 'The jwt on these endpoints require this scope',
            item: [],
          });
          mainFolder.items.add(subFolder);
        }

        // Create request item
        const requestItem = createRequestItem(testItem, nhsNoMap);
        var token = jsonTryParse(testItem.data.request.jwtoken);
        if (
          token &&
          token.requested_scope &&
          !subFolder.description.content.includes(token.requested_scope)
        ) {
          if (
            !subFolder.description.content ||
            subFolder.description.content == undefined
          ) {
            subFolder.description.content = '';
          }
          subFolder.description.content =
            subFolder.description.content + ', ' + token.requested_scope;
        }
        subFolder.items.add(requestItem);
      } else {
        // No subfolder, add request directly to main folder
        const requestItem = createRequestItem(testItem, nhsNoMap);
        mainFolder.items.add(requestItem);
      }
    });

    // Write the collection to a file
    const outputDir = filePath.replace('gpcAcceptanceTestData.passed.json', '');
    if (!fs.existsSync(outputDir)) {
      fs.mkdirSync(outputDir);
    }

    const fileName = path.join(outputDir, 'GpcAcceptTestEndpoints.json');
    const collectionJson = JSON.stringify(collection.toJSON(), null, 2);

    fs.writeFileSync(fileName, collectionJson);
    console.log(`Collection written to file: ${fileName}`);

    return collection;
  } catch (error) {
    console.error('Error creating collection:', error);
    throw error;
  }
}

function jsonTryParse(jsonString) {
  try {
    return JSON.parse(jsonString);
  } catch (e) {
    console.log(`error parsing jsonString but ignored ${jsonString}`);
    return null;
  }
}

/**
 * Decodes HTML entities in a string
 * @param {string} value - The string that might contain HTML entities
 * @returns {string} - The string with HTML entities decoded
 */
function decodeHtmlEntities(value) {
  if (!value || typeof value !== 'string') {
    return value;
  }

  // Replace common HTML entities
  return value
    .replace(/&amp;quot;/g, '"')
    .replace(/&amp;lt;/g, '<')
    .replace(/&amp;gt;/g, '>')
    .replace(/&amp;amp;/g, '&')
    .replace(/&amp;apos;/g, "'");
}

/**
 * Replaces NHS numbers with patient identifiers in a string
 * @param {string} value - The string that might contain NHS numbers
 * @param {Map} nhsNoMap - Map of NHS numbers to patient identifiers
 * @returns {string} - The string with NHS numbers replaced with patient identifiers
 */
function replaceNHSNumbers(value, nhsNoMap) {
  if (!value || typeof value !== 'string' || nhsNoMap.size === 0) {
    return value;
  }

  // Look for patterns like "https://fhir.nhs.uk/Id/nhs-number|9690937375"
  return value.replace(
    /https:\/\/fhir\.nhs\.uk\/Id\/nhs-number\|(\d+)/g,
    (match, nhsNumber) => {
      const patientId = nhsNoMap.get(nhsNumber);
      if (patientId) {
        console.log(`Replacing NHS number ${nhsNumber} with ${patientId}`);
        return `https://fhir.nhs.uk/Id/nhs-number|${patientId}`;
      }
      return match;
    }
  );
}

/**
 * Creates a Postman request item from test data
 * @param {Object} testItem - Test data item
 * @param {Map} nhsNoMap - Map of NHS numbers to patient identifiers
 * @returns {Object} - Postman request item
 */
function createRequestItem(testItem, nhsNoMap) {
  const requestData = testItem.data.request;
  const responseData = testItem.data.response;

  // Create headers
  const headers = [];
  if (requestData.headers) {
    requestData.headers.forEach((header) => {
      // If the header is Ssp-TraceID, replace its value with {{$guid}}
      if (header.name === 'Ssp-TraceID') {
        headers.push({
          key: header.name,
          value: '{{$guid}}',
        });
      } else if (header.name === 'Authorization') {
        headers.push({
          key: header.name,
          value: '{{pre_request_script_generated_JWT}}',
        });
      } else {
        // Replace NHS numbers in header values
        const replacedValue = replaceNHSNumbers(header.value, nhsNoMap);
        headers.push({
          key: header.name,
          value: replacedValue,
        });
      }
    });
  }

  // Add Content-Type header if we have a request body with content type
  if (requestData.body && requestData.body.value && requestData.contentType) {
    headers.push({
      key: 'Content-Type',
      value: requestData.contentType,
    });
  }

  // Create URL parameters
  const queryParams = [];
  let hasDateParams = false;
  if (requestData.parameters) {
    if (Array.isArray(requestData.parameters)) {
      requestData.parameters.forEach((param) => {
        // Skip parameters that start with underscore (test-specific parameters)
        if (param.name.startsWith('_')) {
          return;
        }

        // Replace NHS numbers in parameter values
        let replacedValue = replaceNHSNumbers(param.value, nhsNoMap);

        // Check for start and end parameters and replace their values
        if (param.name === 'start') {
          replacedValue =
            'ge{{pre_request_script_generated_search_start_date}}';
          hasDateParams = true;
        } else if (param.name === 'end') {
          replacedValue = 'le{{pre_request_script_generated_search_end_date}}';
          hasDateParams = true;
        }

        queryParams.push({
          key: param.name,
          value: replacedValue,
        });
      });
    }
  }

  // Create request body
  let body = null;
  if (requestData.body && requestData.body.value) {
    // Replace NHS numbers in request body and decode HTML entities
    const replacedBody = replaceNHSNumbers(requestData.body.value, nhsNoMap);
    const decodedBody = decodeHtmlEntities(replacedBody);
    body = {
      mode: 'raw',
      raw: decodedBody,
      options: {
        raw: {
          language: requestData.contentType || 'json',
        },
      },
    };
  }

  // Create URL
  // Combine endpoint URL with request URL and replace NHS numbers
  const baseUrl = testItem.data.endpointUrl;
  const requestPath = testItem.requestName || '';
  const fullUrl = requestPath ? `${baseUrl}/${requestPath}` : baseUrl;
  const replacedFullUrl = replaceNHSNumbers(fullUrl, nhsNoMap);

  const url = {
    raw: replacedFullUrl,
    protocol: replacedFullUrl.match(/^https?/)[0],
    host: [replacedFullUrl.replace(/^https?:\/\//, '').split('/')[0]],
    path: replacedFullUrl
      .replace(/^https?:\/\/[^/]+\//, '')
      .split('/')
      .filter((p) => p !== ''),
  };

  // Add query parameters to URL if they exist
  if (queryParams.length > 0) {
    url.query = queryParams;
  }

  // Create response
  // Replace NHS numbers in response headers
  const responseHeaders = responseData.headers
    ? responseData.headers.map((h) => ({
        key: h.name,
        value: replaceNHSNumbers(h.value, nhsNoMap),
      }))
    : [];

  // Replace NHS numbers in response body and decode HTML entities
  const responseBody = decodeHtmlEntities(
    replaceNHSNumbers(responseData.body || '', nhsNoMap)
  );

  // Create originalRequest object with the original request headers
  const originalRequestHeaders = requestData.headers
    ? requestData.headers.map((h) => ({
        key: h.name,
        value: h.value,
      }))
    : [];

  const originalRequestBody =
    requestData.body && requestData.body.value
      ? {
          mode: 'raw',
          raw: decodeHtmlEntities(requestData.body.value),
        }
      : null;

  // Create originalRequest URL with the complete path
  const originalBaseUrl = testItem.data.endpointUrl;
  const originalRequestPath = testItem.requestName || '';
  const originalFullUrl = originalRequestPath
    ? `${originalBaseUrl}/${originalRequestPath}`
    : originalBaseUrl;

  const originalRequestUrl = {
    raw: originalFullUrl,
    protocol: originalFullUrl.match(/^https?/)[0],
    host: [originalFullUrl.replace(/^https?:\/\//, '').split('/')[0]],
    path: originalFullUrl
      .replace(/^https?:\/\/[^/]+\//, '')
      .split('/')
      .filter((p) => p !== ''),
  };

  // Add query parameters to URL if they exist
  if (requestData.parameters && requestData.parameters.length > 0) {
    originalRequestUrl.query = requestData.parameters
      .filter((param) => !param.name.startsWith('_')) // Filter out underscore parameters
      .map((param) => ({
        key: param.name,
        value: param.value,
      }));
  }

  const exampleResponse = new sdk.Response({
    name: `Example ${responseData.statusCode} Response`,
    code: responseData.statusCode,
    status: getReasonPhrase(responseData.statusCode),
    header: responseHeaders,
    body: responseBody,
    originalRequest: {
      method: requestData.method,
      header: originalRequestHeaders,
      body: originalRequestBody,
      url: originalRequestUrl,
    },
  });

  let token = jsonTryParse(testItem.data.request.jwtoken);
  let description = '';
  if (token && token.requested_scope) {
    description = `Required JWT Scope: ${token.requested_scope}`;
  }

  // Create the item configuration
  const itemConfig = {
    name: testItem.dir || testItem.requestName,
    description,
    request: {
      url: url,
      method: requestData.method,
      header: headers,
      body: body,
      description,
    },
    response: [exampleResponse],
  };

  // Add prerequest script for date parameters if needed
  if (hasDateParams) {
    itemConfig.event = [
      {
        listen: 'prerequest',
        script: {
          exec: [
            '// Setup search dates',
            'var paramStartTime = new Date();',
            'var paramEndTime = new Date();',
            'paramEndTime.setDate(paramEndTime.getDate() + 3); // 3 days after current date',
            '',
            '// Set the environment variable used in the payload',
            'pm.environment.set("pre_request_script_generated_search_start_date", paramStartTime.toISOString().split(\'T\')[0]);',
            'pm.environment.set("pre_request_script_generated_search_end_date", paramEndTime.toISOString().split(\'T\')[0]);',
            '',
          ],
          type: 'text/javascript',
        },
      },
    ];
  }

  // Create request item
  return new sdk.Item(itemConfig);
}

// If this script is run directly (not imported)
if (require.main === module) {
  // Check if a file path was provided as a command line argument
  const testDataFilePath = process.argv[2];
  const collectionName = process.argv[3];
  createCollection(testDataFilePath, collectionName);
}

// Export the function for use in other modules
module.exports = { createCollection };
