/**
 * GPC Acceptance Test Data Processor
 *
 * This script processes GPC acceptance test data and generates a JSON.
 *
 * Usage:
 *   node ProcessGpcAcceptanceTestData.js [customPath] [projectName]
 *
 * Examples:
 *   node ProcessGpcAcceptanceTestData.js                      # Uses default path (project_root/ExampleData)
 *   node ProcessGpcAcceptanceTestData.js c:\gpcAcceptanceTestData\  # Uses custom path
 *   node ProcessGpcAcceptanceTestData.js c:\gpcAcceptanceTestData\ MyProject  # Uses custom path and project name
 *
 * Or using npm scripts:
 *   npm run pd                               # Uses default path
 *   npm run pd:custom -- c:\gpcAcceptanceTestData\  # Uses custom path
 *   npm run pd:custom -- c:\gpcAcceptanceTestData\ MyProject  # Uses custom path and project name
 */

const fs = require('fs').promises;
const path = require('path');
const { existsSync, readFileSync } = require('fs');

// Function to extract scenario name from directory name by removing hyphen and number at the end
function extractScenarioName(dirName) {
  return dirName.replace(/-\d+$/, '');
}

// Function to determine a test result from TestRunLog.txt
function getTestResult(scenarioName, gpcAcceptanceTestDataDir) {
  try {
    const testRunLogPath = path.join(
      gpcAcceptanceTestDataDir,
      'TestRunLog.txt'
    );
    console.log(`Checking if path exists: \n\t${testRunLogPath}`);
    if (!existsSync(testRunLogPath)) return 'Undetermined';

    console.log(
      `Reading file: \n\t${testRunLogPath}\n - Reason: Determining test result for scenario ${scenarioName}`
    );
    const content = readFileSync(testRunLogPath, 'utf8');
    const lines = content.split('\n');

    for (const line of lines) {
      // Look for lines that contain the scenario name followed by parameters and Pass/Fail
      if (line.includes(scenarioName)) {
        if (line.trim().endsWith(',Pass')) {
          return 'Passed';
        } else if (line.trim().endsWith(',Fail')) {
          return 'Failed';
        }
      }
    }

    return 'Undetermined';
  } catch (error) {
    console.error('Error determining test result:', error);
    return 'Undetermined';
  }
}

// Function to extract failure reason from TestRunLog.txt
function getFailureReason(scenarioName, gpcAcceptanceTestDataDir) {
  try {
    const testRunLogPath = path.join(
      gpcAcceptanceTestDataDir,
      'TestRunLog.txt'
    );
    console.log(`Checking if path exists: \n\t${testRunLogPath}`);
    if (!existsSync(testRunLogPath)) return null;

    console.log(
      `Reading file: \n\t${testRunLogPath}\n - Reason: Extracting failure reason for scenario ${scenarioName}`
    );
    const content = readFileSync(testRunLogPath, 'utf8');
    const lines = content.split('\n');

    let failureLineIndex = -1;

    // Find the line with the scenario name that ends with ",Fail"
    for (let i = 0; i < lines.length; i++) {
      if (lines[i].includes(scenarioName) && lines[i].endsWith(',Fail')) {
        failureLineIndex = i;
        break;
      }
    }

    if (failureLineIndex === -1) return null;

    // Find the next line with hyphens
    let nextHyphenLineIndex = -1;
    for (let i = failureLineIndex + 1; i < lines.length; i++) {
      if (lines[i].startsWith('----')) {
        nextHyphenLineIndex = i;
        break;
      }
    }

    if (nextHyphenLineIndex === -1) return null;

    // Extract the text between the failure line and the next hyphen line
    const failureReason = lines
      .slice(failureLineIndex + 1, nextHyphenLineIndex)
      .join('\n')
      .trim();
    return failureReason.length > 0 ? failureReason : null;
  } catch (error) {
    console.error('Error extracting failure reason:', error);
    return null;
  }
}

// Function to extract value between XML tags
function extractXmlValue(content, tagName) {
  const regex = new RegExp(`<${tagName}>(.*?)<\/${tagName}>`, 's');
  const match = content.match(regex);
  return match ? match[1] : null;
}

