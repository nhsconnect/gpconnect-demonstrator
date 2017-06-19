package uk.gov.hscic.common.filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import uk.gov.hscic.InteractionId;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.SystemCode;
import uk.gov.hscic.SystemHeader;
import uk.gov.hscic.SystemParameter;
import uk.gov.hscic.auth.CertificateValidator;
import uk.gov.hscic.common.filters.model.Interactions;
import uk.gov.hscic.common.filters.model.Interactions.Interaction;
import uk.gov.hscic.common.ldap.model.ProviderRouting;

@Component
public class FhirRequestGenericIntercepter extends InterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(FhirRequestGenericIntercepter.class);
    
    @Autowired
    private Interactions interactions;

    @Autowired
    private CertificateValidator certificateValidator;

    @Value("${config.path}")
    private String configPath;

    @Value("${gp.connect.provider.routing.filename:#{null}}")
    protected String providerRoutingFilename;

    private String systemSspToHeader;

    @PostConstruct
    public void postConstruct() {
        if (providerRoutingFilename != null) {
            Path providerRoutingFilePath = new File(configPath + providerRoutingFilename).toPath();

            if (providerRoutingFilePath.toFile().exists()) {
                try {
                    systemSspToHeader = new ObjectMapper()
                            .readValue(Files.readAllBytes(providerRoutingFilePath), ProviderRouting.class)
                            .getAsid();
                } catch (IOException ex) {
                    LOG.error("Error reading providerRoutingFile.");
                }
            }
        }
    }

    @Override
    public boolean incomingRequestPreProcessed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        certificateValidator.validateRequest(httpRequest); // Validate certificate first!

        // Check there is a Ssp-TraceID header
        if (StringUtils.isBlank(httpRequest.getHeader(SystemHeader.SSP_TRACEID))) {
            throwInvalidRequestException(SystemHeader.SSP_TRACEID + "header blank");
        }

        // Check there is a SSP-From header
        if (StringUtils.isBlank(httpRequest.getHeader(SystemHeader.SSP_FROM))) {
            throwInvalidRequestException(SystemHeader.SSP_FROM + " header blank");
        }

        // Check the SSP-To header is present and directed to our system
        String toASIDHeader = httpRequest.getHeader(SystemHeader.SSP_TO);
        if (StringUtils.isBlank(toASIDHeader)) {
            throwInvalidRequestException(SystemHeader.SSP_TO + " header blank");
        } else if (systemSspToHeader != null && !toASIDHeader.equalsIgnoreCase(systemSspToHeader)) {
            // We loaded our ASID but the SSP-To header does not match the value
            throwBadRequestException(SystemHeader.SSP_TO + " header does not match ASID of system");
        }

        String interactionIdHeader = httpRequest.getHeader(SystemHeader.SSP_INTERACTIONID);
        if (StringUtils.isBlank(interactionIdHeader)) {
            throwInvalidRequestException(SystemHeader.SSP_INTERACTIONID + " header blank");
        }

        Interaction interaction = interactions.getInteraction(interactionIdHeader);
        
        validateURIAgainstInteraction(interaction, httpRequest.getRequestURI());
        
        if (InteractionId.IDENTIFIER_INTERACTIONS.contains(interactionIdHeader)) {
            validateIdentifierSystemAgainstInteraction(interaction, httpRequest.getParameterMap().get(SystemParameter.IDENTIFIER));
        }
        
        return true;
    }

    private static void throwBadRequestException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new InvalidRequestException(exceptionMessage),
                SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
    }

    private static void throwInvalidIdentifierSystemException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new InvalidRequestException(exceptionMessage),
                SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueTypeEnum.INVALID_CONTENT);
    }

    private static void throwInvalidRequestException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new InvalidRequestException(exceptionMessage),
                SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
    }

    private static void throwUnprocessableEntityException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(
                new UnprocessableEntityException(exceptionMessage),
                SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
    }
    
    private static void throwResourceNotFoundException(String exceptionMessage, String resource) {
		String systemCode = null;
		
		switch(resource) {
            case "Organization":
                systemCode = SystemCode.ORGANISATION_NOT_FOUND;
                break;

            case "Patient":
                systemCode = SystemCode.PATIENT_NOT_FOUND;
                break;

            case "Practitioner":
                systemCode = SystemCode.PRACTITIONER_NOT_FOUND;
                break;

            default:
                systemCode = SystemCode.REFERENCE_NOT_FOUND;                
		}
    	
		throw OperationOutcomeFactory.buildOperationOutcomeException(
		new ResourceNotFoundException(exceptionMessage),
		systemCode, IssueTypeEnum.INVALID_CONTENT);   	
    }

    /**
     * Listens for any exceptions thrown. In the case of invalid parameters, we
     * need to catch this and throw it as a UnprocessableEntityException.
     *
     * @param theRequestDetails
     * @param theException
     * @param theServletRequest
     * @return UnprocessableEntityException if a InvalidRequestException was thrown.
     * @throws javax.servlet.ServletException
     */
    @Override
    public BaseServerResponseException preProcessOutgoingException(RequestDetails theRequestDetails,
            Throwable theException, HttpServletRequest theServletRequest) throws ServletException {
        // This string match is really crude and it's not great, but I can't see
        // how else to pick up on just the relevant exceptions!
        if (theException instanceof InvalidRequestException && theException.getMessage().contains("Invalid attribute value")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()),
                    SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof DataFormatException) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()),
                    SystemCode.INVALID_PARAMETER, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof MethodNotAllowedException && theException.getMessage().contains("request must use HTTP GET")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof InvalidRequestException &&
                theException.getMessage().equals("Failed to parse request body as JSON resource. Error was: Failed to parse JSON content, error was: Did not find any content to parse")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().startsWith("Invalid request: ")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().contains("non-repeatable parameter")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().contains("header blank")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().contains("InvalidResourceType")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()),
                    SystemCode.INVALID_RESOURCE, IssueTypeEnum.INVALID_CONTENT);
        }
        
        if(theException instanceof BaseServerResponseException) {
            return (BaseServerResponseException) theException;
        }
        else {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);
        }

        //return super.preProcessOutgoingException(theRequestDetails, theException, theServletRequest);
    }
    
    private void validateURIAgainstInteraction(Interaction interaction, String requestURI) {
    	
    	if(interaction != null) { 	
	    	if(interaction.validateResource(requestURI) == false) {
				throwBadRequestException(String.format("Unknown resource in URI - %s", requestURI));  		
	    	}
	    	
	    	if(interaction.validateIdentifier(requestURI) == false) {
	    		throwResourceNotFoundException(String.format("Unknown resource identifier in URI - %s", requestURI), interaction.getResource());	
	    	}
	    	
	    	if(interaction.validateContainedResource(requestURI) == false) {
	    		throwBadRequestException(String.format("Unknown contained resource in URI - %s", requestURI));  			   		
	    	}
	    	
	    	if(interaction.validateOperation(requestURI) == false) {
	    		throwBadRequestException(String.format("Unknown resource operation in URI - %s", requestURI));    		
	    	}
    	}
    	else {
            throwBadRequestException(String.format("Unable to locate interaction corresponding to the given URI (%s)", requestURI));
    	}
    }   
    
    private void validateIdentifierSystemAgainstInteraction(Interaction interaction, String[] identifiers) {

        if (null == identifiers) {
            throwBadRequestException("No identifier parameter found!");
        }

        if (1 != identifiers.length) {
            throwBadRequestException("Invalid quantity of identifier parameter found: " + identifiers.length);
        }

        String[] identifierParts = identifiers[0].split("\\|");

        if (identifierParts.length == 2) {
            String identifierSystem = identifierParts[0];
            String identifierValue = identifierParts[1];
            
            if(StringUtils.isNotBlank(identifierSystem) && StringUtils.isNotBlank(identifierValue)) {
                if(interaction.validateIdentifierSystem(identifierSystem) == false) {
                    throwInvalidIdentifierSystemException(String.format("The given identifier system code (%s) does not match the expected code - %s", identifierSystem, interaction.getIdentifierSystem()));
                }
            }
            else {
                throwUnprocessableEntityException(String.format("The identifier is invalid. System - %s Value - %s", identifierSystem, identifierValue));
            }
        }
        else {
            throwUnprocessableEntityException("One or both of the identifier system and value are missing from given identifier : " + identifiers[0]);
        }
    }    
}
