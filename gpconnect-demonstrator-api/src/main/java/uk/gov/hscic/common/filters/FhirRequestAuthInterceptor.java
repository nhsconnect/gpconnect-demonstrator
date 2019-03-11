package uk.gov.hscic.common.filters;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.composite.IdentifierDt;
import ca.uhn.fhir.model.dstu2.resource.Parameters;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import static uk.gov.hscic.OperationConstants.*;

import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.common.filters.model.RequestBody;
import uk.gov.hscic.common.filters.model.RequestedRecord;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.common.filters.model.WebTokenValidator;
import uk.gov.hscic.common.util.NhsCodeValidator;

@Component
public class FhirRequestAuthInterceptor extends AuthorizationInterceptor {

    private static final Logger LOG = Logger.getLogger("AuthLog");
    private static final String PERMITTED_MEDIA_TYPE_HEADER_REGEX = "application/(xml|json)\\+fhir(;charset=utf-8)?";

    private static final String JWT_HEADER_TYP = "typ";
    private static final String JWT_HEADER_ALG = "alg";

    @Value("${request.leeway:5}")
    private int futureRequestLeeway;

    @Value("${validateJWT:true}")
    private boolean validateJWT;

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails requestDetails) {

        String contentType = requestDetails.getHeader(HttpHeaders.CONTENT_TYPE);

        if (contentType == null) {
            if (Arrays.asList(RequestTypeEnum.POST, RequestTypeEnum.PUT).contains(requestDetails.getRequestType())) {
                throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported content media type", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Unsupported content media type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.BUSINESS_RULE_VIOLATION));
            }
        } else if (!contentType.replaceAll("; +", ";").toLowerCase(Locale.UK).matches(PERMITTED_MEDIA_TYPE_HEADER_REGEX)) {
            throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported content media type", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Unsupported content media type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.BUSINESS_RULE_VIOLATION));
        }

        String[] formatParam = requestDetails.getParameters().get("_format");

        String acceptHeader = null == formatParam
                ? requestDetails.getHeader(HttpHeaders.ACCEPT)
                : formatParam[0];

        // If it's still null, use the Content-Type header value
        if (null == acceptHeader) {
            acceptHeader = contentType;
        }

        if (acceptHeader == null || !acceptHeader.replaceAll("; +", ";").toLowerCase(Locale.UK).matches(PERMITTED_MEDIA_TYPE_HEADER_REGEX)) {
            throw new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported accept media type");
        }

        if (validateJWT) {
            if (requestDetails.getHeader(HttpHeaders.AUTHORIZATION) == null) {
                throw new InvalidRequestException("Authorization header blank", OperationOutcomeFactory.buildOperationOutcome(
                        OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER, "Authorization header blank",
                        OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }
            String[] authorizationHeaderComponents = requestDetails.getHeader(HttpHeaders.AUTHORIZATION).split(" ");

            if (authorizationHeaderComponents.length != 2 || !"Bearer".equalsIgnoreCase(authorizationHeaderComponents[0])) {
                throw new InvalidRequestException(HttpHeaders.AUTHORIZATION + " header invalid.", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, HttpHeaders.AUTHORIZATION + " header invalid.", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }

            WebToken webToken;

            try {
                // #171
                if (authorizationHeaderComponents[1].contains("==") || authorizationHeaderComponents[1].contains("/") || authorizationHeaderComponents[1].contains("+")) {
                    throw new InvalidRequestException("JWT must be encoded using Base64URL. Padding is not allowed", OperationOutcomeFactory.buildOperationOutcome(
                            SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "JWT must be encoded using Base64URL. Padding is not allowed", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }

                String[] jWTParts = authorizationHeaderComponents[1].split("\\.");
                if (jWTParts.length == 2) {
                    String headerJsonString = new String(Base64.getDecoder().decode(jWTParts[0]));
                    validateJWTHeader(headerJsonString);

                    String claimsJsonString = new String(Base64.getDecoder().decode(jWTParts[1]));

                    webToken = new ObjectMapper().readValue(claimsJsonString, WebToken.class);
                    WebTokenValidator.validateWebToken(webToken, futureRequestLeeway);
                    jwtParseResourcesValidation(claimsJsonString);
                } else {
                    throw new InvalidRequestException("Invalid number of JWT base 64 blocks " + jWTParts.length, OperationOutcomeFactory.buildOperationOutcome(
                            SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Invalid number of JWT base 64 blocks " + jWTParts.length, META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }
            } catch (IllegalArgumentException iae) {
                throw new InvalidRequestException("Not Base 64", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Not Base 64", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            } catch (IOException ex) {
                throw new InvalidRequestException("Invalid WebToken", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Invalid WebToken", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }

            RequestedRecord requestedRecord = webToken.getRequestedRecord();
            String requestedNhsNumber = webToken.getRequestedRecord().getIdentifierValue("http://fhir.nhs.net/Id/nhs-number");

            if ("Patient/$gpc.getcarerecord".equals(requestDetails.getRequestPath())) {
                if (!"Patient".equals(requestedRecord.getResourceType())) {
                    throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                            SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "Bad Request", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }

                validateNhsNumberInBodyIsSameAsHeader(requestedNhsNumber, requestDetails.loadRequestContents(), StringUtils.contains(contentType, "xml"));
            }

            if ("Patient".equals(requestedRecord.getResourceType())) {
                // If it is a patient orientated request
                if (!NhsCodeValidator.nhsNumberValid(requestedNhsNumber)) {
                    throw new InvalidRequestException("Invalid NHS number: ", OperationOutcomeFactory.buildOperationOutcome(
                            SYSTEM_WARNING_CODE, CODE_INVALID_NHS_NUMBER, "Patient Record Not Found", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.NOT_FOUND));
                }
            } else // If it is an organization oriantated request
             if (null == webToken.getRequestedRecord().getIdentifierValue("http://fhir.nhs.net/Id/ods-organization-code")) {
                    throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                            SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "Bad Request", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                }
        }
        return new RuleBuilder().allowAll().build();
    }

    /**
     *
     * @param headerJsonString
     * @throws InvalidRequestException
     * @throws IOException
     */
    private void validateJWTHeader(String headerJsonString) throws InvalidRequestException, IOException {
        Iterator<Map.Entry<String, JsonNode>> iter = new ObjectMapper().readTree(headerJsonString).fields();
        HashMap<String, String> validHeaderKeys = new HashMap<>();
        validHeaderKeys.put(JWT_HEADER_ALG, null);
        validHeaderKeys.put(JWT_HEADER_TYP, null);
        while (iter.hasNext()) {
            Map.Entry<String, JsonNode> entry = iter.next();
            switch (entry.getKey()) {
                case JWT_HEADER_ALG:
                    if (!entry.getValue().asText().equals("none")) {
                        throw new InvalidRequestException("Invalid JWT header " + entry.getKey() + " value " + entry.getValue().asText(), OperationOutcomeFactory.buildOperationOutcome(
                                SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Invalid JWT header" + entry.getKey() + " value " + entry.getValue().asText(), META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                    }
                    break;
                case JWT_HEADER_TYP:
                    if (!entry.getValue().asText().equals("JWT")) {
                        throw new InvalidRequestException("Invalid JWT header " + entry.getKey() + " value " + entry.getValue().asText(), OperationOutcomeFactory.buildOperationOutcome(
                                SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Invalid  JWT header " + entry.getKey() + " value " + entry.getValue().asText(), META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
                    }
                    break;
                default:
                    throw new InvalidRequestException("Unrecognised JWT Header header key " + entry.getKey(), OperationOutcomeFactory.buildOperationOutcome(
                            SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Unrecognised JWT Header header key " + entry.getKey(), META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }
            validHeaderKeys.put(entry.getKey(), entry.getValue().asText());
        }

        // check nothings missing
        for (String key : validHeaderKeys.keySet()) {
            if (validHeaderKeys.get(key) == null) {
                throw new InvalidRequestException("Missing JWT Header key " + key, OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "Missing JWT Header key " + key, META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));

            }
        }
    }

    private static void jwtParseResourcesValidation(String claimsJsonString) {
        IParser parser = FhirContext
                .forDstu2()
                .newJsonParser()
                .setParserErrorHandler(new StrictErrorHandler());

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(claimsJsonString);

            LOG.info("Incoming FHIR request: " + jsonNode);

            parser.parseResource(jsonNode.get("requesting_practitioner").toString());
            parser.parseResource(jsonNode.get("requesting_device").toString());
            parser.parseResource(jsonNode.get("requesting_organization").toString());
        } catch (DataFormatException e) {
            throw new UnprocessableEntityException("Invalid Resource Present", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "Invalid Resource Present", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.STRUCTURAL_ISSUE));
        } catch (IOException ex) {
            throw new InvalidRequestException("Unparsable JSON", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "Unparsable JSON", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.STRUCTURAL_ISSUE));
        }
    }

    private static void validateNhsNumberInBodyIsSameAsHeader(String nhsNumberFromHeader, byte[] requestDetailsBody, boolean xmlContent) {
        String nhsNumberFromBody;

        try {
            // Get the NHS number in the request body to compare to requested_record
            if (xmlContent) { // If XML, you can't use the JSON parser.
                nhsNumberFromBody = FhirContext
                        .forDstu2()
                        .newXmlParser()
                        .setParserErrorHandler(new StrictErrorHandler())
                        .parseResource(Parameters.class, new String(requestDetailsBody))
                        .getParameter()
                        .stream()
                        .filter(parameter -> "patientNHSNumber".equals(parameter.getName()))
                        .map(Parameters.Parameter::getValue)
                        .filter(IdentifierDt.class::isInstance)
                        .map(IdentifierDt.class::cast)
                        .filter(identifierDt -> "http://fhir.nhs.net/Id/nhs-number".equals(identifierDt.getSystem())) // Make sure they match.
                        .map(IdentifierDt::getValue)
                        .findFirst()
                        .orElse(null);
            } else {
                nhsNumberFromBody = new ObjectMapper()
                        .readValue(new String(requestDetailsBody), RequestBody.class)
                        .getIdentifierParameterValue("http://fhir.nhs.net/Id/nhs-number");
            }

            if (null == nhsNumberFromBody) {
                throw new InvalidRequestException("NHS number in body doesn't match the header", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_IDENTIFIER_SYSTEM, "NHS number in body doesn't match the header", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }

            if (!NhsCodeValidator.nhsNumberValid(nhsNumberFromHeader) || !NhsCodeValidator.nhsNumberValid(nhsNumberFromBody)) {
                throw new InvalidRequestException("NHS number invalid", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_NHS_NUMBER, "NHS number invalid", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }

            if (!nhsNumberFromHeader.equals(nhsNumberFromBody)) {
                throw new InvalidRequestException("NHS number in body doesn't match the header", OperationOutcomeFactory.buildOperationOutcome(
                        SYSTEM_WARNING_CODE, CODE_INVALID_NHS_NUMBER, "NHS number invalid", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
            }
        } catch (IOException ex) {
            throw new InvalidRequestException("Cannot parse request body", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_INVALID_NHS_NUMBER, "Cannot parse request body", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }
}
