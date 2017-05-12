package uk.gov.hscic.common.filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
import uk.gov.hscic.SystemURL;
import uk.gov.hscic.auth.CertificateValidator;
import uk.gov.hscic.common.ldap.model.ProviderRouting;

@Component
public class FhirRequestGenericIntercepter extends InterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(FhirRequestGenericIntercepter.class);

    private static final Map<String, String> INTERACTION_MAP = new HashMap<String, String>() {{
        put(InteractionId.CLAIM_PATIENT_ALLERGY_INTOLERANCE, "/fhir/AllergyIntolerance");
        put(InteractionId.CLAIM_PATIENT_APPOINTMENT, "/fhir/Appointment");
        put(InteractionId.CLAIM_PATIENT_CONDITION, "/fhir/Condition");
        put(InteractionId.CLAIM_PATIENT_DIAGNOSTIC_ORDER, "/fhir/DiagnosticOrder");
        put(InteractionId.CLAIM_PATIENT_DIAGNOSTIC_REPORT, "/fhir/DiagnosticReport");
        put(InteractionId.CLAIM_PATIENT_ENCOUNTER, "/fhir/Encounter");
        put(InteractionId.CLAIM_PATIENT_FLAG, "/fhir/Flag");
        put(InteractionId.CLAIM_PATIENT_IMMUNIZATION, "/fhir/Immunization");
        put(InteractionId.CLAIM_PATIENT_MEDICATION_ADMINISTRATION, "/fhir/MedicationAdministration");
        put(InteractionId.CLAIM_PATIENT_MEDICATION_DISPENSE, "/fhir/MedicationDispense");
        put(InteractionId.CLAIM_PATIENT_MEDICATION_ORDER, "/fhir/MedicationOrder");
        put(InteractionId.CLAIM_PATIENT_OBSERVATION, "/fhir/Observation");
        put(InteractionId.CLAIM_PATIENT_PROBLEM, "/fhir/Problem");
        put(InteractionId.CLAIM_PATIENT_PROCEDURES, "/fhir/Procedure");
        put(InteractionId.CLAIM_PATIENT_REFERRAL, "/fhir/Referral");
        put(InteractionId.OPERATION_GPC_GET_CARE_RECORD, "/fhir/Patient/$gpc.getcarerecord");
        put(InteractionId.OPERATION_GPC_GET_SCHEDULE, "/fhir/Organization/%ID%/$gpc.getschedule");
        put(InteractionId.OPERATION_GPC_REGISTER_PATIENT, "/fhir/Patient/$gpc.registerpatient");
        put(InteractionId.REST_CREATE_APPOINTMENT, "/fhir/Appointment");
        put(InteractionId.REST_CREATE_ORDER, "/fhir/Order");
        put(InteractionId.REST_READ_APPOINTMENT, "/fhir/Appointment/%ID%");
        put(InteractionId.REST_READ_LOCATION, "/fhir/Location/%ID%");
        put(InteractionId.REST_READ_METADATA, "/fhir/metadata");
        put(InteractionId.REST_READ_ORGANIZATION, "/fhir/Organization/%ID%");
        put(InteractionId.REST_READ_PATIENT, "/fhir/Patient/%ID%");
        put(InteractionId.REST_READ_PRACTITIONER, "/fhir/Practitioner/%ID%");
        put(InteractionId.REST_SEARCH_LOCATION, "/fhir/Location");
        put(InteractionId.REST_SEARCH_ORGANIZATION, "/fhir/Organization");
        put(InteractionId.REST_SEARCH_PATIENT, "/fhir/Patient");
        put(InteractionId.REST_SEARCH_PATIENT_APPOINTMENTS, "/fhir/Patient/%ID%/Appointment");
        put(InteractionId.REST_SEARCH_PRACTITIONER, "/fhir/Practitioner");
        put(InteractionId.REST_UPDATE_APPOINTMENT, "/fhir/Appointment/%ID%");
    }};

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

        String url = httpRequest.getRequestURI();
        if (!url.equals(getInteractionAndAddId(interactionIdHeader, url))) {
            throwBadRequestException("InteractionId Incorrect");
        }

        if (InteractionId.IDENTIFIER_INTERACTIONS.contains(interactionIdHeader)) {
            String[] identifiers = httpRequest.getParameterMap().get(SystemParameter.IDENTIFIER);

            if (null == identifiers) {
                throwBadRequestException("No identifier parameter found!");
            }

            if (1 != identifiers.length) {
                throwBadRequestException("Invalid quantity of identifier parameter found: " + identifiers.length);
            }

            String[] identifierParts = identifiers[0].split("\\|");

            if (identifierParts.length != 2 || StringUtils.isBlank(identifierParts[0]) || StringUtils.isBlank(identifierParts[1])) {
                throwUnprocessableEntityException("Missing identifier value: " + identifiers[0]);
            }

            switch (interactionIdHeader) {
                case InteractionId.REST_SEARCH_PATIENT:
                    if (!SystemURL.ID_NHS_NUMBER.equals(identifierParts[0])) {
                        throwInvalidIdentifierSystemException("Bad system code: " + identifierParts[0]);
                    }

                    break;

                case InteractionId.REST_SEARCH_ORGANIZATION:
                    if (!SystemURL.ID_ODS_ORGANIZATION_CODE.equals(identifierParts[0]) && !SystemURL.ID_ODS_SITE_CODE.equals(identifierParts[0])) {
                        throwInvalidIdentifierSystemException("Bad system code: " + identifierParts[0]);
                    }

                    break;

                case InteractionId.REST_SEARCH_PRACTITIONER:
                    if (!SystemURL.ID_SDS_USER_ID.equals(identifierParts[0])) {
                        throwInvalidIdentifierSystemException("Bad system code: " + identifierParts[0]);
                    }

                    break;

                default:
                    // Fine for now, but this eventually needs implementing for all options.
            }
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

        return super.preProcessOutgoingException(theRequestDetails, theException, theServletRequest);
    }

    // This method finds the ID and inserts it
    // doesn't always insert the ID - as sometimes there isn't one to insert
    private static String getInteractionAndAddId(String interactionIdHeader, String url) {
    	String interactionAndId = null;
    	
    	Interaction interaction = Interaction.getInteraction(interactionIdHeader);
    	if(interaction != null) {
    		try {
        		ResourcePath resourcePath = interaction.getResourcePath(url);
        		
        		if(resourcePath != null) {
        			interactionAndId = interaction.merge(resourcePath);
        		}
        		else {
        			// 400
                    throw OperationOutcomeFactory.buildOperationOutcomeException(
                            new InvalidRequestException(String.format("The given URL %s does not match the interaction scheme %s (interaction Id - %s)", url, interaction.toString(), interactionIdHeader)),
                            SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);  
        		}
        	}
        	catch(NumberFormatException nfe) {
        		String systemCode = null;	

        		switch(interaction.resourceType) {
    	            case "Appointment":
    	                systemCode = SystemCode.APPOINTMENT_NOT_FOUND;
    	                break;
    	
    	            case "Location":
    	                systemCode = SystemCode.LOCATION_NOT_FOUND;
    	                break;
    	
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
        		// 404
                throw OperationOutcomeFactory.buildOperationOutcomeException(
                        new ResourceNotFoundException("The resource identifier in " + url + " is not a number"),
                        systemCode, IssueTypeEnum.INVALID_CONTENT);    		
        	}
    	}
    	else {
			// 400
            throw OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(String.format("The given URL %s does not match the interaction scheme matching the given ID (%s)", url, interactionIdHeader)),
                    SystemCode.BAD_REQUEST, IssueTypeEnum.INVALID_CONTENT);  
    	}

    	return interactionAndId;
    }
    
    private static class Interaction {
    	private String path = null;
    	
    	private String base = null;
    	private String resourceType = null;
    	private String resourceIdPlaceHolder = null;
    	private String operation = null;
    	
		@Override
    	public String toString() {
    		return path;
    	}
    	
    	private Interaction(String path) {
    		this.path = path;
    		
    		String[] elements = getElements(path);
    		
    		// the path needs to have a minimum of 2 elements
    		int nameCount = elements.length;
			if(nameCount >= 3) {
				base = getBase(elements);
				resourceType = getResourceType(elements);
    		
	    		if(nameCount >= 4) {
	    			resourceIdPlaceHolder = getResourceId(elements);
	    			
	    			if("%ID%".equals(resourceIdPlaceHolder) == false) {
	    				throw new IllegalArgumentException("blah");
	    			}
	    			
	    			if(nameCount >= 5) {
	    				operation = getOperation(elements);
	    			}    			
	    		}
    		}
			else {
				throw new IllegalArgumentException("blah");
			}
    	}
    	
    	private String getBase(String[] elements) {
    		return getName(elements, 1);
    	}
    	
    	private String getResourceType(String[] elements) {
    		return getName(elements, 2);
    	}
    	
    	private String getResourceId(String[] elements) {
    		return getName(elements, 3);
    	}
    	
    	private String getOperation(String[] elements) {
    		return getName(elements, 4);
    	}
    	
    	private String getName(String[] elements, int index) {
    		String name = null;
    		
    		if((elements.length - 1) >= index) {
    			name = elements[index];
    		}
    		
    		return name;
    	}
    	
    	private String merge(ResourcePath resourcePath) {
    		String mergeResult = path;
    		
    		if(resourceIdPlaceHolder != null) {
    			mergeResult = path.replace("%ID%", resourcePath.resourceId.toString());  			
    		}

    		return mergeResult; 
    	}
    	
    	private ResourcePath getResourcePath(String path) {
    		ResourcePath resourcePath = null;
    		
    		// check it matches this interaction
    		String[] elements = getElements(path);

    		boolean valid = false;
    		if(base.equals(getBase(elements))) {
        		if(resourceType.equals(getResourceType(elements))) {
               		if(operation != null) {
	        			if(operation.equals(getOperation(elements))) {
	            			valid = true;
	            		}
        			}
               		else {
               			valid = true;
               		}
        		}
    		}
    		
    		if(valid) {
    			resourcePath = new ResourcePath(getBase(elements), getResourceType(elements), getResourceId(elements), getOperation(elements));
    		}
    		
    		return resourcePath;
    	}
    	
    	private String[] getElements(String path) {
    		return path.split("/");
    	}

    	private static Interaction getInteraction(String interactionId) {
    		Interaction interaction = null;
    		
    		String interactionString = INTERACTION_MAP.getOrDefault(interactionId, "INVALID");
    		if("INVALID".equals(interactionString) == false) {
    			try {
    				interaction = new Interaction(interactionString);
    			}
    			catch(IllegalArgumentException iae) {
    				interaction = null;
    			}
    		}
    		
    		return interaction;
    	}
    }
    
    private static class ResourcePath {
 	
    	private String base = null;
    	private String resourceType = null;
    	private Integer resourceId = null;
    	private String operation = null;

    	private ResourcePath(String base, String resourceType, String resourceId, String operation) {
			this.base = base;
			this.resourceType = resourceType;
			if(resourceId != null) {
				this.resourceId = Integer.parseInt(resourceId);
			}
			this.operation = operation;
		}
    }
}
