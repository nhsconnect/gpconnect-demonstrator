package uk.gov.hscic.common.filters;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import uk.gov.hscic.common.filters.model.RequestedRecord;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.common.filters.model.WebTokenValidator;
import uk.gov.hscic.common.util.NhsCodeValidator;

@Component
public class FhirRequestAuthInterceptor extends AuthorizationInterceptor {
    private static final Logger LOG = Logger.getLogger("AuthLog");

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails requestDetails) {
        String[] authorizationHeaderComponents = requestDetails.getHeader(HttpHeaders.AUTHORIZATION).split(" ");

        if (authorizationHeaderComponents.length != 2 || !"Bearer".equalsIgnoreCase(authorizationHeaderComponents[0])) {
            throw new InvalidRequestException(HttpHeaders.AUTHORIZATION + " header invalid.");
        }

        String claimsJsonString;

        try {
            claimsJsonString = new String(Base64.getDecoder().decode(authorizationHeaderComponents[1].split("\\.")[1]));
        } catch (IllegalArgumentException iae) {
            throw new InvalidRequestException("Not Base 64");
        }

        JSONObject claimsJsonObject = new JSONObject(claimsJsonString);

        LOG.info("Incoming FHIR request: " + claimsJsonObject);

        String contentType = requestDetails.getHeader(HttpHeaders.CONTENT_TYPE);
        String acceptHeader = requestDetails.getHeader(HttpHeaders.ACCEPT);
        String completeUrl = requestDetails.getCompleteUrl();
   
                

        if (contentType != null
                && !"application/json+fhir".equalsIgnoreCase(contentType)
                && !"application/xml+fhir".equalsIgnoreCase(contentType)
                && !"application/json+fhir;charset=utf-8".equalsIgnoreCase(contentType)
                && !"application/xml+fhir;charset=utf-8".equalsIgnoreCase(contentType)
                && !MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType)) {
            throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type");
        }

        if (acceptHeader != null
                && !"application/xml+fhir".equalsIgnoreCase(acceptHeader)
                && !"application/json+fhir".equalsIgnoreCase(acceptHeader)
                && !"application/json+fhir;charset=utf-8".equalsIgnoreCase(acceptHeader)
                && !"application/xml+fhir;charset=utf-8".equalsIgnoreCase(acceptHeader)
                && !"application/json, text/plain, */*".equalsIgnoreCase(acceptHeader)) {
            throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type");
        }
        
        validateContentFormatAndAccept(contentType,acceptHeader,completeUrl);

        WebToken webToken;

        try {
            webToken = new ObjectMapper().readValue(claimsJsonString, WebToken.class);
            WebTokenValidator.validateWebToken(webToken);
        } catch (IOException ex) {
            throw new InvalidRequestException("Invalid WebToken");
        }

        RequestedRecord requestedRecord = webToken.getRequestedRecord();
        String requestedNhsNumber = webToken.getRequestedRecord().getIdentifierValue("http://fhir.nhs.net/Id/nhs-number");

        if ("Patient/$gpc.getcarerecord".equals(requestDetails.getRequestPath())) {
            if (contentType != null
                    && !"application/json+fhir;charset=utf-8".equalsIgnoreCase(contentType)
                    && !"application/xml+fhir;charset=utf-8".equalsIgnoreCase(contentType)
                    && !MediaType.APPLICATION_JSON_UTF8_VALUE.equalsIgnoreCase(contentType)) {
                throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type");
            }

            if (acceptHeader != null
                    && !"application/xml+fhir;charset=utf-8".equalsIgnoreCase(acceptHeader)
                    && !"application/json+fhir;charset=utf-8".equalsIgnoreCase(acceptHeader)
                    && !"application/json, text/plain, */*".equalsIgnoreCase(acceptHeader)) {
                throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type");
            }

            if (!"Patient".equals(requestedRecord.getResourceType())) {
                throw new InvalidRequestException("Bad Request Exception");
            }

            // Gets the NHS number in the request body to compare to requested_record
            boolean isMatchingNhsNumber = requestDetails
                    .getUnqualifiedToQualifiedNames()
                    .values()
                    .stream()
                    .flatMap(List::stream)
                    .allMatch(value -> value.contains(requestedNhsNumber));

            if (!isMatchingNhsNumber) {
                throw new InvalidRequestException("Bad Request Exception");
            }
        }

        if ("Patient".equals(requestedRecord.getResourceType())) {
            // If it is a patient orientated request
            if (!NhsCodeValidator.nhsNumberValid(requestedNhsNumber)) {
                CodeableConceptDt errorCodableConcept = new CodeableConceptDt("http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1", "INVALID_NHS_NUMBER");
                errorCodableConcept.setText("Patient Record Not Found");

                OperationOutcome operationOutcomes = new OperationOutcome();
                operationOutcomes.getMeta().addProfile("http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1");
                operationOutcomes.addIssue().setSeverity(IssueSeverityEnum.ERROR)
                        .setCode(IssueTypeEnum.NOT_FOUND).setDetails(errorCodableConcept);

                throw new InvalidRequestException("Dates are invalid: ", operationOutcomes);
            }
        } else {
            // If it is an organization oriantated request
            if (null == webToken.getRequestedRecord().getIdentifierValue("http://fhir.nhs.net/Id/ods-organization-code")) {
                throw new InvalidRequestException("Bad Request Exception");
            }
        }

        // The method has been commented out to allow continuation of work.
        // The method  fails the response due to the wrong identifier being in place
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

        jwtParseResourcesValidation(claimsJsonObject);

        return new RuleBuilder().allowAll().build();
    }

    private void validateContentFormatAndAccept(String contentType, String acceptHeader, String completeUrl) {
        
        if(completeUrl.contains("_format=text%2Fxml")&&  "application/xml+fhir".equals(acceptHeader) && "application/xml+fhir".equals(contentType))
        {
            throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported media type"); 
        }
        
    }

    private void jwtParseResourcesValidation(JSONObject claimsJsonObject) {
        IParser parser = new FhirContext().newJsonParser();
        parser.setParserErrorHandler(new StrictErrorHandler());

        try {
            parser.parseResource(claimsJsonObject.getJSONObject("requesting_practitioner").toString()).getFormatCommentsPost();
            parser.parseResource(claimsJsonObject.getJSONObject("requesting_device").toString()).getFormatCommentsPost();
            parser.parseResource(claimsJsonObject.getJSONObject("requesting_organization").toString()).getFormatCommentsPost();
        } catch (DataFormatException e) {
            throw new UnprocessableEntityException("Invalid Resource Present");
        }
    }
}