package uk.gov.hscic.common.filters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.parser.IParser;
import ca.uhn.fhir.parser.StrictErrorHandler;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnclassifiedServerFailureException;
import com.fasterxml.jackson.core.JsonParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.common.filters.model.WebTokenValidator;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwInvalidRequest400_BadRequestException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntity422_BadRequestException;

@Component
public class WebTokenFactory {

    private static final Logger LOG = Logger.getLogger("AuthLog");
    private static final List<String> CONTENT_TYPES = Arrays.asList(
            "application/fhir+json",
            "application/fhir+xml"
    );

    private IParser fhirJsonParser = null;

    private static final String JWT_HEADER_TYP = "typ";
    private static final String JWT_HEADER_ALG = "alg";

    /**
     *
     * @param requestDetails Hapi Fhir object describing request
     * @param futureRequestLeeway JWT time leeway in seconds
     * @return populated WebToken object
     */
    WebToken getWebToken(RequestDetails requestDetails, int futureRequestLeeway) {
        WebToken webToken = null;

        String authorizationHeader = requestDetails.getHeader(HttpHeaders.AUTHORIZATION);

        if (null == authorizationHeader) {
            throwInvalidRequest400_BadRequestException(HttpHeaders.AUTHORIZATION + " header missing");
        }

        String[] authorizationHeaderComponents = authorizationHeader.split(" ");

        if (authorizationHeaderComponents.length != 2 || !"Bearer".equalsIgnoreCase(authorizationHeaderComponents[0])) {
            throwInvalidRequest400_BadRequestException(HttpHeaders.AUTHORIZATION + " header invalid");
        }

        String contentType = requestDetails.getHeader(HttpHeaders.CONTENT_TYPE);

        if (contentType == null) {
            if (Arrays.asList(RequestTypeEnum.POST, RequestTypeEnum.PUT).contains(requestDetails.getRequestType())) {
                // NB Issue type is Incomplete not Invalid
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, "No content media type set"),
                        SystemCode.BAD_REQUEST, IssueType.INCOMPLETE);
            }
        } else if (!CONTENT_TYPES.contains(contentType.split(";")[0])) {
            throwUnsupportedMedia415_BadRequestException("Unsupported content media type");
        }

        String[] formatParam = requestDetails.getParameters().get("_format");

        String acceptHeader = null == formatParam
                ? requestDetails.getHeader(HttpHeaders.ACCEPT)
                : formatParam[0];

        // If it's still null, use the Content-Type header value
        if (null == acceptHeader) {
            acceptHeader = contentType;
        }

        if (acceptHeader == null || !CONTENT_TYPES.contains(acceptHeader.split(";")[0])) {
            throwUnsupportedMedia415_BadRequestException("Unsupported accept media type");
        }

        try {
            if (authorizationHeaderComponents[1].contains("==") || authorizationHeaderComponents[1].contains("/") || authorizationHeaderComponents[1].contains("+")) {
                throwInvalidRequest400_BadRequestException("JWT must be encoded using Base64URL. Padding is not allowed");
            }

            String[] jWTParts = authorizationHeaderComponents[1].split("\\.");
            if (jWTParts.length == 2) {
                String headerJsonString = new String(Base64.getUrlDecoder().decode(jWTParts[0]));
                validateJWTHeader(headerJsonString);

                String claimsJsonString = new String(Base64.getUrlDecoder().decode(jWTParts[1]));

                // This magically populates the WebToken data object
                webToken = new ObjectMapper().readValue(claimsJsonString, WebToken.class);

                jwtParseResourcesValidation(claimsJsonString);
            } else {
                throwInvalidRequest400_BadRequestException("Invalid number of JWT base 64 blocks " + jWTParts.length);
            }
        } catch (IllegalArgumentException iae) {
            throwInvalidRequest400_BadRequestException("JWT is not Base 64");
        } catch (JsonParseException ex) {
            throwInvalidRequest400_BadRequestException(String.format("Invalid WebToken: JSON is not valid - %s", ex.getMessage()));
        } catch (IOException ex) {
            throwInvalidRequest400_BadRequestException("Invalid WebToken");
        }

        WebTokenValidator.validateWebToken(webToken, futureRequestLeeway);

        return webToken;
    } // getWebToken

    /**
     * Validates the first part of the JWT object
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
                        throwInvalidRequest400_BadRequestException("Invalid JWT header " + entry.getKey() + " value " + entry.getValue().asText());
                    }
                    break;
                case JWT_HEADER_TYP:
                    if (!entry.getValue().asText().equals("JWT")) {
                        throwInvalidRequest400_BadRequestException("Invalid JWT header " + entry.getKey() + " value " + entry.getValue().asText());
                    }
                    break;
                default:
                    throwInvalidRequest400_BadRequestException("Unrecognised JWT Header header key " + entry.getKey());
            }
            validHeaderKeys.put(entry.getKey(), entry.getValue().asText());
        }

        // check nothings missing
        for (String key : validHeaderKeys.keySet()) {
            if (validHeaderKeys.get(key) == null) {
                throwInvalidRequest400_BadRequestException("Missing JWT Header key " + key);
            }
        }
    }

    /**
     * handles the second part of the JWT object - the payload of claims checks
     * for presence of mandatory items, absence of forbidden itsms and
     * validity of some json objects which will be converted to hapifhir
     * resource objects
     *
     * @param claimsJsonString
     */
    private void jwtParseResourcesValidation(String claimsJsonString) {
        if (fhirJsonParser == null) {
            fhirJsonParser = FhirContext
                    .forDstu3()
                    .newJsonParser()
                    .setParserErrorHandler(new StrictErrorHandler());
        }

        String thisClaim = null;
        try {
            JsonNode jsonNode = new ObjectMapper().readTree(claimsJsonString);

            // Check for json objects that are not allowed
            for (String claim : new String[]{
                "requested_record",}) // #170 requested_record is not allowed
            {
                if (jsonNode.get(claim) != null) {
                    throwInvalidRequest400_BadRequestException(String.format("JWT claim %s should not be present", claim));
                }
            }

            // These are json objects not simple data types. They should be present
            // we now see if they can be converted to valid fhir resources
            for (String claim : new String[]{
                "requesting_practitioner",
                "requesting_device",
                "requesting_organization",}) {
                thisClaim = claim;

                if (jsonNode.get(claim) == null) {
                    throwInvalidRequest400_BadRequestException(String.format("JWT required claim %s is not present", thisClaim));
                }

                // are these valid json objects also valid fhir resources?
                fhirJsonParser.parseResource(jsonNode.get(claim).toString());
            }
        } catch (DataFormatException e) { // NB This is a fhir exception not a jackson json parsing exception
            // TODO NB This is UnprocessableEntity is that correct?
            throwUnprocessableEntity422_BadRequestException(
                    String.format("Invalid Resource claim %s in JWT (Not a valid Fhir Resource - %s)", thisClaim, e.getMessage()));
        } catch (IOException ex) {
            throwInvalidRequest400_BadRequestException(String.format("Unparsable JSON retrieving JWT claim %s", thisClaim));
        }
    } // jwtParseResourcesValidation

    private void throwUnsupportedMedia415_BadRequestException(String message) throws BaseServerResponseException {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new UnclassifiedServerFailureException(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE, message),
                SystemCode.BAD_REQUEST, IssueType.INVALID);
    }
}
