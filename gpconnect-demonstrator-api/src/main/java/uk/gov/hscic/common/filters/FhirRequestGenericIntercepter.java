package uk.gov.hscic.common.filters;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.hl7.fhir.dstu3.model.OperationOutcome.IssueType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uhn.fhir.parser.DataFormatException;
import ca.uhn.fhir.rest.api.server.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import ca.uhn.fhir.rest.server.exceptions.ResourceVersionConflictException;
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
    private static final String PRACTITIONER_RESOURCE_NAME = "Practitioner";
    private static final String ORGANIZATION_RESOURCE_NAME = "Organization";
    private static final String PATIENT_RESOURCE_NAME = "Patient";

    @PostConstruct
    public void postConstruct() {
        if (providerRoutingFilename != null) {
            Path providerRoutingFilePath = new File(configPath + providerRoutingFilename).toPath();

            if (providerRoutingFilePath.toFile().exists()) {
                try {
                    systemSspToHeader = new ObjectMapper()
                            .readValue(Files.readAllBytes(providerRoutingFilePath), ProviderRouting.class).getAsid();
                } catch (IOException ex) {
                    LOG.error("Error reading providerRoutingFile.");
                }
            }
        }
    }

    @Override
    public boolean incomingRequestPreProcessed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        certificateValidator.validateRequest(httpRequest); // Validate
                                                           // certificate first!

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

        validateInteraction(httpRequest);

        return true;
    }

    private void validateInteraction(HttpServletRequest httpRequest) {
        String interactionIdHeader = httpRequest.getHeader(SystemHeader.SSP_INTERACTIONID);
        if (StringUtils.isBlank(interactionIdHeader)) {
            throwInvalidRequestException(SystemHeader.SSP_INTERACTIONID + " header blank");
        }

        Interaction interaction = interactions.getInteraction(interactionIdHeader);

        if (interaction != null) {
            String httpVerb = httpRequest.getMethod();
            if (interaction.validateHttpVerb(httpVerb)) {
                validateURIAgainstInteraction(interaction, httpRequest.getRequestURI());

                if (InteractionId.IDENTIFIER_INTERACTIONS.contains(interactionIdHeader)) {
                    validateIdentifierSystemAgainstInteraction(interaction,
                            httpRequest.getParameterMap().get(SystemParameter.IDENTIFIER));
                }
            } else {
                throwBadRequestException(String.format(
                        "The interaction ID does not match the HTTP request verb (interaction ID - %s HTTP verb - %s)",
                        interactionIdHeader, httpVerb));
            }
        } else {
            throwBadRequestException(
                    String.format("Unable to locate interaction corresponding to the given interaction ID (%s)",
                            interactionIdHeader));
        }
    }

    private static void throwBadRequestException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(exceptionMessage),
                SystemCode.BAD_REQUEST, IssueType.INVALID);
    }

    private static void throwInvalidIdentifierSystemException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(exceptionMessage),
                SystemCode.INVALID_IDENTIFIER_SYSTEM, IssueType.INVALID);
    }

    private static void throwInvalidRequestException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(new InvalidRequestException(exceptionMessage),
                SystemCode.INVALID_PARAMETER, IssueType.INVALID);
    }

    private static void throwUnprocessableEntityException(String exceptionMessage) {
        throw OperationOutcomeFactory.buildOperationOutcomeException(new UnprocessableEntityException(exceptionMessage),
                SystemCode.INVALID_PARAMETER, IssueType.INVALID);
    }

    private static void throwResourceNotFoundException(String exceptionMessage, String resource) {
        String systemCode = null;

        switch (resource) {
        case ORGANIZATION_RESOURCE_NAME:
            systemCode = SystemCode.ORGANISATION_NOT_FOUND;
            break;

        case PATIENT_RESOURCE_NAME:
            systemCode = SystemCode.PATIENT_NOT_FOUND;
            break;

        case PRACTITIONER_RESOURCE_NAME:
            systemCode = SystemCode.PRACTITIONER_NOT_FOUND;
            break;

        default:
            systemCode = SystemCode.REFERENCE_NOT_FOUND;
        }

        throw OperationOutcomeFactory.buildOperationOutcomeException(new ResourceNotFoundException(exceptionMessage),
                systemCode, IssueType.INVALID);
    }

    /**
     * Listens for any exceptions thrown. In the case of invalid parameters, we
     * need to catch this and throw it as a UnprocessableEntityException.
     *
     * @param theRequestDetails
     * @param theException
     * @param theServletRequest
     * @return UnprocessableEntityException if a InvalidRequestException was
     *         thrown.
     * @throws javax.servlet.ServletException
     */
    @Override
    public BaseServerResponseException preProcessOutgoingException(RequestDetails theRequestDetails,
            Throwable theException, HttpServletRequest theServletRequest) throws ServletException {

        LOG.info("Response Exception");
        LOG.info(theException.getMessage());
        LOG.info("stackTrace: ", theException);

        // This string match is really crude and it's not great, but I can't see
        // how else to pick up on just the relevant exceptions!
        if (theException instanceof InvalidRequestException
                && theException.getMessage().contains("Invalid attribute value")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()), SystemCode.INVALID_PARAMETER,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException
                && theException.getMessage().contains("Unknown resource in URI")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new ResourceNotFoundException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage()
                .contains("Can not have multiple date range parameters for the same param ")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()), SystemCode.INVALID_PARAMETER,
                    IssueType.INVALID);
        }

        if (theException instanceof DataFormatException) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()), SystemCode.INVALID_PARAMETER,
                    IssueType.INVALID);
        }

        if (theException instanceof MethodNotAllowedException
                && theException.getMessage().contains("request must use HTTP GET")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException
                && theException.getMessage().equals("Failed to parse request body as JSON resource. Error was: ")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException
                && theException.getMessage().startsWith("Invalid request: ")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException
                && theException.getMessage().contains("non-repeatable parameter")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().contains("header blank")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new InvalidRequestException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException
                && theException.getMessage().contains("InvalidResourceType")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()), SystemCode.INVALID_RESOURCE,
                    IssueType.INVALID);
        }

        if (theException instanceof InvalidRequestException
                && theException.getMessage().contains("Can not create resource with ID")) {
            return OperationOutcomeFactory.buildOperationOutcomeException(
                    new UnprocessableEntityException(theException.getMessage()), SystemCode.BAD_REQUEST,
                    IssueType.INVALID);
        }

        if (theException instanceof ResourceNotFoundException
                && theException.getMessage().contains("Unknown resource type")) {
            return OperationOutcomeFactory.buildOperationOutcomeException((ResourceNotFoundException) theException,
                    SystemCode.BAD_REQUEST, IssueType.INVALID);
        }

        // BaseServerResponseException outgoingException =
        // outgoingExceptions.toOutgoingException(theException,
        // theRequestDetails);
        // if(outgoingException != null) {
        // return outgoingException;
        // }
        
        if (theException instanceof ResourceVersionConflictException){
            ResourceVersionConflictException exception = (ResourceVersionConflictException) theException;
            
            return OperationOutcomeFactory.buildOperationOutcomeException(exception, SystemCode.FHIR_CONSTRAINT_VIOLATION, IssueType.CONFLICT);
        }

        if (theException instanceof BaseServerResponseException) {
            BaseServerResponseException baseServerResponseException = (BaseServerResponseException) theException;

            // If the OperationalOutcome is already set, just return it.
            return null == baseServerResponseException.getOperationOutcome()
                    ? OperationOutcomeFactory.buildOperationOutcomeException(baseServerResponseException,
                            SystemCode.BAD_REQUEST, IssueType.INVALID)
                    : baseServerResponseException;
        }

        // Default catch all.
        return OperationOutcomeFactory.buildOperationOutcomeException(
                new InvalidRequestException(theException.getMessage()), SystemCode.BAD_REQUEST,
                IssueType.INVALID);
    }

    private void validateURIAgainstInteraction(Interaction interaction, String requestURI) {

        Resource requestResource = Resource.resolveFromRequestUri(requestURI);
        Resource interactionResource = Resource.resolveFromName(interaction.getResource());

        if (interactionResource == null) {
            throwBadRequestException(
                    String.format("Interaction containts invalid resource (%s)", interaction.getResource()));
        }
        if (requestResource == null) {
            // error bad request
            throwResourceNotFoundException(String.format("Request containts invalid resource (%s)", requestURI),
                    requestURI);
        }
        if (interactionResource != requestResource) {
            throwBadRequestException(String.format(
                    "Resource in request does not match resource in interaction (request - %s interaction - %s)",
                    requestResource, interactionResource));
        }

        Operation requestOperation = Operation.resolveFromRequestUri(requestURI);
        Operation interactionOperation = Operation.resolveFromName(interaction.getOperation());
        if (requestOperation != null) {
            if (interactionOperation != null) {
                if (requestOperation != interactionOperation) {
                    throwBadRequestException(String.format(
                            "Operation in request does not match operation in interaction (request - %s interaction - %s)",
                            requestOperation, interactionOperation));
                }
            } else {
                throwBadRequestException(String.format(
                        "Request defines an operation but the interaction does not (request - %s)", requestOperation));
            }
        } else if (interactionOperation != null) {
            throwBadRequestException(
                    String.format("Interaction defines an operation but the request does not (interaction - %s)",
                            interactionOperation));
        }

        // can we retireve a resource based in this - if it's null we say it's
        // an unknown resource
        // otherwise we pass it in?

        // if(interaction != null) {

        if (interaction.validateIdentifier(requestURI) == false) {
            throwResourceNotFoundException(String.format("Unexpected resource identifier in URI - %s", requestURI),
                    interaction.getResource());
        }

        if (interaction.validateContainedResource(requestURI) == false) {
            throwBadRequestException(String.format("Unexpected contained resource in URI - %s", requestURI));
        }

        // if(interaction.validateOperation(requestURI) == false) {
        // throwBadRequestException(String.format("Unexpected resource operation
        // in URI - %s", requestURI));
        // }
        // }
        // else {
        // throwBadRequestException(String.format("Unable to locate interaction
        // corresponding to the given URI (%s)", requestURI));
        // }
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

            if (StringUtils.isNotBlank(identifierSystem) && StringUtils.isNotBlank(identifierValue)) {
                if (interaction.validateIdentifierSystem(identifierSystem) == false) {
                    throwInvalidIdentifierSystemException(
                            String.format("The given identifier system code (%s) is not an expected code - %s",
                                    identifierSystem, interaction.getIdentifierSystems().toString()));
                }
            } else {
                throwUnprocessableEntityException(String.format("The identifier is invalid. System - %s Value - %s",
                        identifierSystem, identifierValue));
            }
        } else {
            throwUnprocessableEntityException(
                    "One or both of the identifier system and value are missing from given identifier : "
                            + identifiers[0]);
        }
    }

    private enum Operation {

        GetCareRecord("$gpc.getcarerecord"), GetSchedule("$gpc.getschedule"), RegisterPatient("$gpc.registerpatient"), GetStructured("$gpc.getstructuredrecord");

        private Operation(String operationName) {
            this.operationName = operationName;
        }

        private String operationName;

        private static final Map<String, Operation> mappings = new HashMap<>();

        static {
            for (Operation operation : values()) {
                mappings.put(operation.operationName, operation);
            }
        }

        public static Operation resolveFromRequestUri(String requestUri) {
            Operation operation = null;

            if (requestUri != null) {
                String operationName = getOperationFromUri(requestUri);
                if (operationName != null) {
                    operation = resolveFromName(operationName);
                    if (operation == null) {
                        throwResourceNotFoundException("Unknown operation type", operationName);
                    }
                }
            }

            return operation;
        }

        public static Operation resolveFromName(String operationName) {
            Operation operation = null;

            if (operationName != null) {
                operation = mappings.get(operationName);
            }

            return operation;
        }

        private static String getOperationFromUri(String requestUri) {
            String operation = null;

            StringTokenizer tok = new StringTokenizer(requestUri, "/");
            while (tok.hasMoreTokens() && operation == null) {
                String nextToken = tok.nextToken();
                if (nextToken.length() > 0) {
                    char firstChar = nextToken.charAt(0);
                    if ('$' == firstChar) {
                        operation = nextToken;
                    }
                }
            }

            return operation;
        }
    }

    private enum Resource {

        AllergyIntolerance, Appointment, Condition, DiagnosticOrder, DiagnosticReport, Encounter, Flag, Immunization, MedicationAdministration, MedicationDispense, MedicationOrder, Observation, Problem, Procedure, Referral, Patient, Organization, Order, Location, metadata, Practitioner, Slot;

        private static final Map<String, Resource> mappings = new HashMap<>();

        static {
            for (Resource resource : values()) {
                mappings.put(resource.name(), resource);
            }
        }

        public static Resource resolveFromRequestUri(String requestUri) {
            Resource resource = null;

            if (requestUri != null) {
                String resourceName = getResourceFromUri(requestUri);
                resource = resolveFromName(resourceName);
            }

            return resource;
        }

        public static Resource resolveFromName(String resourceName) {
            Resource resource = null;

            if (resourceName != null) {
                resource = mappings.get(resourceName);
            }

            return resource;
        }

        private static String getResourceFromUri(String requestUri) {
            String resource = null;

            StringTokenizer tok = new StringTokenizer(requestUri, "/");
            if (tok.countTokens() > 1) {
                tok.nextToken();
                resource = tok.nextToken();
            }

            return resource;
        }
    }

}
