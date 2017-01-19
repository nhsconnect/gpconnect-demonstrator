package uk.gov.hscic.common.filters;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import ca.uhn.fhir.rest.server.interceptor.InterceptorAdapter;

@Component
public class FhirRequestGenericIntercepter extends InterceptorAdapter {

	@Value("${gp.connect.spineproxyconf.path}")
	private String spineroxyConfigPath;

	@Value("${gp.connect.interactionwhitelist.path}")
	private String interactionWhiteListPath;

	@Value("${gp.connect.ErrorSimulation.path}")
	private String errorSimulationPath;

	Logger log = Logger.getLogger(FhirRequestGenericIntercepter.class);

	private String systemSspToHeader = null;
	private HashSet interactionIdWhiteList = null;
	private List<JSONObject> errorSimulationCodes = null;
	private int simulationErrorIndex = 0;

	@PostConstruct
	public void postConstruct() {

		// Load config file
		try {
			String configJson = new String(Files.readAllBytes(Paths.get(spineroxyConfigPath)));
			JSONObject configJSonObj = new JSONObject(configJson);
			systemSspToHeader = configJSonObj.getString("toASID");
		} catch (Exception e) {
			log.error("Error loading SSP header values from file: " + e.getMessage());
		}

		// Load interactionId white list
		try {
			String whiteListJson = new String(Files.readAllBytes(Paths.get(interactionWhiteListPath)));
			JSONObject whiteListJSonObj = new JSONObject(whiteListJson);
			JSONArray whiteListJSONArray = (JSONArray) whiteListJSonObj.get("interactionIds");
			if (whiteListJSONArray.length() > 0) {
				interactionIdWhiteList = new HashSet();
				for (int index = 0; index < whiteListJSONArray.length(); index++) {
					interactionIdWhiteList.add(whiteListJSONArray.getJSONObject(index).getString("interactionId"));
				}
			}

		} catch (Exception e) {
			log.error("Error loading interactionID White List from file: " + e.getMessage());
		}

		// Load Error File if exists
		try {
			String errorSimulationJson = new String(Files.readAllBytes(Paths.get(errorSimulationPath)));
			JSONObject errorSimulationJSonObj = new JSONObject(errorSimulationJson);
			JSONArray errorsJSONArray = (JSONArray) errorSimulationJSonObj.get("errors");
			if (errorsJSONArray.length() > 0) {
				errorSimulationCodes = new ArrayList<>();
				for (int index = 0; index < errorsJSONArray.length(); index++) {
					errorSimulationCodes.add(errorsJSONArray.getJSONObject(index));
				}
			}

		} catch (Exception e) {
			log.error("Error loading error simulation file: " + e.getMessage());
		}

	}

	@Override
	public boolean incomingRequestPreProcessed(HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		String failedMsg = "";
		// Check there is a Ssp-TraceID header
		String TraceID = httpRequest.getHeader("Ssp-TraceId");
		if (TraceID == null || TraceID.isEmpty()) {
			failedMsg = failedMsg + "SSP-From header blank,";
		}
		
		// Check there is a SSP-From header
		String fromASIDHeader = httpRequest.getHeader("Ssp-From");
		if (fromASIDHeader == null || fromASIDHeader.isEmpty()) {
			failedMsg = failedMsg + "SSP-From header blank,";
		}

		// Check the SSP-To header is present and directed to our system
		String toASIDHeader = httpRequest.getHeader("Ssp-To");
		if (toASIDHeader == null || toASIDHeader.isEmpty()) {
			failedMsg = failedMsg + "SSP-To header blank,";
		}
		if (systemSspToHeader != null && !toASIDHeader.equalsIgnoreCase(systemSspToHeader)) {
			// We loaded our ASID but the SSP-To header does not match the value
			failedMsg = failedMsg + "SSP-To header does not match ASID of system,";
		}

		String URL = httpRequest.getRequestURI();
		int id = getIdFromUrl(URL);
		
		// The paths should be added into a file. Currently due to administrator
		// access
		// file cannot be found on local
		Properties prop = new Properties();
		String propFileName = "hello.properties";
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
		
		if (inputStream != null ){
			try {
				prop.load(inputStream);
			
				Enumeration<?> e = prop.propertyNames();
				while (e.hasMoreElements()) {
					String key = (String) e.nextElement();
					String value = prop.getProperty(key);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			
		}
		
		
		List<String> validInteractionIds = new ArrayList<String>();
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:read:metadata");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:read:patient");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:search:patient");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:search:practitioner");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:read:organization");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:search:organization");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:read:location");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:search:location");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:read:appointment");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:create:appointment");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:update:appointment");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:create:order");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:operation:gpc.registerpatient");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/AllergyIntolerance.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Condition.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/DiagnosticOrder.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/DiagnosticReport.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Encounter.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Flag.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Immunization.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationOrder.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationDispense.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationAdministration.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Observation.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Problem.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Procedures.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Referral.read");
		validInteractionIds.add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Appointment.read");

		Map<String, String> map = new HashMap<String, String>() {
			{
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
						"/fhir/Organization/$gpc.getschdule");
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

			}
		};

		String interactionIdHeader = httpRequest.getHeader("Ssp-InteractionID");

		if (interactionIdWhiteList != null && !interactionIdWhiteList.contains(interactionIdHeader)) {
			// We managed to load our whilte list but the interaction Id in the
			// header does not exist in the list
			failedMsg = failedMsg + "SSP-InteractionID in not a valid interaction ID for the system,";
		}
	
		boolean interactionIdValidator = false;
		for (int i = 0; i < validInteractionIds.size(); i++) {
			if (interactionIdHeader.equals(validInteractionIds.get(i))
					&& URL.equals(map.get(validInteractionIds.get(i)))) {
				interactionIdValidator = true;
				break;
			}

		}

		if (interactionIdValidator == false) {
			failedMsg = failedMsg + "InteractionId Incorrect";
		}
		
		if (!failedMsg.isEmpty()) {
			try {
				httpResponse.sendError(400, failedMsg);
			} catch (Exception e) {
				log.error("Error adding response message for Failed validation: (" + failedMsg + ") " + e.getMessage());
			}
			return false;
		}
	
		// Mock Spine Error test component
		if (errorSimulationCodes != null) {
			if (simulationErrorIndex >= 0 && simulationErrorIndex < errorSimulationCodes.size()) {
				try {
					httpResponse.setStatus(errorSimulationCodes.get(simulationErrorIndex).getInt("httpError"));
					httpResponse.setHeader("Content-Type", "application/json+fhir");
					PrintWriter writer = httpResponse.getWriter();
					writer.write(
							"{ \"resourceType\": \"OperationOutcome\", \"issue\": [ { \"severity\": \"error\", \"code\": \"forbidden\", \"details\": { \"coding\": [ { \"system\": \"http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1\", \"code\": \""
									+ errorSimulationCodes.get(simulationErrorIndex).getString("errorCode")
									+ "\" } ], \"text\": \""
									+ errorSimulationCodes.get(simulationErrorIndex).getString("errorDescription")
									+ "\" }}]}");
					writer.flush();
					writer.close();
					simulationErrorIndex++;
					if (simulationErrorIndex == errorSimulationCodes.size())
						simulationErrorIndex = 0;
					
					return false;
				} catch (Exception e) {
					log.error(e);
				}
			}
		}
	
		return true;
	}

	// This method finds the patient No which is different from the NHS number
	private Integer getIdFromUrl(String URL) {
		Pattern p = Pattern.compile("-?\\d+");
		Matcher m = p.matcher(URL);
		if (m.find()) {
			return Integer.parseInt(m.group());
		}
		return 0;
	}

}