// Function to decode HTML entities
function decodeHtmlEntities(text) {
  return text
    .replace(/&amp;quot;/g, '"')
    .replace(/&amp;lt;/g, '<')
    .replace(/&amp;gt;/g, '>')
    .replace(/&amp;amp;/g, '&')
    .replace(/&amp;#39;/g, "'");
}

// Function to extract NHS number from request body for Patient/$gpc.getstructuredrecord
function extractNhsNumber(requestBody) {
  try {
    // Decode HTML entities in the request body
    const decodedBody = decodeHtmlEntities(requestBody);

    // Parse the JSON
    const parsedBody = JSON.parse(decodedBody);

    // Find the parameter with the name "patientNHSNumber"
    const patientParam = parsedBody.parameter.find(
      (param) => param.name === 'patientNHSNumber'
    );

    // Extract the NHS number
    if (
      patientParam &&
      patientParam.valueIdentifier &&
      patientParam.valueIdentifier.value
    ) {
      return patientParam.valueIdentifier.value;
    }
  } catch (error) {
    console.error('Error extracting NHS number:', error);
  }

  return null;
}

// Function to extract query parameters from ConsoleLog.txt
function extractQueryParameters(dirPath) {
  try {
    const consoleLogPath = path.join(dirPath, 'ConsoleLog.txt');
    console.log(`Checking if path exists: \n\t${consoleLogPath}`);
    if (!existsSync(consoleLogPath)) return {};

    console.log(
      `Reading file: \n\t${consoleLogPath}\n - Reason: Extracting query parameters from ConsoleLog.txt`
    );
    const content = readFileSync(consoleLogPath, 'utf8');
    const lines = content.split('\n');

    const queryParameters = {};
    const headerKeys = new Set();

    // First, collect all header keys to avoid duplicating parameters that are already in headers
    for (const line of lines) {
      if (line.startsWith('Header - ')) {
        const headerMatch = line.match(/Header - (.*?) ->/);
        if (headerMatch) {
          headerKeys.add(headerMatch[1]);
        }
      }
    }

    // Then extract parameters from "Added Key" lines
    for (const line of lines) {
      if (line.startsWith('Added Key=')) {
        // The format in ConsoleLog.txt is: Added Key='key' Value='value'
        const match = line.match(/Added Key='(.*?)' Value='(.*?)'/);
        if (match) {
          const key = match[1];
          const value = match[2];

          // Only add if not already in headers
          if (!headerKeys.has(key)) {
            queryParameters[key] = value;
          }
        }
      }
    }

    return queryParameters;
  } catch (error) {
    console.error('Error extracting query parameters:', error);
    return {};
  }
}

// Function to parse XML to JSON
function parseXmlToJson(xmlContent) {
  // This is a simplified XML parser for this specific use case
  // It extracts key elements from the HttpContext.xml structure
  const result = {
    request: {},
    response: {},
    httpcontext: {},
  };

  // Extract request information
  const requestMatch = xmlContent.match(/<request([^>]*)>(.*?)<\/request>/s);
  if (requestMatch) {
    const requestAttributes = requestMatch[1];
    const requestContent = requestMatch[2];

    // Extract endpointUrl from request attributes
    const endpointUrlMatch = requestAttributes.match(/endpointUrl="([^"]*)"/);
    if (endpointUrlMatch) {
      result.endpointUrl = endpointUrlMatch[1];
    }

    // Extract request headers
    result.request.headers = [];
    const headerMatches = requestContent.matchAll(
      /<requestHeader name="(.*?)" value="(.*?)" \/>/g
    );
    for (const match of headerMatches) {
      result.request.headers.push({
        name: match[1],
        value: match[2],
      });

      // Check for Authorization header with Bearer token
      if (match[1] === 'Authorization' && match[2].startsWith('Bearer ')) {
        // Extract the JWT token (everything after "Bearer ")
        const jwt = match[2].substring(7);

        // Base64 decode the JWT token
        try {
          // JWT tokens have 3 parts separated by dots. We need the middle part (payload)
          const parts = jwt.split('.');
          if (parts.length >= 2) {
            // Base64 decode the payload
            const payload = Buffer.from(parts[1], 'base64').toString('utf8');
            // Store the decoded JWT as a property on the request
            result.request.jwtoken = payload;
          }
        } catch (error) {
          console.error('Error decoding JWT token:', error);
        }
      }
    }

    // Keep method, contentType, body, and parameters at the request level for backward compatibility
    result.request.method = extractXmlValue(requestContent, 'requestMethod');
    result.request.contentType = extractXmlValue(
      requestContent,
      'requestContentType'
    );
    result.request.body = {
      value: extractXmlValue(requestContent, 'requestBody'),
    };

    // Extract request parameters
    result.request.parameters = [];
    const parametersMatch = requestContent.match(
      /<requestParameters>(.*?)<\/requestParameters>/s
    );
    if (parametersMatch) {
      const parametersContent = parametersMatch[1];
      const paramMatches = parametersContent.matchAll(
        /<requestParameter name="(.*?)" value="(.*?)" \/>/g
      );
      for (const match of paramMatches) {
        result.request.parameters.push({
          name: match[1],
          value: match[2],
        });
      }
    } else {
      // For backward compatibility, keep the old format
      result.request.parameters =
        extractXmlValue(requestContent, 'requestParameters') || '';
    }
  }

  // Extract response information
  const responseMatch = xmlContent.match(/<response>(.*?)<\/response>/s);
  if (responseMatch) {
    const responseContent = responseMatch[1];

    // Extract response properties
    result.response.contentType = extractXmlValue(
      responseContent,
      'responseContentType'
    );
    result.response.statusCode = extractXmlValue(
      responseContent,
      'responseStatusCode'
    );
    result.response.timeInMilliseconds = extractXmlValue(
      responseContent,
      'responseTimeInMilliseconds'
    );
    result.response.timeAcceptable = extractXmlValue(
      responseContent,
      'responseTimeAcceptable'
    );

    // Extract response headers
    result.response.headers = [];
    const headerMatches = responseContent.matchAll(
      /<responseHeader name="(.*?)" value="(.*?)" \/>/g
    );
    for (const match of headerMatches) {
      result.response.headers.push({
        name: match[1],
        value: match[2],
      });
    }

    // Extract response body
    result.response.body = extractXmlValue(responseContent, 'responseBody');
  }

  // Extract httpContext attributes
  const httpContextMatch = xmlContent.match(/<httpContext([^>]*)>/);
  if (httpContextMatch) {
    const attributes = httpContextMatch[1];

    const attrMatches = attributes.matchAll(/(\w+)="([^"]*)"/g);
    for (const match of attrMatches) {
      result.httpcontext[match[1]] = match[2];
    }
  }

  return result;
}

