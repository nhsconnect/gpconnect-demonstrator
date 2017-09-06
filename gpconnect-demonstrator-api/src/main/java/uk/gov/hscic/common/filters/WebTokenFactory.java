package uk.gov.hscic.common.filters;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Locale;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.common.filters.model.WebTokenValidator;

@Component
public class WebTokenFactory {
    private static final Logger LOG = Logger.getLogger("AuthLog");

    private static final String PERMITTED_MEDIA_TYPE_HEADER_REGEX = "application/(xml|json)\\+fhir(;charset=utf-8)?";

    private IParser parser = null;

    WebToken getWebToken(RequestDetails requestDetails, int futureRequestLeeway) {
        WebToken webToken = null;

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

        try {
            if(authorizationHeaderComponents[1].contains("==") || authorizationHeaderComponents[1].contains("/") || authorizationHeaderComponents[1].contains("+"))
            {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("JWT must be encoded using Base64URL. Padding is not allowed"),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT); 
            }

            String claimsJsonString = new String(Base64.getDecoder().decode(authorizationHeaderComponents[1].split("\\.")[1]));
            webToken = new ObjectMapper().readValue(claimsJsonString, WebToken.class);
            
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

        WebTokenValidator.validateWebToken(webToken, futureRequestLeeway);

        return webToken;
    }

    private void jwtParseResourcesValidation(String claimsJsonString) {
        if (parser == null) {
            parser = FhirContext
                .forDstu2()
                .newJsonParser()
                .setParserErrorHandler(new StrictErrorHandler());
        }

        try {
            JsonNode jsonNode = new ObjectMapper().readTree(claimsJsonString);

            //LOG.info("Incoming FHIR request: " + jsonNode);

            parser.parseResource(jsonNode.get("requesting_practitioner").toString());
            parser.parseResource(jsonNode.get("requesting_device").toString());
            parser.parseResource(jsonNode.get("requesting_organization").toString());
        } catch (DataFormatException e) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Invalid Resource Present"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        } catch (IOException | NullPointerException ex) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Unparsable JSON"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }
}
