package uk.gov.hscic.common.filters;

import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.MethodNotAllowedException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.OperationOutcomeFactory;
import uk.gov.hscic.auth.CertificateValidator;
import uk.gov.hscic.common.ldap.model.ProviderRouting;

@Component
public class FhirRequestGenericIntercepter extends InterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(FhirRequestGenericIntercepter.class);

    private static final String SSP_FROM = "Ssp-From";
    private static final String SSP_INTERACTIONID = "Ssp-InteractionID";
    private static final String SSP_TO = "Ssp-To";
    private static final String SSP_TRACEID = "Ssp-TraceId";

    @Autowired
    private CertificateValidator certificateValidator;

    @Value("${gp.connect.provider.routing.file:#{null}}")
    protected String providerRoutingFile;

    private String systemSspToHeader;

    @PostConstruct
    public void postConstruct() {
        if (providerRoutingFile != null) {
            Path providerRoutingFilePath = Paths.get(providerRoutingFile);

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

        String failedMsg = "";

        // Check there is a Ssp-TraceID header
        if (StringUtils.isBlank(httpRequest.getHeader(SSP_TRACEID))) {
            throwInvalidRequestException(SSP_TRACEID + "header blank");
        }

        // Check there is a SSP-From header
        if (StringUtils.isBlank(httpRequest.getHeader(SSP_FROM))) {
            throwInvalidRequestException(SSP_FROM + " header blank");
        }

        // Check the SSP-To header is present and directed to our system
        String toASIDHeader = httpRequest.getHeader(SSP_TO);
        if (StringUtils.isBlank(toASIDHeader)) {
            throwInvalidRequestException(SSP_TO + " header blank");
        } else if (systemSspToHeader != null && !toASIDHeader.equalsIgnoreCase(systemSspToHeader)) {
            // We loaded our ASID but the SSP-To header does not match the value
            failedMsg += SSP_TO + " header does not match ASID of system,";
        }

        String interactionIdHeader = httpRequest.getHeader(SSP_INTERACTIONID);
        if (StringUtils.isBlank(interactionIdHeader)) {
            throwInvalidRequestException(SSP_INTERACTIONID + " header blank");
        }

        String url = httpRequest.getRequestURI();
        if (!url.equals(createMapUsingId(getIdFromUrl(url)).get(interactionIdHeader))) {
            throwInvalidRequestException("InteractionId Incorrect");
        }

        if (!failedMsg.isEmpty()) {
            try {
                httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, StringUtils.chop(failedMsg));
            } catch (IOException e) {
                LOG.error("Error adding response message for Failed validation: (" + failedMsg + ") " + e.getMessage());
            }

            return false;
        }

        return true;
    }

    private static Map<String, String> createMapUsingId(int id) {
        return new HashMap<String, String>() {{
            put("urn:nhs:names:services:gpconnect:fhir:rest:read:metadata", "/fhir/metadata");
            put("urn:nhs:names:services:gpconnect:fhir:rest:read:patient", "/fhir/Patient/" + id);
            put("urn:nhs:names:services:gpconnect:fhir:rest:search:patient", "/fhir/Patient");
            put("urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner", "/fhir/Practitioner/" + id);
            put("urn:nhs:names:services:gpconnect:fhir:rest:search:practitioner", "/fhir/Practitioner");
            put("urn:nhs:names:services:gpconnect:fhir:rest:read:organization", "/fhir/Organization/" + id);
            put("urn:nhs:names:services:gpconnect:fhir:rest:search:organization", "/fhir/Organization");
            put("urn:nhs:names:services:gpconnect:fhir:rest:read:location", "/fhir/Location/" + id);
            put("urn:nhs:names:services:gpconnect:fhir:rest:search:location", "/fhir/Location");
            put("urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord",
                    "/fhir/Patient/$gpc.getcarerecord");
            put("urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule",
                    "/fhir/Organization/1/$gpc.getschedule");
            put("urn:nhs:names:services:gpconnect:fhir:rest:read:appointment", "/fhir/Appointment/" + id);
            put("urn:nhs:names:services:gpconnect:fhir:rest:create:appointment", "/fhir/Appointment");
            put("urn:nhs:names:services:gpconnect:fhir:rest:update:appointment", "/fhir/Appointment/" + id);
            put("urn:nhs:names:services:gpconnect:fhir:rest:create:order", "/fhir/Order");
            put("urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments",
                    "/fhir/Patient/" + id + "/Appointment");
            put("urn:nhs:names:services:gpconnect:fhir:operation:gpc.registerpatient",
                    "/fhir/Patient/$gpc.registerpatient");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/AllergyIntolerance.read",
                    "/fhir/AllergyIntolerance");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Condition.read", "/fhir/Condition");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/DiagnosticOrder.read",
                    "/fhir/DiagnosticOrder");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/DiagnosticReport.read",
                    "/fhir/DiagnosticReport");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Encounter.read", "/fhir/Encounter");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Flag.read", "/fhir/Flag");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Immunization.read", "/fhir/Immunization");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationOrder.read",
                    "/fhir/MedicationOrder");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationDispense.read",
                    "/fhir/MedicationDispense");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationAdministration.read",
                    "/fhir/MedicationAdministration");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Observation.read", "/fhir/Observation");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Problem.read", "/fhir/Problem");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Procedures.read", "/fhir/Procedure");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Referral.read", "/fhir/Referral");
            put("urn:nhs:names:services:gpconnect:fhir:claim:patient/Appointment.read", "/fhir/Appointment");
        }};
    }

    private static void throwInvalidRequestException(String exceptionMessage) {
        throw new InvalidRequestException(exceptionMessage, OperationOutcomeFactory.buildOperationOutcome(
                OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER, exceptionMessage,
                OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
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
            return new UnprocessableEntityException(theException.getMessage(), OperationOutcomeFactory.buildOperationOutcome(
                    OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER, theException.getMessage(),
                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if (theException instanceof MethodNotAllowedException && theException.getMessage().contains("request must use HTTP GET")) {
            return new UnprocessableEntityException(theException.getMessage(), OperationOutcomeFactory.buildOperationOutcome(
                    OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_BAD_REQUEST, theException.getMessage(),
                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().contains("InvalidResourceType")) {
            return new UnprocessableEntityException(theException.getMessage(), OperationOutcomeFactory.buildOperationOutcome(
                    OperationConstants.SYSTEM_WARNING_CODE, "INVALID_RESOURCE", theException.getMessage(),
                    OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT));
        }

        return super.preProcessOutgoingException(theRequestDetails, theException, theServletRequest);
    }

    // This method finds the patient No which is different from the NHS number
    private static Integer getIdFromUrl(String url) {
        Matcher m = Pattern.compile("-?\\d+").matcher(url);

        return m.find()
                ? Integer.parseInt(m.group())
                : 0;
    }
}
