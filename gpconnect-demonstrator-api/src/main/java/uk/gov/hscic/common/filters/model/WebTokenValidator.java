package uk.gov.hscic.common.filters.model;

import java.util.Arrays;
import java.util.List;
import static uk.gov.hscic.SystemURL.ID_ODS_ORGANIZATION_CODE;
import static uk.gov.hscic.SystemURL.ID_SDS_USER_ID;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwInvalidRequest400_BadRequestException;
import static uk.gov.hscic.common.filters.FhirRequestGenericIntercepter.throwUnprocessableEntity422_BadRequestException;

public class WebTokenValidator {

    private static final List<String> PERMITTED_REQUESTED_SCOPES = Arrays.asList("patient/*.read", "patient/*.write",
            "organization/*.read", "organization/*.write");

    /**
     * NB a single public static method only used by WebTokenFactory
     *
     * @param webToken
     * @param futureRequestLeeway
     */
    public static void validateWebToken(WebToken webToken, int futureRequestLeeway) {
        verifyNoNullValues(webToken);
        verifyTimeValues(webToken, futureRequestLeeway);
        verifyRequestedResourceValues(webToken);

        // Checking the practionerId and the sub are equal in value
        if (!(webToken.getRequestingPractitioner().getId().equals(webToken.getSub()))) {
            throwInvalidRequest400_BadRequestException("JWT Practitioner ids do not match!");
        }

        if (!PERMITTED_REQUESTED_SCOPES.contains(webToken.getRequestedScope())) {
            throwInvalidRequest400_BadRequestException("JWT Bad Request Exception Invalid requested scope: " + webToken.getRequestedScope());
        }
    }

    /**
     * These are checks done on the Jackson WebToken object
     *
     * @param webToken
     */
    private static void verifyNoNullValues(WebToken webToken) {
        assertNotNull("aud", webToken.getAud());
        assertNotNull("exp", webToken.getExp());
        assertNotNull("iat", webToken.getIat());
        assertNotNull("iss", webToken.getIss());
        assertNotNull("sub", webToken.getSub());
        assertNotNull("reason_for_request", webToken.getReasonForRequest());
        assertNotNull("requested_scope", webToken.getRequestedScope());
        assertNotNull("requesting_device", webToken.getRequestingDevice());
        assertNotNull("requesting_device.resourceType", webToken.getRequestingDevice().getResourceType());

        // #209
        assertNotNull("requesting_device.identifier", webToken.getRequestingDevice().getIdentifiers());
        assertNotNull("requesting_device.identifier.system", webToken.getRequestingDevice().getIdentifiers().get(0).getSystem());
        assertNotNull("requesting_device.identifier.value", webToken.getRequestingDevice().getIdentifiers().get(0).getValue());
        assertNotNull("requesting_device.identifier.model", webToken.getRequestingDevice().getModel());
        assertNotNull("requesting_device.identifier.version", webToken.getRequestingDevice().getVersion());

        assertNotNull("requesting_organization", webToken.getRequestingOrganization());

        // #209
        assertNotNull("requesting_organization.name", webToken.getRequestingOrganization().getName());
        assertNotNull("requesting_organization.identifier (ODS)", webToken.getRequestingOrganization().getIdentifierValue(ID_ODS_ORGANIZATION_CODE));
        
        assertNotNull("requesting_organization.resourceType", webToken.getRequestingOrganization().getResourceType());
        assertNotNull("requesting_practitioner", webToken.getRequestingPractitioner());
        assertNotNull("requesting_practitioner.resourceType", webToken.getRequestingPractitioner().getResourceType());

        // #209
        assertNotNull("requesting_practitioner.id", webToken.getRequestingPractitioner().getId());
        assertNotNull("requesting_practitioner.name", webToken.getRequestingPractitioner().getName());
        assertNotNull("requesting_practitioner.name.family", webToken.getRequestingPractitioner().getName().get(0).getFamily());
        assertNotNull("requesting_practitioner.name.given", webToken.getRequestingPractitioner().getName().get(0).getGiven().get(0));
        assertNotNull("requesting_practitioner.identifier (SDS)", webToken.getRequestingPractitioner().getIdentifierValue(ID_SDS_USER_ID));
    }

    private static void assertNotNull(String claim, Object object) {
        if (null == object) {
            throwInvalidRequest400_BadRequestException("JWT JSON entry incomplete: claim " + claim + " is null.");
        }
    }

    private static void verifyTimeValues(WebToken webToken, int futureRequestLeeway) {
        // Checking the creation date is not in the future
        int timeValidationIdentifierInt = webToken.getIat();
        int timeValidationIdentifierExp = webToken.getExp();
        long epoch = System.currentTimeMillis() / 1000;

        // Checking creation time is not in the future (with a 5 second leeway
        if (timeValidationIdentifierInt > epoch + futureRequestLeeway) {
            throwInvalidRequest400_BadRequestException("JWT Creation time is in the future");
        }

        // Checking the expiry time is 5 minutes after creation
        if ((timeValidationIdentifierExp - timeValidationIdentifierInt) != 300 ) {
            throwInvalidRequest400_BadRequestException("JWT expiry time is not 5 minutes after the creation time");
        }

        // #238 Checking the expiry time is not in the past
        if (timeValidationIdentifierExp < epoch ) {
            throwInvalidRequest400_BadRequestException("JWT Request time has expired");
        }
    }

    private static void verifyRequestedResourceValues(WebToken webToken) {
        // Checking the reason for request is directcare
        if (!"directcare".equals(webToken.getReasonForRequest())) {
            throwInvalidRequest400_BadRequestException("JWT Reason for request is not directcare");
        }

        RequestingDevice requestingDevice = webToken.getRequestingDevice();

        if (null == requestingDevice) {
            throwInvalidRequest400_BadRequestException("JWT No requesting_device");
        }

        String deviceType = requestingDevice.getResourceType();
        String organizationType = webToken.getRequestingOrganization().getResourceType();
        String practitionerType = webToken.getRequestingPractitioner().getResourceType();

        if (!"Device".equals(deviceType)) {
            throwInvalidRequest400_BadRequestException("JWT Invalid requestingDevice resource type \""+deviceType+"\"");
        }
        if (!"Organization".equals(organizationType)) {
            throwInvalidRequest400_BadRequestException("JWT Invalid requestingOrganization resource type \""+organizationType+"\"");
        }
        if (!"Practitioner".equals(practitionerType)) {
            throwInvalidRequest400_BadRequestException("JWT Invalid requestingPractitioner resource type \""+practitionerType+"\"");
        }
    }
}
