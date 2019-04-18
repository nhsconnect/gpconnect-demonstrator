package uk.gov.hscic.common.filters.model;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import java.util.Arrays;
import java.util.List;
import static uk.gov.hscic.OperationConstants.CODE_BAD_REQUEST;
import static uk.gov.hscic.OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME;
import static uk.gov.hscic.OperationConstants.SYSTEM_WARNING_CODE;
import uk.gov.hscic.OperationOutcomeFactory;

public class WebTokenValidator {
    // #239 remove the */write scopes
    private static final List<String> PERMITTED_REQUESTED_SCOPES = Arrays.asList("patient/*.read", "organization/*.read");

    public static void validateWebToken(WebToken webToken, int futureRequestLeeway) {
        verifyNoNullValues(webToken);
        verifyTimeValues(webToken, futureRequestLeeway);
        verifyRequestedResourceValues(webToken);

        // Checking the practionerId and the sub are equal in value
        if (!(webToken.getRequestingPractitioner().getId().equals(webToken.getSub()))) {
            throw new InvalidRequestException("JWT Practitioner ids do not match!",OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Practitioner ids do not match!", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if (!PERMITTED_REQUESTED_SCOPES.contains(webToken.getRequestedScope())) {
            throw new InvalidRequestException("Bad Request Exception",OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Bad Request Exception", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if (!"https://authorize.fhir.nhs.net/token".equals(webToken.getAud())) {
            throw new InvalidRequestException("Bad Request Exception",OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Bad Request Exception", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }

    private static void verifyNoNullValues(WebToken webToken) {
        assertNotNull("aud", webToken.getAud());
        
        assertNotNull("exp", webToken.getExp());
        
        assertNotNull("iat", webToken.getIat());
        assertNotNull("iss", webToken.getIss());
        assertNotNull("sub", webToken.getSub());
        assertNotNull("reason_for_request", webToken.getReasonForRequest());

        assertNotNull("requested_record", webToken.getRequestedRecord());
        assertNotNull("requested_record.resourceType", webToken.getRequestedRecord().getResourceType());

        assertNotNull("requested_scope", webToken.getRequestedScope());

        assertNotNull("requesting_device", webToken.getRequestingDevice());
        assertNotNull("requesting_device.id", webToken.getRequestingDevice().getId());
        assertNotNull("requesting_device.model", webToken.getRequestingDevice().getModel());
        assertNotNull("requesting_device.resourceType", webToken.getRequestingDevice().getResourceType());
        assertNotNull("requesting_device.version", webToken.getRequestingDevice().getVersion());
	
        // #239 Extra checks on requesting_device.identifier
        assertNotNull("requesting_device.identifiers", webToken.getRequestingDevice().getIdentifiers());
        assertNotNull("requesting_device.identifier", webToken.getRequestingDevice().getIdentifiers().get(0));
        assertNotNull("requesting_device.identifier.system", webToken.getRequestingDevice().getIdentifiers().get(0).getSystem());
        assertNotNull("requesting_device.identifier.value", webToken.getRequestingDevice().getIdentifiers().get(0).getValue());

        assertNotNull("requesting_organization", webToken.getRequestingOrganization());
        assertNotNull("requesting_organization.id", webToken.getRequestingOrganization().getId());
        assertNotNull("requesting_organization.name", webToken.getRequestingOrganization().getName());
        assertNotNull("requesting_organization.resourceType", webToken.getRequestingOrganization().getResourceType());
        assertNotNull("requesting_organization.identifier (ODS)", webToken.getRequestingOrganization().getIdentifierValue("http://fhir.nhs.net/Id/ods-organization-code"));

        assertNotNull("requesting_practitioner", webToken.getRequestingPractitioner());
        assertNotNull("requesting_practitioner.id", webToken.getRequestingPractitioner().getId());
        assertNotNull("requesting_practitioner.resourceType", webToken.getRequestingPractitioner().getResourceType());
        assertNotNull("requesting_practitioner.name", webToken.getRequestingPractitioner().getName());
        assertNotNull("requesting_practitioner.family", webToken.getRequestingPractitioner().getName().get("family"));
        assertNotNull("requesting_practitioner.given", webToken.getRequestingPractitioner().getName().get("given"));
        assertNotNull("requesting_practitioner.prefix", webToken.getRequestingPractitioner().getName().get("prefix"));
        assertNotNull("requesting_practitioner.identifier (SDS)", webToken.getRequestingPractitioner().getIdentifierValue("http://fhir.nhs.net/sds-user-id"));
        assertNotNull("requesting_practitioner.role.code", webToken.getRequestingPractitioner().getPractitionerRoleCode("http://fhir.nhs.net/ValueSet/sds-job-role-name-1"));
    }

    private static void assertNotNull(String claim, Object object) {
        if (null == object) {
            throw new InvalidRequestException("JWT JSON Incomplete: claim "+claim + " is null.", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT JSON Incomplete: claim "+claim + " is null.", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }

    private static void verifyTimeValues(WebToken webToken, int futureRequestLeeway) {
        // Checking the creation date is not in the future
        int timeValidationIdentifierInt = webToken.getIat();
        
        long epoch = System.currentTimeMillis() / 1000;
        
        // Checking creation time is not in the future (with a 5 second leeway
        if (timeValidationIdentifierInt > epoch + futureRequestLeeway) {
        	throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Creation time is in the future", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        // Checking the expiry time is 5 minutes after creation
        if (webToken.getExp() - timeValidationIdentifierInt != 300) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT expiry time is not 5 minutes after the creation time", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        // #238 Checking the token has not expired 
        if (webToken.getExp() < epoch) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT token has expired", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }

    private static void verifyRequestedResourceValues(WebToken webToken) {
        // Checking the reason for request is directcare
        if (!"directcare".equals(webToken.getReasonForRequest())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Reason for request is not directcare", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        RequestingDevice requestingDevice = webToken.getRequestingDevice();

        if (null == requestingDevice) {
            throw new InvalidRequestException("No requesting_device", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Requesting device is null", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("InvalidResourceType".equals(requestingDevice.getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Invalid Resource Type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("InvalidResourceType".equals(webToken.getRequestingOrganization().getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Invalid Resource Type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("InvalidResourceType".equals(webToken.getRequestingPractitioner().getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Invalid Resource Type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("Patient".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("patient/\\*\\.(read|write)")) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Invalid Resource Type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("Organization".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("organization/\\*\\.(read|write)")) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SYSTEM_WARNING_CODE, CODE_BAD_REQUEST, "JWT Invalid Resource Type", META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }
}
