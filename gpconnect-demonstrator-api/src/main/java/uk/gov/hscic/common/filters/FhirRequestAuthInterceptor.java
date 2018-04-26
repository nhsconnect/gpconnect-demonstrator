package uk.gov.hscic.common.filters;

import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;

import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemParameter;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.filters.model.WebToken;
import uk.gov.hscic.organization.OrganizationResourceProvider;
import uk.gov.hscic.patient.PatientResourceProvider;
import uk.gov.hscic.util.NhsCodeValidator;

@Component
public class FhirRequestAuthInterceptor extends AuthorizationInterceptor {

    private static final String LOCATION_RESOURCE_NAME = "Location";
    private static final String PRACTITIONER_RESOURCE_NAME = "Practitioner";
    private static final String ORGANIZATION_RESOURCE_NAME = "Organization";
    private static final String PATIENT_RESOURCE_NAME = "Patient";
    private static final String APPOINTMENT_RESOURCE_NAME = "Appointment";
    private static final String ORDER_RESOURCE_NAME = "Order";
    private static final String SCHEDULE_RESOURCE_NAME = "Schedule";
    private static final String SLOT_RESOURCE_NAME = "Slot";
     
    private static final List<String> PERMITTED_ORGANIZATION_IDENTIFIER_SYSTEMS = Arrays.asList(SystemURL.ID_ODS_ORGANIZATION_CODE, SystemURL.ID_ODS_OLD_ORGANIZATION_CODE);

    @Value("${request.leeway:5}")
    private int futureRequestLeeway;

    @Autowired
    WebTokenFactory webTokenFactory;

    private RequestOperation requestOperation;

    private Map<String, Set<String>> validResourceCombinations;

    @PostConstruct
    private void postConstruct() {
        requestOperation = new RequestOperation();

        // only certain combinations of JWT resource type and request resource type are allowed
        // in this map the key is the JWT resource type and the value is a set of permitted request resources
        validResourceCombinations = new HashMap<String, Set<String>>();
        validResourceCombinations.put(PATIENT_RESOURCE_NAME, new HashSet<String>(Arrays.asList(new String[]{PATIENT_RESOURCE_NAME, APPOINTMENT_RESOURCE_NAME})));
        validResourceCombinations.put(ORGANIZATION_RESOURCE_NAME, new HashSet<String>(Arrays.asList(new String[]{ORGANIZATION_RESOURCE_NAME, PRACTITIONER_RESOURCE_NAME, LOCATION_RESOURCE_NAME, ORDER_RESOURCE_NAME,SCHEDULE_RESOURCE_NAME,SLOT_RESOURCE_NAME})));
    }

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails requestDetails) {
        WebToken webToken = webTokenFactory.getWebToken(requestDetails, futureRequestLeeway);

         validateClaim(webToken, requestDetails);
        validateIdentifier(webToken, requestDetails);

        return new RuleBuilder().allowAll().build();
    }

    private void validateOrganizationIdentifier(RequestDetails requestDetails) {
        Map<String, String[]> parameters = requestDetails.getParameters();
        
        if (parameters != null && parameters.containsKey(SystemParameter.IDENTIFIER)) {
            String[] identifierParts = parameters.get(SystemParameter.IDENTIFIER)[0].split("\\|");
            
            String identifierSystem = identifierParts[0];
            
            if (!PERMITTED_ORGANIZATION_IDENTIFIER_SYSTEMS.contains(identifierSystem)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid organization identifier system: " + identifierSystem),
                        SystemCode.BAD_REQUEST, IssueType.INVALID);
            }
        }
    }

    private void validatePatientIdentifier(RequestDetails requestDetails) {
        Map<String, String[]> parameters = requestDetails.getParameters();

        if(parameters != null && parameters.isEmpty() == false) {
            String[] requestIdentifiers = parameters.get(SystemParameter.IDENTIFIER);

            if(requestIdentifiers != null && requestIdentifiers.length > 0) {
                String requestIdentifierValue = requestIdentifiers[0].split("\\|")[1];
                if(!NhsCodeValidator.nhsNumberValid(requestIdentifierValue)) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid NHS number in request: " + requestIdentifierValue),
                            SystemCode.INVALID_NHS_NUMBER, IssueType.INVALID);
                }
            }
        }
    }

    private void validateIdentifier(WebToken webToken, RequestDetails requestDetails) {
        if(PATIENT_RESOURCE_NAME.equals(requestDetails.getResourceName())) {
            validatePatientIdentifier(requestDetails);
        }
        else if(ORGANIZATION_RESOURCE_NAME.equals(requestDetails.getResourceName())) {
            validateOrganizationIdentifier(requestDetails);
        }
    }

    private void validateClaim(WebToken webToken, RequestDetails requestDetails) {

        if (requestOperation.isRead(requestDetails) && webToken.isReadRequestedScope() == false) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The claim requested scope does not match the reqested operation (read)"),
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }

        if (requestOperation.isWrite(requestDetails) && webToken.isWriteRequestedScope() == false) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The claim requested scope does not match the reqested operation (write)"),
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }
    }

    private class RequestOperation {

        Set<String> customReadOperations;
        Set<String> customWriteOperations;
        Set<RestOperationTypeEnum> readOperations;
        Set<RestOperationTypeEnum> writeOperations;

        private RequestOperation() {
            customReadOperations = new HashSet<String>();
            customReadOperations.addAll(PatientResourceProvider.getCustomReadOperations());
            customReadOperations.addAll(OrganizationResourceProvider.getCustomReadOperations());

            customWriteOperations = new HashSet<String>();
            customWriteOperations.addAll(PatientResourceProvider.getCustomWriteOperations());

            readOperations = new HashSet<RestOperationTypeEnum>();
            readOperations.add(RestOperationTypeEnum.READ);
            readOperations.add(RestOperationTypeEnum.SEARCH_SYSTEM);
            readOperations.add(RestOperationTypeEnum.SEARCH_TYPE);
            readOperations.add(RestOperationTypeEnum.VREAD);

            writeOperations = new HashSet<RestOperationTypeEnum>();
            writeOperations.add(RestOperationTypeEnum.CREATE);
            writeOperations.add(RestOperationTypeEnum.DELETE);
            writeOperations.add(RestOperationTypeEnum.PATCH);
            writeOperations.add(RestOperationTypeEnum.UPDATE);
        }

        private boolean isRead(RequestDetails requestDetails) {
            boolean isRead = false;

            RestOperationTypeEnum restOperationType = requestDetails.getRestOperationType();

            isRead = readOperations.contains(restOperationType);
            if(isRead == false) {
                  String operation = requestDetails.getOperation();
                  isRead = customReadOperations.contains(operation);
            }

            return isRead;
        }

        private boolean isWrite(RequestDetails requestDetails) {
            boolean isWrite = false;

            RestOperationTypeEnum restOperationType = requestDetails.getRestOperationType();

            isWrite = writeOperations.contains(restOperationType);
            if(isWrite == false) {
                String operation = requestDetails.getOperation();
                isWrite = customWriteOperations.contains(operation);
            }

            return isWrite;
        }
    }
}
