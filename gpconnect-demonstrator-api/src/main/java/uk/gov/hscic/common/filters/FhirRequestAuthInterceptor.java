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
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemParameter;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.filters.model.RequestBody;
import uk.gov.hscic.common.filters.model.RequestedRecord;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.common.filters.model.WebTokenValidator;
import uk.gov.hscic.util.NhsCodeValidator;

@Component
public class FhirRequestAuthInterceptor extends AuthorizationInterceptor {
    private static final Logger LOG = Logger.getLogger("AuthLog");
    private static final String PERMITTED_MEDIA_TYPE_HEADER_REGEX = "application/(xml|json)\\+fhir(;charset=utf-8)?";
    private static final List<String> PERMITTED_ORGANIZATION_IDENTIFIER_SYSTEMS = Arrays.asList(SystemURL.ID_ODS_ORGANIZATION_CODE, SystemURL.ID_ODS_SITE_CODE);

    @Value("${request.leeway:5}")
    private int futureRequestLeeway;

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails requestDetails) {
        String authorizationHeader = requestDetails.getHeader(HttpHeaders.AUTHORIZATION);

        if (null == authorizationHeader) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(HttpHeaders.AUTHORIZATION + " header missing"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        String[] authorizationHeaderComponents = authorizationHeader.split(" ");

        if (authorizationHeaderComponents.length != 2 || !"Bearer".equalsIgnoreCase(authorizationHeaderComponents[0])) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(HttpHeaders.AUTHORIZATION + " header invalid"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        String contentType = requestDetails.getHeader(HttpHeaders.CONTENT_TYPE);

        if (contentType == null) {
            if (Arrays.asList(RequestTypeEnum.POST, RequestTypeEnum.PUT).contains(requestDetails.getRequestType())) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "No content media type set"),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.REQUIRED_ELEMENT_MISSING);
            }
        } else if (!contentType.replaceAll("; +", ";").toLowerCase(Locale.UK).matches(PERMITTED_MEDIA_TYPE_HEADER_REGEX)) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported content media type"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
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
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "Unsupported accept media type"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        WebToken webToken;

        try {
            String claimsJsonString = new String(Base64.getDecoder().decode(authorizationHeaderComponents[1].split("\\.")[1]));
            webToken = new ObjectMapper().readValue(claimsJsonString, WebToken.class);
            WebTokenValidator.validateWebToken(webToken, futureRequestLeeway);
            jwtParseResourcesValidation(claimsJsonString);
        } catch (IllegalArgumentException iae) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Not Base 64"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        } catch (IOException ex) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid WebToken"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        RequestedRecord requestedRecord = webToken.getRequestedRecord();
        String jwtNhsNumber = webToken.getRequestedRecord().getIdentifierValue(SystemURL.ID_NHS_NUMBER);

        switch (requestDetails.getRequestPath()) {
            case "Patient":
                if (!"Patient".equals(requestedRecord.getResourceType())) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid resource type, should be Patient but was " + requestedRecord.getResourceType()),
                            SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
                }

                String identifierValue = requestDetails
                        .getParameters()
                        .get(SystemParameter.IDENTIFIER)[0]
                        .split("\\|")[1];

                if (!StringUtils.equals(jwtNhsNumber, identifierValue)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("NHS number in identifier header doesn't match the header"),
                            SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
                }

                break;

            case "Patient/$gpc.getcarerecord":
                if (!"Patient".equals(requestedRecord.getResourceType())) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid resource type, should be Patient but was " + requestedRecord.getResourceType()),
                            SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
                }

                validateNhsNumberInBodyIsSameAsJWT(jwtNhsNumber, requestDetails.loadRequestContents(), StringUtils.contains(contentType, "xml"));

                break;

            case "Organization":
                String identifierSystem = requestDetails
                        .getParameters()
                        .get(SystemParameter.IDENTIFIER)[0]
                        .split("\\|")[0];

                if (!PERMITTED_ORGANIZATION_IDENTIFIER_SYSTEMS.contains(identifierSystem)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid organization identifier system: " + identifierSystem),
                            SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
                }

                break;

            default:
        }

        if ("Patient".equals(requestedRecord.getResourceType())) {
            // If it is a patient orientated request
            if (!NhsCodeValidator.nhsNumberValid(jwtNhsNumber)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid NHS number: " + jwtNhsNumber),
                        SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
            }
        } else {
            // If it is an organization orientated request
            if (null == webToken.getRequestedRecord().getIdentifierValue(SystemURL.ID_ODS_ORGANIZATION_CODE)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Bad Request Exception"),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
            }
        }

        return new RuleBuilder().allowAll().build();
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
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Invalid Resource Present"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        } catch (IOException ex) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Unparsable JSON"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }

    private static void validateNhsNumberInBodyIsSameAsJWT(String nhsNumberFromHeader, byte[] requestDetailsBody, boolean xmlContent) {
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
                        .filter(identifierDt -> SystemURL.ID_NHS_NUMBER.equals(identifierDt.getSystem())) // Make sure they match.
                        .map(IdentifierDt::getValue)
                        .findFirst()
                        .orElse(null);
            } else {
                nhsNumberFromBody = new ObjectMapper()
                        .readValue(new String(requestDetailsBody), RequestBody.class)
                        .getIdentifierParameterValue(SystemURL.ID_NHS_NUMBER);
            }

            if (null == nhsNumberFromBody) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("NHS number in body doesn't match the header"),
                        SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueTypeEnum.INVALID_CONTENT);
            }

            if (!NhsCodeValidator.nhsNumberValid(nhsNumberFromHeader) || !NhsCodeValidator.nhsNumberValid(nhsNumberFromBody)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("NHS number invalid"),
                        SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
            }

            if (!nhsNumberFromHeader.equals(nhsNumberFromBody)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("NHS number in body doesn't match the header"),
                        SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
            }
        } catch (IOException ex) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Cannot parse request body"),
                    SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
        }
    }
}
