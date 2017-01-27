package uk.gov.hscic.common.filters.model;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import java.util.Arrays;
import java.util.List;

public class WebTokenValidator {
    private static final List<String> PERMITTED_REQUESTED_SCOPES = Arrays.asList(
            "patient/*.read", "patient/*.write", "organization/*.read", "organization/*.write");

    public static void validateWebToken(WebToken webToken) {
        verifyNoNullValues(webToken);
        verifyTimeValues(webToken);
        verifyRequestedResourceValues(webToken);

        // Checking the practionerId and the sub are equal in value
        if (!(webToken.getRequestingPractitioner().getId().equals(webToken.getSub()))) {
            throw new InvalidRequestException("Practitioner ids do not match!");
        }

        if (!PERMITTED_REQUESTED_SCOPES.contains(webToken.getRequestedScope())) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        if (!"https://authorize.fhir.nhs.net/token".equals(webToken.getAud())) {
            throw new InvalidRequestException("Bad Request Exception");
        }
    }

    private static void verifyNoNullValues(WebToken webToken) {
        assertNotNull(webToken.getAud());
        assertNotNull(webToken.getExp());
        assertNotNull(webToken.getIat());
        assertNotNull(webToken.getIss());
        assertNotNull(webToken.getSub());
        assertNotNull(webToken.getReasonForRequest());

        assertNotNull(webToken.getRequestedRecord());
        assertNotNull(webToken.getRequestedRecord().getResourceType());
        assertNotNull(webToken.getRequestedRecord().getIdentifierValue("http://fhir.nhs.net/Id/nhs-number"));

        assertNotNull(webToken.getRequestedScope());

        assertNotNull(webToken.getRequestingDevice());
        assertNotNull(webToken.getRequestingDevice().getId());
        assertNotNull(webToken.getRequestingDevice().getModel());
        assertNotNull(webToken.getRequestingDevice().getResourceType());
        assertNotNull(webToken.getRequestingDevice().getVersion());

        assertNotNull(webToken.getRequestingOrganization());
        assertNotNull(webToken.getRequestingOrganization().getId());
        assertNotNull(webToken.getRequestingOrganization().getName());
        assertNotNull(webToken.getRequestingOrganization().getResourceType());
        assertNotNull(webToken.getRequestingOrganization().getIdentifierValue("http://fhir.nhs.net/Id/ods-organization-code"));

        assertNotNull(webToken.getRequestingPractitioner());
        assertNotNull(webToken.getRequestingPractitioner().getId());
        assertNotNull(webToken.getRequestingPractitioner().getResourceType());
        assertNotNull(webToken.getRequestingPractitioner().getName().get("family"));
        assertNotNull(webToken.getRequestingPractitioner().getName().get("given"));
        assertNotNull(webToken.getRequestingPractitioner().getName().get("prefix"));
        assertNotNull(webToken.getRequestingPractitioner().getIdentifierValue("http://fhir.nhs.net/sds-user-id"));
    }

    private static void assertNotNull(Object object) {
        if (null == object) {
            throw new InvalidRequestException("JSON entry imcomplete.");
        }
    }

    private static void verifyTimeValues(WebToken webToken) {
        // Checking the creation date is not in the future
        int timeValidationIdentifierInt = webToken.getIat();

        if (timeValidationIdentifierInt * 1000 > System.currentTimeMillis()) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        // Checking the expiry time is 5 minutes after creation
        if (webToken.getExp() - timeValidationIdentifierInt != 300) {
            throw new InvalidRequestException("Bad Request Exception");
        }
    }

    private static void verifyRequestedResourceValues(WebToken webToken) {
        // Checking the reason for request is directcare
        if (!"directcare".equals(webToken.getReasonForRequest())) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        RequestingDevice requestingDevice = webToken.getRequestingDevice();

        if (null == requestingDevice) {
            throw new InvalidRequestException("No requesting_device");
        }

        if ("InvalidResourceType".equals(requestingDevice.getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        if ("InvalidResourceType".equals(webToken.getRequestingOrganization().getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        if ("InvalidResourceType".equals(webToken.getRequestingPractitioner().getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        if ("Patient".equals(webToken.getRequestedRecord().getResourceType()) && "organization/*.write".equals(webToken.getRequestedScope())) {
            throw new InvalidRequestException("Bad Request Exception");
        }

        if ("Organization".equals(webToken.getRequestedRecord().getResourceType()) && "patient/*.read".equals(webToken.getRequestedScope())) {
            throw new InvalidRequestException("Bad Request Exception");
        }
    }
}