package uk.gov.hscic.common.filters.model;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import java.util.Arrays;
import java.util.List;
import uk.gov.hscic.CodableConceptText;
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
            throw new InvalidRequestException("Practitioner ids do not match!", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    "Practitioner ids do not match!", SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if (!PERMITTED_REQUESTED_SCOPES.contains(webToken.getRequestedScope())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.BAD_REQUEST, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if (!SystemURL.AUTHORIZATION_TOKEN.equals(webToken.getAud())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.BAD_REQUEST, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
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
        assertNotNull(webToken.getRequestingDevice().getId());
        assertNotNull(webToken.getRequestingDevice().getModel());
        assertNotNull(webToken.getRequestingDevice().getResourceType());
        assertNotNull(webToken.getRequestingDevice().getVersion());

        assertNotNull(webToken.getRequestingOrganization());
        assertNotNull(webToken.getRequestingOrganization().getId());
        assertNotNull(webToken.getRequestingOrganization().getName());
        assertNotNull(webToken.getRequestingOrganization().getResourceType());
        assertNotNull(webToken.getRequestingOrganization().getIdentifierValue(SystemURL.ID_ODS_ORGANIZATION_CODE));

        assertNotNull(webToken.getRequestingPractitioner());
        assertNotNull(webToken.getRequestingPractitioner().getId());
        assertNotNull(webToken.getRequestingPractitioner().getResourceType());
        assertNotNull(webToken.getRequestingPractitioner().getName());
        assertNotNull(webToken.getRequestingPractitioner().getName().get("family"));
        assertNotNull(webToken.getRequestingPractitioner().getName().get("given"));
        assertNotNull(webToken.getRequestingPractitioner().getName().get("prefix"));
        assertNotNull(webToken.getRequestingPractitioner().getIdentifierValue(SystemURL.SDS_USER_ID));
        assertNotNull(webToken.getRequestingPractitioner().getPractitionerRoleCode(SystemURL.VS_SDS_JOB_ROLE_NAME));
    }

    private static void assertNotNull(Object object) {
        if (null == object) {
            throw new InvalidRequestException("JSON entry incomplete.", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    "JSON Incomplete", SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }

    private static void verifyTimeValues(WebToken webToken, int futureRequestLeeway) {
        // Checking the creation date is not in the future
        int timeValidationIdentifierInt = webToken.getIat();

        // Checking creation time is not in the future (with a 5 second leeway
        if (timeValidationIdentifierInt > (System.currentTimeMillis() / 1000) + futureRequestLeeway) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    "Creation time is in the future", SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        // Checking the expiry time is 5 minutes after creation
        if (webToken.getExp() - timeValidationIdentifierInt != 300) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    "Request time expired", SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }

    private static void verifyRequestedResourceValues(WebToken webToken) {
        // Checking the reason for request is directcare
        if (!"directcare".equals(webToken.getReasonForRequest())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    "Reason for request is not directcare", SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        RequestingDevice requestingDevice = webToken.getRequestingDevice();

        if (null == requestingDevice) {
            throw new InvalidRequestException("No requesting_device", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    "Requesting device is null", SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("InvalidResourceType".equals(requestingDevice.getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.INVALID_RESOURCE_TYPE, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("InvalidResourceType".equals(webToken.getRequestingOrganization().getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.INVALID_RESOURCE_TYPE, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("InvalidResourceType".equals(webToken.getRequestingPractitioner().getResourceType())) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.INVALID_RESOURCE_TYPE, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("Patient".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("patient/\\*\\.(read|write)")) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.INVALID_RESOURCE_TYPE, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if ("Organization".equals(webToken.getRequestedRecord().getResourceType())
                && !webToken.getRequestedScope().matches("organization/\\*\\.(read|write)")) {
            throw new InvalidRequestException("Bad Request Exception", OperationOutcomeFactory.buildOperationOutcome(
                    SystemURL.VS_GPC_ERROR_WARNING_CODE, SystemCode.BAD_REQUEST,
                    CodableConceptText.INVALID_RESOURCE_TYPE, SystemURL.SD_GPC_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }
    }
}
