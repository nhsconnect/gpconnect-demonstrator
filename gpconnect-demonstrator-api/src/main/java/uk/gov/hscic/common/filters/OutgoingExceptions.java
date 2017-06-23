package uk.gov.hscic.common.filters;

import java.util.HashMap;
import java.util.Map;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InternalErrorException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;

class OutgoingExceptions {
    
    /*
     * ca.uhn.fhir.rest.server.exceptions.InvalidRequestException: Failed to parse request body as JSON resource. 
     * Error was: Invalid attribute value "noStart": Invalid date/time format: "noStart"
     */
    
    //Set<ExceptionAdapter> exceptionAdapters;
    
    
    Map<ExceptionMatcher, ExceptionGenerator> exceptions;
       
    // separate matching from action
    
    OutgoingExceptions() {
        
//        // exception matching
//        ExceptionMatcher matchRegisterPatientInvalidRequestException = new ExceptionMatcher()
//                                                                            .whereResouceIs("Patient")
//                                                                            .whereOperationIs("$gpc.registerpatient")
//                                                                            .whereThrowableClassIs(InvalidRequestException.class)
//                                                                            .whereErrorMessageContainsOneOf("Invalid date/time format", 
//                                                                                                            "Unknown resource operation in URI");     
//        
//        
        
        // exception generation
        ExceptionGenerator generateInvalidRequestBadRequestInvalidContentException = (exception, requestDetails) -> { return OperationOutcomeFactory.buildOperationOutcomeException(
                                                                                                                    new InvalidRequestException(exception.getMessage()),
                                                                                                                    SystemCode.BAD_REQUEST, 
                                                                                                                    IssueTypeEnum.INVALID_CONTENT); };

        ExceptionGenerator generateResourceNotFoundReferenceNotFoundInvalidContentException = (exception, requestDetails) -> { return OperationOutcomeFactory.buildOperationOutcomeException(
                                                                                                                            new ResourceNotFoundException(exception.getMessage()),
                                                                                                                            SystemCode.REFERENCE_NOT_FOUND, 
                                                                                                                            IssueTypeEnum.INVALID_CONTENT); };   
                                                                                                                                                                                                                                
        exceptions = new HashMap<>();
        exceptions.put(getExceptionMatcher("Patient", "$gpc.registerpatient", InvalidRequestException.class, "Invalid date/time format"), generateInvalidRequestBadRequestInvalidContentException);
        exceptions.put(getExceptionMatcher(null, null, InvalidRequestException.class, "Unexpected resource operation in URI"), generateResourceNotFoundReferenceNotFoundInvalidContentException);
        exceptions.put(getExceptionMatcher(null, null, InvalidRequestException.class, "Unexpected resource in URI"), generateResourceNotFoundReferenceNotFoundInvalidContentException);
        exceptions.put(getExceptionMatcher("Patient", "$gpc.registerpatient", InternalErrorException.class, "Failed to call access method"), generateInvalidRequestBadRequestInvalidContentException);
        exceptions.put(getExceptionMatcher("Patient", "$gpc.registerpatient", InvalidRequestException.class, "Unknown resource name"), generateInvalidRequestBadRequestInvalidContentException);
        exceptions.put(getExceptionMatcher(null, null, InvalidRequestException.class, "The interaction ID does not match the HTTP request verb"), generateInvalidRequestBadRequestInvalidContentException);
    }
    
    private ExceptionMatcher getExceptionMatcher(String resourceName, String operationName, Class<? extends BaseServerResponseException> throwableClass, String ... errorMessageContents) {
        return new ExceptionMatcher()
        .whereResouceIs(resourceName)
        .whereOperationIs(operationName)
        .whereThrowableClassIs(throwableClass)
        .whereErrorMessageContainsOneOf(errorMessageContents);  
    }
    
    BaseServerResponseException toOutgoingException(Throwable exception, RequestDetails requestDetails) {
        // stream through keys and where there's a match grab the value and call generate
        BaseServerResponseException outgoingException = exceptions.entrySet().stream()
                                                                                .filter(entry -> entry.getKey().matches(requestDetails, exception))
                                                                                .map(entry -> entry.getValue().generate(exception, requestDetails))
                                                                                .findFirst()
                                                                                .orElse(null);
         
        return outgoingException;
    }
    
    @FunctionalInterface
    private interface ExceptionGenerator {
        BaseServerResponseException generate(Throwable exception, RequestDetails requestDetails);
    }
    
    private class ExceptionMatcher {
        private String resourceName;
        private String operationName;
        private Class<? extends Throwable> throwableClass;
        private String[] errorMessagesContent;
        
        private ExceptionMatcher whereResouceIs(String resourceName) {
            this.resourceName = resourceName;
            
            return this;
        }
        
        private ExceptionMatcher whereOperationIs(String operationName) {
            this.operationName = operationName;
            
            return this;
        }
        
        private ExceptionMatcher whereThrowableClassIs(Class<? extends Throwable> throwableClass) {
            this.throwableClass = throwableClass;
            
            return this;
        }
        
        private ExceptionMatcher whereErrorMessageContainsOneOf(String ... errorMessagesContent) {
            this.errorMessagesContent = errorMessagesContent;
            
            return this;
        }
        
        private boolean matches(RequestDetails requestDetails, Throwable exception) {
            boolean matches = true;
            
            matches = ( 
                        resourceNameMatches(requestDetails.getResourceName()) &&
                        operationNameMatches(requestDetails.getOperation()) &&
                        throwableClassMatches(exception.getClass()) &&
                        errorMessageContains(exception.getMessage())
                      );
            
            return matches;
        }
        
        private boolean resourceNameMatches(String resourceName) {
            boolean matches = true;
            
            if(this.resourceName != null) {
                matches = this.resourceName.equals(resourceName);
            }
            
            return matches;
        }
        
        private boolean operationNameMatches(String operationName) {
            boolean matches = true;
            
            if(this.operationName != null) {
                matches = this.operationName.equals(operationName);
            }
            
            return matches;
        }
        
        private boolean throwableClassMatches(Class<? extends Throwable> throwableClass) {
            boolean matches = true;
            
            if(this.throwableClass != null) {
                matches = this.throwableClass.equals(throwableClass);
            }
            
            return matches;
        }
        
        private boolean errorMessageContains(String errorMessageContents) {
            boolean matches = false;
            
            if(this.errorMessagesContent != null && this.errorMessagesContent.length > 0) {
                for(int e = 0; (e < this.errorMessagesContent.length && matches == false); e++) {
                    String currentErrorMessageContents = errorMessagesContent[e];
                    matches = errorMessageContents.contains(currentErrorMessageContents);
                }
            }
            else {
                matches = true;
            }
            
            return matches;
        }
    }
}
