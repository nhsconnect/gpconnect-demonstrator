package uk.gov.hscic.common.filters.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.springframework.stereotype.Component;
import uk.gov.hscic.InteractionId;
import uk.gov.hscic.SystemURL;

@Component
public class Interactions {
	private final Map<String, Interaction> interactions = new HashMap<String, Interaction>() {{
        put(InteractionId.CLAIM_PATIENT_ALLERGY_INTOLERANCE, new Interaction("AllergyIntolerance"));
        put(InteractionId.CLAIM_PATIENT_APPOINTMENT, new Interaction("Appointment"));
        put(InteractionId.CLAIM_PATIENT_CONDITION, new Interaction("Condition"));
        put(InteractionId.CLAIM_PATIENT_DIAGNOSTIC_ORDER, new Interaction("DiagnosticOrder"));
        put(InteractionId.CLAIM_PATIENT_DIAGNOSTIC_REPORT, new Interaction("DiagnosticReport"));
        put(InteractionId.CLAIM_PATIENT_ENCOUNTER, new Interaction("Encounter"));
        put(InteractionId.CLAIM_PATIENT_FLAG, new Interaction("Flag"));
        put(InteractionId.CLAIM_PATIENT_IMMUNIZATION, new Interaction("Immunization"));
        put(InteractionId.CLAIM_PATIENT_MEDICATION_ADMINISTRATION, new Interaction("MedicationAdministration"));
        put(InteractionId.CLAIM_PATIENT_MEDICATION_DISPENSE, new Interaction("MedicationDispense"));
        put(InteractionId.CLAIM_PATIENT_MEDICATION_ORDER, new Interaction("MedicationOrder"));
        put(InteractionId.CLAIM_PATIENT_OBSERVATION, new Interaction("Observation"));
        put(InteractionId.CLAIM_PATIENT_PROBLEM, new Interaction("Problem"));
        put(InteractionId.CLAIM_PATIENT_PROCEDURES, new Interaction("Procedure"));
        put(InteractionId.CLAIM_PATIENT_REFERRAL, new Interaction("Referral"));
        put(InteractionId.OPERATION_GPC_GET_CARE_RECORD, new Interaction("Patient").operation("$gpc.getcarerecord"));
        put(InteractionId.OPERATION_GPC_GET_SCHEDULE, new Interaction("Organization").identifier().operation("$gpc.getschedule"));
        put(InteractionId.OPERATION_GPC_REGISTER_PATIENT, new Interaction("Patient").operation("$gpc.registerpatient"));
        put(InteractionId.REST_CREATE_APPOINTMENT, new Interaction("Appointment"));
        put(InteractionId.REST_CREATE_ORDER, new Interaction("Order"));
        put(InteractionId.REST_READ_APPOINTMENT, new Interaction("Appointment").identifier());
        put(InteractionId.REST_READ_LOCATION, new Interaction("Location").identifier());
        put(InteractionId.REST_READ_METADATA, new Interaction("metadata"));
        put(InteractionId.REST_READ_ORGANIZATION, new Interaction("Organization").identifier());
        put(InteractionId.REST_READ_PATIENT, new Interaction("Patient").identifier());
        put(InteractionId.REST_READ_PRACTITIONER, new Interaction("Practitioner").identifier());
        put(InteractionId.REST_SEARCH_LOCATION, new Interaction("Location").addIdentifierSystems(SystemURL.ID_ODS_SITE_CODE));
        put(InteractionId.REST_SEARCH_ORGANIZATION, new Interaction("Organization").addIdentifierSystems(SystemURL.ID_ODS_ORGANIZATION_CODE, SystemURL.ID_ODS_SITE_CODE));
        put(InteractionId.REST_SEARCH_PATIENT, new Interaction("Patient").addIdentifierSystems(SystemURL.ID_NHS_NUMBER));
        put(InteractionId.REST_SEARCH_PATIENT_APPOINTMENTS, new Interaction("Patient").identifier().containedResource("Appointment"));
        put(InteractionId.REST_SEARCH_PRACTITIONER, new Interaction("Practitioner").addIdentifierSystems(SystemURL.ID_SDS_USER_ID));
        put(InteractionId.REST_UPDATE_APPOINTMENT, new Interaction("Appointment").identifier());
    }};

	public Interaction getInteraction(String interactionId) {
		return interactions.get(interactionId);
	}

	public static class Interaction  {
		private static final Pattern WILDCARD = Pattern.compile(".+");

		private String resource = null;
		private Pattern resourcePattern = null;
		private Pattern containedResourcePattern = null;
		private Pattern identifierPattern = null;
		private Pattern operationPattern = null;
		private final Set<String> identifierSystems = new HashSet<>();

		private String currentRegex = "/fhir";

		private Interaction(String resource) {
			this.resource = resource;

			resourcePattern = buildPattern(resource);
			containedResourcePattern = WILDCARD;
			identifierPattern = WILDCARD;
			operationPattern = WILDCARD;
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

		private Interaction addIdentifierSystems(String... identifierSystem) {
		    this.identifierSystems.addAll(Arrays.asList(identifierSystem));

		    return this;
		}

		private Pattern buildPattern(String end) {
			currentRegex = currentRegex + "/" + end;

			return Pattern.compile(currentRegex + ".*");
		}

		public String getResource() {
			return resource;
		}

		public Set<String> getIdentifierSystems() {
		    return this.identifierSystems;
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

		public boolean validateIdentifierSystem(String identifierSystem) {
            return null == identifierSystem
                    ? true
                    : identifierSystems.contains(identifierSystem);
		}
	}
}
