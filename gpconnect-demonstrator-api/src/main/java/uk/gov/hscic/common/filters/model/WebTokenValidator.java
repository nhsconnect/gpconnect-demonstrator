package uk.gov.hscic.common.filters.model;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.util.Arrays;
import java.util.List;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;

public class WebTokenValidator {
    private static final List<String> PERMITTED_REQUESTED_SCOPES = Arrays.asList(
            "patient/*.read", "patient/*.write", "organization/*.read", "organization/*.write");

    private WebTokenValidator() { }

    public static void validateWebToken(WebToken webToken, int futureRequestLeeway) {
        verifyNoNullValues(webToken);
        verifyTimeValues(webToken, futureRequestLeeway);
        verifyRequestedResourceValues(webToken);

        // Checking the practionerId and the sub are equal in value
        if (!(webToken.getRequestingPractitioner().getId().equals(webToken.getSub()))) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Practitioner ids do not match!"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (!PERMITTED_REQUESTED_SCOPES.contains(webToken.getRequestedScope())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Bad Request Exception"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (!SystemURL.AUTHORIZATION_TOKEN.equals(webToken.getAud())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Bad Request Exception"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
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

        assertNotNull(webToken.getRequestedScope());

        assertNotNull(webToken.getRequestingDevice());
        //assertNotNull(webToken.getRequestingDevice().getId()); // Is not mandatory
        //assertNotNull(webToken.getRequestingDevice().getModel());
        assertNotNull(webToken.getRequestingDevice().getResourceType());
        //assertNotNull(webToken.getRequestingDevice().getVersion());

        assertNotNull(webToken.getRequestingOrganization());
        //assertNotNull(webToken.getRequestingOrganization().getId()); // Is not mandatory
        //assertNotNull(webToken.getRequestingOrganization().getName());
        assertNotNull(webToken.getRequestingOrganization().getResourceType());
        //assertNotNull(webToken.getRequestingOrganization().getIdentifierValue(SystemURL.ID_ODS_ORGANIZATION_CODE));

        assertNotNull(webToken.getRequestingPractitioner());
        //assertNotNull(webToken.getRequestingPractitioner().getId());
        assertNotNull(webToken.getRequestingPractitioner().getResourceType());
        //assertNotNull(webToken.getRequestingPractitioner().getName());
        //assertNotNull(webToken.getRequestingPractitioner().getName().get("family"));
        //assertNotNull(webToken.getRequestingPractitioner().getName().get("given"));
        //assertNotNull(webToken.getRequestingPractitioner().getName().get("prefix"));
        //assertNotNull(webToken.getRequestingPractitioner().getIdentifierValue(SystemURL.SDS_USER_ID));
        //assertNotNull(webToken.getRequestingPractitioner().getPractitionerRoleCode(SystemURL.VS_SDS_JOB_ROLE_NAME));
    }

    private static void assertNotNull(Object object) {
        if (null == object) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("JSON entry incomplete."),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }

    private static void verifyTimeValues(WebToken webToken, int futureRequestLeeway) {
        // Checking the creation date is not in the future
        int timeValidationIdentifierInt = webToken.getIat();

        // Checking creation time is not in the future (with a 5 second leeway
        if (timeValidationIdentifierInt > (System.currentTimeMillis() / 1000) + futureRequestLeeway) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Creation time is in the future"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        // Checking the expiry time is 5 minutes after creation
        if (webToken.getExp() - timeValidationIdentifierInt != 300) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Request time expired"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }

    private static void verifyRequestedResourceValues(WebToken webToken) {
        // Checking the reason for request is directcare
        if (!"directcare".equals(webToken.getReasonForRequest())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Reason for request is not directcare"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        RequestingDevice requestingDevice = webToken.getRequestingDevice();

        if (null == requestingDevice) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("No requesting_device"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if ("InvalidResourceType".equals(requestingDevice.getResourceType()) ||
                "InvalidResourceType".equals(webToken.getRequestingOrganization().getResourceType()) ||
                "InvalidResourceType".equals(webToken.getRequestingPractitioner().getResourceType())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Invalid resource type"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if ("Patient".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("patient/\\*\\.(read|write)")) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid Patient requested scope"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if ("Organization".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("organization/\\*\\.(read|write)")) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid Organization requested scope"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }
}
