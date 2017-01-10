package uk.gov.hscic.common.filters;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.IRestfulResponse;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;

import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;

import javax.json.JsonException;
import javax.naming.directory.InvalidAttributeIdentifierException;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Time;
import java.util.Base64;
import org.json.*;
import org.springframework.expression.AccessException;
import org.springframework.stereotype.Component;
import uk.gov.hscic.common.util.NhsCodeValidator;

@Component
public class FhirRequestAuthInterceptor extends AuthorizationInterceptor {

	static Logger authLog = Logger.getLogger("AuthLog");

	@Override
	public List<IAuthRule> buildRuleList(RequestDetails theRequestDetails) {
		String authorizationStr = theRequestDetails.getHeader("Authorization");
		String[] jwtHeaderComponents = authorizationStr.split(" ");

		if (jwtHeaderComponents.length == 2 && "Bearer".equalsIgnoreCase(jwtHeaderComponents[0])) {
			String[] tokenComponents = jwtHeaderComponents[1].split("\\.");
			String claimsJsonString = new String(Base64.getDecoder().decode(tokenComponents[1]));

			authLog.info("JWTClaims - " + claimsJsonString);

			JSONObject claimsJsonObject = new JSONObject(claimsJsonString);

			try {

				String requestScopeType = claimsJsonObject.getJSONObject("requested_record").getString("resourceType");
				JSONArray requestIdentifiersArray = claimsJsonObject.getJSONObject("requested_record")
						.getJSONArray("identifier");

				String identifierValue = ((JSONObject) requestIdentifiersArray.get(0)).getString("value");

				String requestOperation = theRequestDetails.getRequestPath();
	
				
				
				if (requestOperation.equals("Patient/$gpc.getcarerecord")) {

					// "REQUEST_RECORD" = patient resource //DONE
					
					if (!requestScopeType.equals("Patient")) {
						throw new InvalidRequestException("Bad Request Exception");
					}
					// Gets the NHS number in the request body to compare to
					// requested_record
				
					Map<String, List<String>> map = theRequestDetails.getUnqualifiedToQualifiedNames();
					
					
					
		
					for (String paramName : map.keySet()) {
						List<String> paramValues = map.get(paramName);

						// Get Values of Param Name
						for (String valueOfParam : paramValues) {
							// Output the Values
							if (valueOfParam.contains(identifierValue) != true) {
								throw new InvalidRequestException("Bad Request Exception");
							}
							;
						}
					}

				}

				if (requestIdentifiersArray.length() > 0) {
					String identifierSystem = ((JSONObject) requestIdentifiersArray.get(0)).getString("system");

					if ("Patient".equalsIgnoreCase(requestScopeType)) {
						// If it is a patient orientated request
						if (!"http://fhir.nhs.net/Id/nhs-number".equalsIgnoreCase(identifierSystem)
								|| !NhsCodeValidator.nhsNumberValid(identifierValue)) {
							throw new InvalidRequestException("Bad Request Exception");
						}

					} else {
						// If it is an organization oriantated request
						if (!"http://fhir.nhs.net/Id/ods-organization-code".equalsIgnoreCase(identifierSystem)) {
							throw new InvalidRequestException("Bad Request Exception");
						}
					}
				} else {
					throw new InvalidRequestException("Bad Request Exception");
				}

				// Checking the practionerId and the sub are equal in value
				JSONObject requestingPractitionerArray = claimsJsonObject.getJSONObject("requesting_practitioner");
				String practionerId = requestingPractitionerArray.getString("id");
				String sub = claimsJsonObject.getString("sub");
				System.out.println("Getting default policy " + getDefaultPolicy());
				if (!(practionerId.equals(sub))) {
					throw new InvalidRequestException("Bad Request Exception");

				}

				// Checking organization identifier is correct
				JSONArray organizationIdentifierArray = claimsJsonObject.getJSONObject("requesting_organization")
						.getJSONArray("identifier");
				if (organizationIdentifierArray.length() > 0) {
					for (int i = 0; i < organizationIdentifierArray.length(); i++) {
						String identifierSystem = ((JSONObject) organizationIdentifierArray.get(i)).getString("system");
						if (!"http://fhir.nhs.net/Id/ods-organization-code".equalsIgnoreCase(identifierSystem)) {
							throw new InvalidRequestException("Bad Request Exception");

						}
					}
				} else {
					throw new InvalidRequestException("Bad Request Exception");

				}
				// The method has been commented out to allow continuation of
				// work.
				// The method
				// fails the response due to the wrong identifier being in place
				/*
				 * JSONArray practitionerIdentifierArray =
				 * claimsJsonObject.getJSONObject("requesting_practitioner")
				 * .getJSONArray("identifier"); if
				 * (practitionerIdentifierArray.length() > 0) { for (int i = 0;
				 * i < practitionerIdentifierArray.length(); i++) { String
				 * identifierSystem = ((JSONObject)
				 * practitionerIdentifierArray.get(i)).getString("system"); if
				 * (!"http://fhir.nhs.net/sds-user-id".equalsIgnoreCase(
				 * identifierSystem)) { throw new
				 * InvalidRequestException("Bad Request Exception"); } } } else
				 * { return new throw new
				 * InvalidRequestException("Bad Request Exception");}
				 */

				// Checking practitioner identifier is correct
				JSONArray practitionerIdentifierArray = claimsJsonObject.getJSONObject("requesting_practitioner")
						.getJSONArray("identifier");
				if (practitionerIdentifierArray.length() > 0) {
					String identifierSystem = ((JSONObject) practitionerIdentifierArray.get(0)).getString("system");
					if (!"http://fhir.nhs.net/sds-user-id".equalsIgnoreCase(identifierSystem)) {
						throw new InvalidRequestException("Bad Request Exception");

					}
				} else {
					throw new InvalidRequestException("Bad Request Exception");

				}
				
				
				boolean hasRequestingDevice = claimsJsonObject.has("requesting_device");
				if (hasRequestingDevice == false)
				{
					throw new InvalidRequestException("Bad Request Exception");
				}
			

				// Checking the creation date is not in the future
				int timeValidationIdentifierInt = claimsJsonObject.getInt("iat");

				if (timeValidationIdentifierInt > (System.currentTimeMillis()) / 1000) {
					throw new InvalidRequestException("Bad Request Exception");

				}
				// Checking the reason for request is directcare
				String reasonForRequestValid = claimsJsonObject.getString("reason_for_request");
				if (!reasonForRequestValid.equals("directcare")) {
					throw new InvalidRequestException("Bad Request Exception");

				}
				
			
				if(claimsJsonObject.getJSONObject("requesting_device").getString("resourceType").equals("InvalidResourceType")){
					throw new InvalidRequestException("Bad Request Exception");
				};
			
			
				// Checking the expiary time is 5 minutes after creation
				int timeValidationExpiryTime = claimsJsonObject.getInt("exp");
				int expiryTime = 300;

				if ((timeValidationExpiryTime) - (timeValidationIdentifierInt) != expiryTime) {
					throw new InvalidRequestException("Bad Request Exception");

				}
				
				
				
				
				

				// Checking the requested scope is valid
				String requestedScopeValue = claimsJsonObject.getString("requested_scope");
				boolean comparisonResultPR = requestedScopeValue.equals("patient/*.read");
				boolean comparisonResultPW = requestedScopeValue.equals("patient/*.write");
				boolean comparisonResultOR = requestedScopeValue.equals("organization/*.read");
				boolean comparisonResultOW = requestedScopeValue.equals("organization/*.write");

				if (comparisonResultPR == true || comparisonResultPW == true || comparisonResultOR == true
						|| comparisonResultOW == true) {
				} else {
					throw new InvalidRequestException("Bad Request Exception");

				}

				// Checks the aud is the correct link
				String aud = claimsJsonObject.getString("aud");
				if (!(aud.equals("https://authorize.fhir.nhs.net/token"))) {
					throw new InvalidRequestException("Bad Request Exception");
				}

				// Checks the JWT has the correct propertys
				boolean JWTHasCorrectJsonPropertys = true;
				JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestedRecordIsValidated(claimsJsonObject);
				JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestingDeviceIsValidated(claimsJsonObject);
				JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestingOrganizationIsValidated(claimsJsonObject);
				JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestingPractitionerIsValidated(claimsJsonObject);

				if (JWTHasCorrectJsonPropertys == false) {

					throw new InvalidRequestException("Bad Request Exception");
				}
			} catch (org.json.JSONException e) {
				throw new InvalidRequestException("Bad Request Exception");

			}

		}
		return new RuleBuilder().allowAll().build();
	}

