package uk.gov.hscic.common.filters;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.api.RequestTypeEnum;
import ca.uhn.fhir.rest.api.RestOperationTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.interceptor.auth.AuthorizationInterceptor;
import ca.uhn.fhir.rest.server.interceptor.auth.IAuthRule;
import ca.uhn.fhir.rest.server.interceptor.auth.RuleBuilder;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemParameter;
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.common.filters.model.WebToken;

@Component
public class FhirRequestAuthInterceptor extends AuthorizationInterceptor {

    private static final List<String> PERMITTED_ORGANIZATION_IDENTIFIER_SYSTEMS = Arrays.asList(SystemURL.ID_ODS_ORGANIZATION_CODE, SystemURL.ID_ODS_SITE_CODE);

    @Value("${request.leeway:5}")
    private int futureRequestLeeway;
    
    @Autowired
    WebTokenFactory webTokenFactory;
    
    private RequestOperation requestOperation;
    
    @PostConstruct
    private void postConstruct() {
        requestOperation = new RequestOperation();
    }

    @Override
    public List<IAuthRule> buildRuleList(RequestDetails requestDetails) {
        WebToken webToken = webTokenFactory.getWebToken(requestDetails, futureRequestLeeway);

        validateClaim(webToken, requestDetails);
        validateResource(webToken, requestDetails);
        validateIdentifier(webToken, requestDetails);

        return new RuleBuilder().allowAll().build();
    }
    
    private void validateOrganisationIdentifier(WebToken webToken, RequestDetails requestDetails) {
        Map<String, String[]> parameters = requestDetails.getParameters();
        
        if(parameters != null && parameters.isEmpty() == false) {
            String identifierSystem = parameters.get(SystemParameter.IDENTIFIER)[0].split("\\|")[0];
            
            if (!PERMITTED_ORGANIZATION_IDENTIFIER_SYSTEMS.contains(identifierSystem)) {
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new InvalidRequestException("Invalid organization identifier system: " + identifierSystem),
                        SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
            } 
        }
        
        if (null == webToken.getRequestedRecord().getIdentifierValue(SystemURL.ID_ODS_ORGANIZATION_CODE)) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("Bad Request Exception"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
        
    }
    
    private void validatePatientIdentifier(WebToken webToken, RequestDetails requestDetails) {
        Map<String, String[]> parameters = requestDetails.getParameters();
        
        if(parameters != null && parameters.isEmpty() == false) {         
            String[] requestIdentifiers = parameters.get(SystemParameter.IDENTIFIER);
           
            if(requestIdentifiers != null && requestIdentifiers.length > 0) {
                String requestIdentifierValue = requestIdentifiers[0].split("\\|")[1];
                
                String jwtIdentifierValue = webToken.getRequestedRecord().getIdentifierValue(SystemURL.ID_NHS_NUMBER);;        
                
                if(jwtIdentifierValue.equals(requestIdentifierValue) == false) {
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException("Invalid NHS number: " + jwtIdentifierValue),
                            SystemCode.INVALID_NHS_NUMBER, IssueTypeEnum.INVALID_CONTENT);
                }
            }
        }
    }    
    
    private void validateIdentifier(WebToken webToken, RequestDetails requestDetails) {
        if("Patient".equals(requestDetails.getResourceName())) {
            validatePatientIdentifier(webToken, requestDetails);
        }
        else if("Organisation".equals(requestDetails.getResourceName())) {
            validateOrganisationIdentifier(webToken, requestDetails);
        }
    }

    private void validateResource(WebToken webToken, RequestDetails requestDetails) {
        String jwtResource = webToken.getRequestedRecord().getResourceType();
        String requestResource = requestDetails.getResourceName();
        
        if(jwtResource.equals(requestResource) == false) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(String.format("Invalid request resource type from JWT (%s) does not match resource type from request (%s)", jwtResource, requestResource)),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }   
    }

    private void validateClaim(WebToken webToken, RequestDetails requestDetails) {
        
        if (requestOperation.isRead(requestDetails) && webToken.isReadRequestedScope() == false) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The claim requested scope does not match the reqested operation (read)"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
        
        if (requestOperation.isWrite(requestDetails) && webToken.isWriteRequestedScope() == false) {
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException("The claim requested scope does not match the reqested operation (write)"),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }
    }
    
    private class RequestOperation {
        
        Set<RestOperationTypeEnum> customOperations;
        Set<RestOperationTypeEnum> readOperations;
        Set<RestOperationTypeEnum> writeOperations;
           
        private RequestOperation() {
            customOperations = new HashSet<RestOperationTypeEnum>();
            customOperations.add(RestOperationTypeEnum.EXTENDED_OPERATION_INSTANCE);
            customOperations.add(RestOperationTypeEnum.EXTENDED_OPERATION_SERVER);
            customOperations.add(RestOperationTypeEnum.EXTENDED_OPERATION_TYPE);
            
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
                if(customOperations.contains(restOperationType)) {
                    // check the HTTP verb is a read; GET
                    isRead = RequestTypeEnum.GET == requestDetails.getRequestType();
                }    
            }
            
            return isRead;
        }

        private boolean isWrite(RequestDetails requestDetails) {
            boolean isWrite = false;
            
            RestOperationTypeEnum restOperationType = requestDetails.getRestOperationType();
            
            isWrite = writeOperations.contains(restOperationType);
            if(isWrite == false) {
                if(customOperations.contains(restOperationType)) {
                    // check the HTTP verb is a write; PATCH, POST or PUT
                    isWrite = RequestTypeEnum.PATCH == requestDetails.getRequestType() ||
                              RequestTypeEnum.POST == requestDetails.getRequestType() ||
                              RequestTypeEnum.PUT == requestDetails.getRequestType();
                }    
            }
            
            return isWrite;
        }
    }
}