async function processGpcAcceptanceTestData(
  gpcAcceptanceTestDataDir = path.join(__dirname, '..', 'ExampleData'),
  projectName
) {
  const results = [];

  try {
    // Get all subdirectories in the gpcAcceptanceTestData folder
    console.log(
      `Reading directory: \n\t${gpcAcceptanceTestDataDir}\n - Reason: Getting all subdirectories in the gpcAcceptanceTestData folder`
    );
    const directories = await fs.readdir(gpcAcceptanceTestDataDir);

    for (const dir of directories) {
      const dirPath = path.join(gpcAcceptanceTestDataDir, dir);

      // Check if it's a directory
      console.log(
        `Checking path stats: \n\t${dirPath}\n - Reason: Verifying if it's a directory`
      );
      const stats = await fs.stat(dirPath);
      if (!stats.isDirectory()) continue;

      // Check if HttpContext.xml exists in this directory
      const httpContextPath = path.join(dirPath, 'HttpContext.xml');
      console.log(`Checking if path exists: \n\t${httpContextPath}`);
      if (!existsSync(httpContextPath)) continue;

      // Read and parse the HttpContext.xml file
      console.log(
        `Reading file: \n\t${httpContextPath}\n - Reason: Reading and parsing the HttpContext.xml file`
      );
      let xmlContent = await fs.readFile(httpContextPath, 'utf8');

      // Extract query parameters from ConsoleLog.txt
      const queryParameters = extractQueryParameters(dirPath);

      // Update the HttpContext.xml file with the extracted query parameters
      if (Object.keys(queryParameters).length > 0) {
        // Create XML for query parameters
        const queryParamsXml = Object.entries(queryParameters)
          .map(
            ([key, value]) =>
              `      <requestParameter name="${key}" value="${value}" />`
          )
          .join('\n');

        // TEMP COMMENT OUT AS CAUSES DOWNSTREAM ISSUES IN WIREMOCK
        // Replace empty requestParameters tag with populated one
        // xmlContent = xmlContent.replace(
        //     /<requestParameters\s*\/>/,
        //     `<requestParameters>\n${queryParamsXml}\n    </requestParameters>`
        // );

        // Write the updated XML back to the file
        console.log(
          `Writing to file: \n\t${httpContextPath}\n - Reason: Writing updated XML with query parameters back to the file`
        );
        await fs.writeFile(httpContextPath, xmlContent, 'utf8');
      }

      // Extract the requestUrl
      const requestUrlMatch = xmlContent.match(
        /<requestUrl>(.*?)<\/requestUrl>/
      );
      if (!requestUrlMatch) continue;

      const requestUrl = requestUrlMatch[1];

      // Parse the XML content to JSON
      const data = parseXmlToJson(xmlContent);
      data.testLocation = gpcAcceptanceTestDataDir;

      let interactionIdHeader = data.request.headers.find(
        (header) => header.name === 'Ssp-InteractionId'
      );
      let interactionId = '';
      if (interactionIdHeader) {
        interactionId = data.request.headers
          .find((header) => header.name === 'Ssp-InteractionId')
          .value.trim()
          .split(':')
          .slice(-2)
          .join('/')
          .replace(/-\d+$/, '');
      }
      // get last interaction 2 sections of an interaction id
      // example "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getstructuredrecord"
      // will select "operation/gpc.getstructuredrecord"

      if (
        projectName === null ||
        projectName === undefined ||
        projectName.trim() === ''
      ) {
        data.projectName = `${interactionId}`;
      } else {
        data.projectName = `${projectName}/${interactionId}`;
      }

      // Check if this is a Patient/$gpc.getstructuredrecord request
      let requestName = requestUrl;
      if (
        requestUrl === 'Patient/$gpc.getstructuredrecord' &&
        data.requestBody
      ) {
        // Extract NHS number from request body
        const nhsNumber = extractNhsNumber(data.requestBody);
        if (nhsNumber) {
          // Append NHS number to name
          name = `${requestUrl} ${nhsNumber}`;
        }
      }

      // Extract scenario name and determine test result
      const scenarioName = extractScenarioName(dir);
      const testResult = getTestResult(scenarioName, gpcAcceptanceTestDataDir);

      // Create the result object
      const resultObj = {
        requestName,
        dir,
        testResult,
        data,
      };

      // If the test failed, add the reason for failure
      if (testResult === 'Failed') {
        const reasonFailed = getFailureReason(
          scenarioName,
          gpcAcceptanceTestDataDir
        );
        if (reasonFailed) {
          resultObj.reasonFailed = reasonFailed;
        }
      }

      results.push(resultObj);
    }

    // Log the results
    // console.log(JSON.stringify(results, null, 2));

    // Create an output directory if it doesn't exist
    const outputDir = path.join(
      gpcAcceptanceTestDataDir ?? path.join(__dirname, '..'),
      '_output'
    );
    console.log(`Checking if path exists: \n\t${outputDir}`);
    if (!existsSync(outputDir)) {
      console.log(
        `Creating directory: \n\t${outputDir}\n - Reason: Creating output directory for JSON results`
      );
      await fs.mkdir(outputDir, { recursive: true });
    }

    // Write results to a JSON file

    let undetermined = results.filter(
      (result) => result.testResult === 'Undetermined'
    );
    let passed = results.filter((result) => result.testResult === 'Passed');
    let failed = results.filter((result) => result.testResult === 'Failed');

    var dir = __dirname.substring(0, __dirname.length - 3);
    var outputMessage = (str) =>
      str.startsWith('C:')
        ? `Results written to \u001b]8;;file:///${str.replace(
            /\\/g,
            '/'
          )}\u0007${str}\u001b]8;;\u0007`
        : `Results written to \u001b]8;;file:///${
            dir + str.replace(/\\/g, '/')
          }\u0007${dir + str}\u001b]8;;\u0007`;

    const outputFileUndetermined = path.join(
      outputDir,
      `gpcAcceptanceTestData.undetermined.json`
    );
    await fs.writeFile(
      outputFileUndetermined,
      JSON.stringify(undetermined, null, 2)
    );
    console.log(outputMessage(outputFileUndetermined));

    const outputFilePassed = path.join(
      outputDir,
      `gpcAcceptanceTestData.passed.json`
    );
    await fs.writeFile(outputFilePassed, JSON.stringify(passed, null, 2));
    console.log(outputMessage(outputFilePassed));

    const outputFileFailed = path.join(
      outputDir,
      `gpcAcceptanceTestData.failed.json`
    );
    await fs.writeFile(outputFileFailed, JSON.stringify(failed, null, 2));
    console.log(
      `Results written to \u001b]8;;file:///${outputFileFailed.replace(
        /\\/g,
        '/'
      )}\u0007${outputFileFailed}\u001b]8;;\u0007`
    );
  } catch (error) {
    console.error('Error processing example data:', error);
  }
}

// Check if custom path and project name were provided as command line arguments
const customPath = process.argv[2];
const projectName = process.argv[3];

if (customPath) {
  console.log(`Using custom path: ${customPath}`);
  console.log(`Using project name: ${projectName}`);
  processGpcAcceptanceTestData(customPath, projectName);
} else {
  // Run the function with a default path
  console.log(`Using default path`);
  processGpcAcceptanceTestData(path.join(__dirname, '..', 'ExampleData'));
}

// Export functions for testing and for use by other modules
module.exports = {
  parseXmlToJson,
  processGpcAcceptanceTestData,
};
