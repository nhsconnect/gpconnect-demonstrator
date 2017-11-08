package uk.gov.hscic.common.filters.model;

import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import java.util.Arrays;
import java.util.List;

import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;

import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemURL;

public class WebTokenValidator {
    private static final List<String> PERMITTED_REQUESTED_SCOPES = Arrays.asList("patient/*.read", "patient/*.write",
            "organization/*.read", "organization/*.write");

    public static void validateWebToken(WebToken webToken, int futureRequestLeeway) {
        verifyNoNullValues(webToken);
        verifyTimeValues(webToken, futureRequestLeeway);
        verifyRequestedResourceValues(webToken);

        // Checking the practionerId and the sub are equal in value
        if (!(webToken.getRequestingPractitioner().getId().equals(webToken.getSub()))) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Practitioner ids do not match!"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (!PERMITTED_REQUESTED_SCOPES.contains(webToken.getRequestedScope())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Bad Request Exception"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (!SystemURL.AUTHORIZATION_TOKEN.equals(webToken.getAud())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Bad Request Exception"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
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
        assertNotNull(webToken.getRequestingDevice().getResourceType());
        assertNotNull(webToken.getRequestingOrganization());
        assertNotNull(webToken.getRequestingOrganization().getResourceType());
        assertNotNull(webToken.getRequestingPractitioner());
        assertNotNull(webToken.getRequestingPractitioner().getResourceType());

    }

    private static void assertNotNull(Object object) {
        if (null == object) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("JSON entry incomplete."), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }
    }

    private static void verifyTimeValues(WebToken webToken, int futureRequestLeeway) {
        // Checking the creation date is not in the future
        int timeValidationIdentifierInt = webToken.getIat();

        // Checking creation time is not in the future (with a 5 second leeway
        if (timeValidationIdentifierInt > (System.currentTimeMillis() / 1000) + futureRequestLeeway) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Creation time is in the future"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        // Checking the expiry time is 5 minutes after creation
        if (webToken.getExp() - timeValidationIdentifierInt != 300) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Request time expired"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }
    }

    private static void verifyRequestedResourceValues(WebToken webToken) {
        // Checking the reason for request is directcare
        if (!"directcare".equals(webToken.getReasonForRequest())) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Reason for request is not directcare"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        RequestingDevice requestingDevice = webToken.getRequestingDevice();

        if (null == requestingDevice) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("No requesting_device"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        String deviceType = requestingDevice.getResourceType();
        String organizationType = webToken.getRequestingOrganization().getResourceType();
        String practitionerType = webToken.getRequestingPractitioner().getResourceType();
        
        if (!deviceType.equals("Device") || !organizationType.equals("Organization") || !practitionerType.equals("Practitioner")) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException("Invalid resource type"), 
                    SystemCode.BAD_REQUEST,
                    IssueType.INVALID
            );
        }
        
        if ("Patient".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("patient/\\*\\.(read|write)")) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid Patient requested scope"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if ("Organization".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("organization/\\*\\.(read|write)")) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid Organization requested scope"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if ("Slot".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("slot/\\*\\.(read|write)")) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Invalid Slot requested scope"), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }     
    }   
}
