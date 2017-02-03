package uk.gov.hscic.common.filters;

import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;
import ca.uhn.fhir.rest.method.RequestDetails;
import ca.uhn.fhir.rest.server.exceptions.BaseServerResponseException;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import uk.gov.hscic.OperationConstants;
import uk.gov.hscic.OperationOutcomeFactory;

@Component
public class FhirRequestGenericIntercepter extends InterceptorAdapter {
    private static final Logger LOG = Logger.getLogger(FhirRequestGenericIntercepter.class);

    @Value("${gp.connect.spineproxyconf.path}")
    private String spineroxyConfigPath;

    @Value("${gp.connect.interactionwhitelist.path}")
    private String interactionWhiteListPath;

    @Value("${gp.connect.ErrorSimulation.path}")
    private String errorSimulationPath;

    private String systemSspToHeader = null;
    private HashSet interactionIdWhiteList = null;
    private List<JSONObject> errorSimulationCodes = null;
    private int simulationErrorIndex = 0;

    @PostConstruct
    public void postConstruct() {
        // Load config file
        try {
            systemSspToHeader = new JSONObject(new String(Files.readAllBytes(Paths.get(spineroxyConfigPath)))).getString("toASID");
        } catch (IOException | JSONException e) {
            LOG.error("Error loading SSP header values from file: " + e.getMessage());
        }

        // Load interactionId white list
        try {
            JSONArray whiteListJSONArray = (JSONArray) new JSONObject(new String(Files.readAllBytes(Paths.get(interactionWhiteListPath)))).get("interactionIds");

            if (whiteListJSONArray.length() > 0) {
                interactionIdWhiteList = new HashSet();

                for (int index = 0; index < whiteListJSONArray.length(); index++) {
                    interactionIdWhiteList.add(whiteListJSONArray.getJSONObject(index).getString("interactionId"));
                }
            }
        } catch (IOException | JSONException e) {
            LOG.error("Error loading interactionID White List from file: " + e.getMessage());
        }

        // Load Error File if exists
        try {
            JSONArray errorsJSONArray = (JSONArray) new JSONObject(new String(Files.readAllBytes(Paths.get(errorSimulationPath)))).get("errors");

            if (errorsJSONArray.length() > 0) {
                errorSimulationCodes = new ArrayList<>();

                for (int index = 0; index < errorsJSONArray.length(); index++) {
                    errorSimulationCodes.add(errorsJSONArray.getJSONObject(index));
                }
            }
        } catch (IOException | JSONException e) {
            LOG.error("Error loading error simulation file: " + e.getMessage());
        }
    }

    @Override
    public boolean incomingRequestPreProcessed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
        String failedMsg = "";

        // Check there is a Ssp-TraceID header
        if (StringUtils.isBlank(httpRequest.getHeader("Ssp-TraceId"))) {
            throwInvalidRequestException("SSP-From header blank");
        }

        // Check there is a SSP-From header
        if (StringUtils.isBlank(httpRequest.getHeader("Ssp-From"))) {
            throwInvalidRequestException("SSP-From header blank");
        }

        // Check the SSP-To header is present and directed to our system
        String toASIDHeader = httpRequest.getHeader("Ssp-To");
        if (StringUtils.isBlank(toASIDHeader)) {
            throwInvalidRequestException("SSP-To header blank");
        } else if (systemSspToHeader != null && !toASIDHeader.equalsIgnoreCase(systemSspToHeader)) {
            // We loaded our ASID but the SSP-To header does not match the value
            failedMsg += "SSP-To header does not match ASID of system,";
        }

        String interactionIdHeader = httpRequest.getHeader("Ssp-InteractionID");
        if (interactionIdHeader == null || interactionIdHeader.isEmpty()) {
            throwInvalidRequestException("Ssp-InteractionID header blank");
        }

        if (interactionIdWhiteList != null && !interactionIdWhiteList.contains(interactionIdHeader)) {
            // We managed to load our whilte list but the interaction Id in the header does not exist in the list
            failedMsg += "SSP-InteractionID in not a valid interaction ID for the system,";
        }

        String URL = httpRequest.getRequestURI();
        int id = getIdFromUrl(URL);
        if (!URL.equals(createMapUsingid(id).get(interactionIdHeader))) {
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

        // Mock Spine Error test component
        if (errorSimulationCodes != null) {
            if (simulationErrorIndex >= 0 && simulationErrorIndex < errorSimulationCodes.size()) {
                try {
                    httpResponse.setStatus(errorSimulationCodes.get(simulationErrorIndex).getInt("httpError"));
                    httpResponse.setHeader(HttpHeaders.CONTENT_TYPE, "application/json+fhir");

                    try (PrintWriter writer = httpResponse.getWriter()) {
                        writer.write(
                                "{ \"resourceType\": \"OperationOutcome\", \"issue\": [ { \"severity\": \"error\", \"code\": \"forbidden\", \"details\": { \"coding\": [ { \"system\": \"http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1\", \"code\": \""
                                        + errorSimulationCodes.get(simulationErrorIndex).getString("errorCode")
                                        + "\" } ], \"text\": \""
                                        + errorSimulationCodes.get(simulationErrorIndex).getString("errorDescription")
                                        + "\" }}]}");
                        writer.flush();
                    }

                    simulationErrorIndex++;

                    if (simulationErrorIndex == errorSimulationCodes.size()) {
                        simulationErrorIndex = 0;
                    }

                    return false;
                } catch (Exception e) {
                    LOG.error(e);
                }
            }
        }

        return true;
    }

    private Map<String, String> createMapUsingid(int id) {
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

    private void throwInvalidRequestException(String exceptionMessage) {
        OperationOutcome operationOutcome = OperationOutcomeFactory.buildOperationOutcome(
                OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                exceptionMessage, OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT);

        throw new InvalidRequestException(exceptionMessage, operationOutcome);
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
        // This string match is really crude and it's not great, but I can't see
        // how else to pick up on just the relevant exceptions!
        if (theException instanceof InvalidRequestException && theException.getMessage().contains("Invalid attribute value")) {
            OperationOutcome operationOutcome = OperationOutcomeFactory.buildOperationOutcome(
                    OperationConstants.SYSTEM_WARNING_CODE, OperationConstants.CODE_INVALID_PARAMETER,
                    theException.getMessage(), OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT);

            return new UnprocessableEntityException(theException.getMessage(), operationOutcome);
        }

        if (theException instanceof InvalidRequestException && theException.getMessage().contains("InvalidResourceType")) {
            OperationOutcome operationOutcome = OperationOutcomeFactory.buildOperationOutcome(OperationConstants.SYSTEM_WARNING_CODE,
                    "INVALID_RESOURCE", theException.getMessage(), OperationConstants.META_GP_CONNECT_OPERATIONOUTCOME, IssueTypeEnum.INVALID_CONTENT);

            return new UnprocessableEntityException(theException.getMessage(), operationOutcome);
        }

        return super.preProcessOutgoingException(theRequestDetails, theException, theServletRequest);
    }

    // This method finds the patient No which is different from the NHS number
    private Integer getIdFromUrl(String URL) {
        Matcher m = Pattern.compile("-?\\d+").matcher(URL);

        return m.find() ? Integer.parseInt(m.group()) : 0;
    }
}
