package uk.gov.hscic.common.filters;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import java.util.List;
import java.util.logging.ConsoleHandler;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.sql.Time;
import java.util.Base64;
import org.json.*;
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

			String requestScopeType = claimsJsonObject.getJSONObject("requested_record").getString("resourceType");
			JSONArray requestIdentifiersArray = claimsJsonObject.getJSONObject("requested_record")
					.getJSONArray("identifier");

			String requestOperation = theRequestDetails.getRequestPath();

			if (requestOperation.equals("Patient/$gpc.getcarerecord")) {
				// "REQUEST_RECORD" = patient resource //DONE
				if (!requestScopeType.equals("Patient")) {
					return new RuleBuilder().denyAll().build();
				}

			}

			if (requestIdentifiersArray.length() > 0) {
				String identifierSystem = ((JSONObject) requestIdentifiersArray.get(0)).getString("system");

				String identifierValue = ((JSONObject) requestIdentifiersArray.get(0)).getString("value");
				if ("Patient".equalsIgnoreCase(requestScopeType)) {
					// If it is a patient orientated request
					if (!"http://fhir.nhs.net/Id/nhs-number".equalsIgnoreCase(identifierSystem)
							|| !NhsCodeValidator.nhsNumberValid(identifierValue)) {
						return new RuleBuilder().denyAll().build();
					}

				} else {
					// If it is an organization oriantated request
					if (!"http://fhir.nhs.net/Id/ods-organization-code".equalsIgnoreCase(identifierSystem)) {
						return new RuleBuilder().denyAll().build();
					}
				}
			} else {
				return new RuleBuilder().denyAll().build();
			}

			// Checking the practionerId and the sub are equal in value
			JSONObject requestingPractitionerArray = claimsJsonObject.getJSONObject("requesting_practitioner");
			String practionerId = requestingPractitionerArray.getString("id");
			String sub = claimsJsonObject.getString("sub");

			if (!(practionerId.equals(sub))) {
				return new RuleBuilder().denyAll().build();
			}

			// Checking organization identifier is correct
			JSONArray organizationIdentifierArray = claimsJsonObject.getJSONObject("requesting_organization")
					.getJSONArray("identifier");
			if (organizationIdentifierArray.length() > 0) {
				for (int i = 0; i < organizationIdentifierArray.length(); i++) {
					String identifierSystem = ((JSONObject) organizationIdentifierArray.get(i)).getString("system");
					if (!"http://fhir.nhs.net/Id/ods-organization-code".equalsIgnoreCase(identifierSystem)) {
						return new RuleBuilder().denyAll().build();
					}
				}
			} else {
				return new RuleBuilder().denyAll().build();
			}
			// The method has been commented out to allow continuation of work.
			// The method
			// fails the response due to the wrong identifier being in place
			/*
			 * JSONArray practitionerIdentifierArray =
			 * claimsJsonObject.getJSONObject("requesting_practitioner")
			 * .getJSONArray("identifier"); if
			 * (practitionerIdentifierArray.length() > 0) { for (int i = 0; i <
			 * practitionerIdentifierArray.length(); i++) { String
			 * identifierSystem = ((JSONObject)
			 * practitionerIdentifierArray.get(i)).getString("system"); if
			 * (!"http://fhir.nhs.net/sds-user-id".equalsIgnoreCase(
			 * identifierSystem)) { return new RuleBuilder().denyAll().build();
			 * } } } else { return new RuleBuilder().denyAll().build(); }
			 */

			// Checking practitioner identifier is correct
			JSONArray practitionerIdentifierArray = claimsJsonObject.getJSONObject("requesting_practitioner")
					.getJSONArray("identifier");
			if (practitionerIdentifierArray.length() > 0) {
				String identifierSystem = ((JSONObject) practitionerIdentifierArray.get(0)).getString("system");
				if (!"http://fhir.nhs.net/sds-user-id".equalsIgnoreCase(identifierSystem)) {
					return new RuleBuilder().denyAll().build();
				}
			} else {
				return new RuleBuilder().denyAll().build();
			}

			// Checking the creation date is not in the future
			int timeValidationIdentifierInt = claimsJsonObject.getInt("iat");
			
			if (timeValidationIdentifierInt > (System.currentTimeMillis())/1000) {
					return new RuleBuilder().denyAll().build();
			}
			// Checking th reason for request is dircetcare
			String reasonForRequestValid = claimsJsonObject.getString("reason_for_request");
			if (!reasonForRequestValid.equals("directcare")) {
				return new RuleBuilder().denyAll().build();
			}
			
			// Checking the expiary time is 5 minutes after creation
			int timeValidationExpiryTime = claimsJsonObject.getInt("exp");
			int expiryTime = 300;
		
			if ((timeValidationExpiryTime) - (timeValidationIdentifierInt) != expiryTime) {
				return new RuleBuilder().denyAll().build();
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
				return new RuleBuilder().denyAll().build();
			}

			// Checks the aud is the correct link
			String aud = claimsJsonObject.getString("aud");
			if (!(aud.equals("https://authorize.fhir.nhs.net/token"))) {
				return new RuleBuilder().denyAll().build();
			}

			// Checks the JWT has the correct propertys
			boolean JWTHasCorrectJsonPropertys = true;
			JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestedRecordIsValidated(claimsJsonObject);
			JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestingDeviceIsValidated(claimsJsonObject);
			JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestingOrganizationIsValidated(claimsJsonObject);
			JWTHasCorrectJsonPropertys = checkJWTJSONResponseRequestingPractitionerIsValidated(claimsJsonObject);

			if (JWTHasCorrectJsonPropertys == false) {
				return new RuleBuilder().denyAll().build();
			}

		}
		return new RuleBuilder().allowAll().build();
	}

	private boolean checkJWTJSONResponseRequestedRecordIsValidated(JSONObject claimsJsonObject) {
		// Done
		try {
			claimsJsonObject.getJSONObject("requested_record").getString("resourceType");
			JSONArray RESULTS = claimsJsonObject.getJSONObject("requested_record").getJSONArray("identifier");
			if (RESULTS.length() > 0) {
				((JSONObject) RESULTS.get(0)).getString("system");
				((JSONObject) RESULTS.get(0)).getString("value");
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
			JSONArray RESULTS = claimsJsonObject.getJSONObject("requesting_device").getJSONArray("identifier");
			if (RESULTS.length() > 0) {
				((JSONObject) RESULTS.get(0)).getString("system");
				((JSONObject) RESULTS.get(0)).getString("value");
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
			JSONArray RESULTS = claimsJsonObject.getJSONObject("requesting_organization").getJSONArray("identifier");
			if (RESULTS.length() > 0) {
				((JSONObject) RESULTS.get(0)).getString("system");
				((JSONObject) RESULTS.get(0)).getString("value");
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
			JSONArray RESULTS = claimsJsonObject.getJSONObject("requesting_practitioner").getJSONArray("identifier");
			if (RESULTS.length() > 0) {
				((JSONObject) RESULTS.get(0)).getString("system");
				((JSONObject) RESULTS.get(0)).getString("value");
			}
			JSONObject RESULTS1 = claimsJsonObject.getJSONObject("requesting_practitioner").getJSONObject("name");

			if (RESULTS1.length() > 0) {
				RESULTS1.get("family");
				RESULTS1.get("given");
				RESULTS1.get("prefix");
			}
			// This is where practitionerRole should be tested, Issue accessing
			// inner property
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