	private boolean checkJWTJSONResponseRequestedRecordIsValidated(JSONObject claimsJsonObject) {
		// Done
		try {
			claimsJsonObject.getJSONObject("requested_record").getString("resourceType");
			JSONArray requestedRecordResults = claimsJsonObject.getJSONObject("requested_record").getJSONArray("identifier");
			if (requestedRecordResults.length() > 0) {
				((JSONObject) requestedRecordResults.get(0)).getString("system");
				((JSONObject) requestedRecordResults.get(0)).getString("value");
			}
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	private boolean checkJWTJSONResponseRequestingDeviceIsValidated(JSONObject claimsJsonObject) {
		try {
			claimsJsonObject.getJSONObject("requesting_device").getString("resourceType");
			claimsJsonObject.getJSONObject("requesting_device").getString("id");
			claimsJsonObject.getJSONObject("requesting_device").getString("model");
			claimsJsonObject.getJSONObject("requesting_device").getString("version");
			JSONArray requestingDeviceResults = claimsJsonObject.getJSONObject("requesting_device").getJSONArray("identifier");
			if (requestingDeviceResults.length() > 0) {
				((JSONObject) requestingDeviceResults.get(0)).getString("system");
				((JSONObject) requestingDeviceResults.get(0)).getString("value");
			}
			return true;
		} catch (Exception e) {
			return false;

		}
	}

	private boolean checkJWTJSONResponseRequestingOrganizationIsValidated(JSONObject claimsJsonObject) {
		try {
			claimsJsonObject.getJSONObject("requesting_organization").getString("resourceType");
			claimsJsonObject.getJSONObject("requesting_organization").getString("id");
			claimsJsonObject.getJSONObject("requesting_organization").getString("name");
			JSONArray requestingOrganizationResults = claimsJsonObject.getJSONObject("requesting_organization").getJSONArray("identifier");
			if (requestingOrganizationResults.length() > 0) {
				((JSONObject) requestingOrganizationResults.get(0)).getString("system");
				((JSONObject) requestingOrganizationResults.get(0)).getString("value");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean checkJWTJSONResponseRequestingPractitionerIsValidated(JSONObject claimsJsonObject) {
		try {
			claimsJsonObject.getJSONObject("requesting_practitioner").getString("resourceType");
			claimsJsonObject.getJSONObject("requesting_practitioner").getString("id");
			JSONArray requestingPractitionerResults = claimsJsonObject.getJSONObject("requesting_practitioner").getJSONArray("identifier");
			if (requestingPractitionerResults.length() > 0) {
				((JSONObject) requestingPractitionerResults.get(0)).getString("system");
				((JSONObject) requestingPractitionerResults.get(0)).getString("value");
			}
			
			JSONObject requestingPractitionerResultsName = claimsJsonObject.getJSONObject("requesting_practitioner").getJSONObject("name");

			if (requestingPractitionerResultsName.length() > 0) {
				requestingPractitionerResultsName.get("family");
				requestingPractitionerResultsName.get("given");
				requestingPractitionerResultsName.get("prefix");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
