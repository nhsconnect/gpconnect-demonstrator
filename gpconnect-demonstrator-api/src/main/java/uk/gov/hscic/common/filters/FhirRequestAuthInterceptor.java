package uk.gov.hscic.common.filters;

import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import java.util.List;
import java.util.logging.ConsoleHandler;

import org.apache.log4j.Logger;

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

			int timeValidationIdentifierInt = claimsJsonObject.getInt("iat");
			if (timeValidationIdentifierInt > System.currentTimeMillis()) {
				return new RuleBuilder().denyAll().build();
			}
			
			String reasonForRequestValid = claimsJsonObject.getString("reason_for_request");
			if (!reasonForRequestValid.equals("directcare"))
			{
				return new RuleBuilder().denyAll().build();
			}
			
			
			

			int timeValidationExpiryTime = claimsJsonObject.getInt("exp");
			int expiryTime = 300000;
			
			if ((timeValidationExpiryTime - timeValidationIdentifierInt) != expiryTime) {
				return new RuleBuilder().denyAll().build();
			}

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
		}

		return new RuleBuilder().allowAll().build();
	}

}
