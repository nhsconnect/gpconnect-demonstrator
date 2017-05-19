package uk.gov.hscic.common.filters.model;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import uk.gov.hscic.InteractionId;

@Component
public class Interactions {
	
	public static class Interaction  {
		private static final Pattern WILDCARD = Pattern.compile(".+");
	
		private String resource = null;
		private Pattern resourcePattern = null;
		private Pattern containedResourcePattern = null;
		private Pattern identifierPattern = null;
		private Pattern operationPattern = null;
		
		private String currentRegex = null;
		
		private Interaction() {
			currentRegex = "/fhir";
			
			resourcePattern = WILDCARD;
			containedResourcePattern = WILDCARD;
			identifierPattern = WILDCARD;
			operationPattern = WILDCARD;
		}
				
		private Interaction resource(String resource) {
			this.resource = resource;

			resourcePattern = buildPattern(resource);
			
			return this;
		}
		
		private Interaction containedResource(String containedResource) {
			containedResourcePattern = buildPattern(containedResource);
			
			return this;
		}
		
		private Interaction operation(String operation) {
			this.operationPattern = buildPattern("\\" + operation);

			return this;
		}
		
		private Interaction identifier() {
			// from http://hl7.org/fhir/resource.html#id -
			// Ids can be up to 64 characters long, and contain any combination of upper and lowercase ASCII letters, numerals, "-" and "."
			this.identifierPattern = buildPattern("[A-Za-z0-9\\-\\.]{1,64}");
			
			return this;
		}
		
		private Pattern buildPattern(String end) {
			currentRegex = currentRegex + "/" + end;
			
			return Pattern.compile(currentRegex + ".*");
		}

		public String getResource() {
			return resource;
		}
			
		public boolean validateResource(String uri) {
			return resourcePattern.matcher(uri).matches();
		}
		
		public boolean validateContainedResource(String uri) {
			return containedResourcePattern.matcher(uri).matches();
		}
		
		public boolean validateIdentifier(String uri) {
			return identifierPattern.matcher(uri).matches();
		}
		
		public boolean validateOperation(String uri) {
			return operationPattern.matcher(uri).matches();
		}
	}
	
	private Map<String, Interaction> interactions = null;
	
	@PostConstruct
	public void postConstruct() {
		interactions = new HashMap<String, Interaction>();
		
        interactions.put(InteractionId.CLAIM_PATIENT_ALLERGY_INTOLERANCE, new Interaction().resource("AllergyIntolerance"));
        interactions.put(InteractionId.CLAIM_PATIENT_APPOINTMENT, new Interaction().resource("Appointment"));
        interactions.put(InteractionId.CLAIM_PATIENT_CONDITION, new Interaction().resource("Condition"));
        interactions.put(InteractionId.CLAIM_PATIENT_DIAGNOSTIC_ORDER, new Interaction().resource("DiagnosticOrder"));
        interactions.put(InteractionId.CLAIM_PATIENT_DIAGNOSTIC_REPORT, new Interaction().resource("DiagnosticReport"));
        interactions.put(InteractionId.CLAIM_PATIENT_ENCOUNTER, new Interaction().resource("Encounter"));
        interactions.put(InteractionId.CLAIM_PATIENT_FLAG, new Interaction().resource("Flag"));
        interactions.put(InteractionId.CLAIM_PATIENT_IMMUNIZATION, new Interaction().resource("Immunization"));
        interactions.put(InteractionId.CLAIM_PATIENT_MEDICATION_ADMINISTRATION, new Interaction().resource("MedicationAdministration"));
        interactions.put(InteractionId.CLAIM_PATIENT_MEDICATION_DISPENSE, new Interaction().resource("MedicationDispense"));
        interactions.put(InteractionId.CLAIM_PATIENT_MEDICATION_ORDER, new Interaction().resource("MedicationOrder"));
        interactions.put(InteractionId.CLAIM_PATIENT_OBSERVATION, new Interaction().resource("Observation"));
        interactions.put(InteractionId.CLAIM_PATIENT_PROBLEM, new Interaction().resource("Problem"));
        interactions.put(InteractionId.CLAIM_PATIENT_PROCEDURES, new Interaction().resource("Procedure"));
        interactions.put(InteractionId.CLAIM_PATIENT_REFERRAL, new Interaction().resource("Referral"));
        interactions.put(InteractionId.OPERATION_GPC_GET_CARE_RECORD, new Interaction().resource("Patient").operation("$gpc.getcarerecord"));
        interactions.put(InteractionId.OPERATION_GPC_GET_SCHEDULE, new Interaction().resource("Organization").identifier().operation("$gpc.getschedule"));
        interactions.put(InteractionId.OPERATION_GPC_REGISTER_PATIENT, new Interaction().resource("Patient").operation("$gpc.registerpatient"));
        interactions.put(InteractionId.REST_CREATE_APPOINTMENT, new Interaction().resource("Appointment"));
        interactions.put(InteractionId.REST_CREATE_ORDER, new Interaction().resource("Order"));
        interactions.put(InteractionId.REST_READ_APPOINTMENT, new Interaction().resource("Appointment").identifier());
        interactions.put(InteractionId.REST_READ_LOCATION, new Interaction().resource("Location").identifier());
        interactions.put(InteractionId.REST_READ_METADATA, new Interaction().resource("metadata"));
        interactions.put(InteractionId.REST_READ_ORGANIZATION, new Interaction().resource("Organization").identifier());
        interactions.put(InteractionId.REST_READ_PATIENT, new Interaction().resource("Patient").identifier());
        interactions.put(InteractionId.REST_READ_PRACTITIONER, new Interaction().resource("Practitioner").identifier());
        interactions.put(InteractionId.REST_SEARCH_LOCATION, new Interaction().resource("Location"));
        interactions.put(InteractionId.REST_SEARCH_ORGANIZATION, new Interaction().resource("Organization"));
        interactions.put(InteractionId.REST_SEARCH_PATIENT, new Interaction().resource("Patient"));
        interactions.put(InteractionId.REST_SEARCH_PATIENT_APPOINTMENTS, new Interaction().resource("Patient").identifier().containedResource("Appointment"));
        interactions.put(InteractionId.REST_SEARCH_PRACTITIONER, new Interaction().resource("Practitioner"));
        interactions.put(InteractionId.REST_UPDATE_APPOINTMENT, new Interaction().resource("Appointment").identifier());
	}
	
	public Interaction getInteraction(String interactionId) {
		return interactions.get(interactionId);
	}
}
