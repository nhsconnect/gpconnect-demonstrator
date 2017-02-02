package uk.gov.hscic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ca.uhn.fhir.model.dstu2.composite.CodeableConceptDt;
import ca.uhn.fhir.model.dstu2.resource.OperationOutcome;
import ca.uhn.fhir.model.dstu2.valueset.IssueSeverityEnum;
import ca.uhn.fhir.model.dstu2.valueset.IssueTypeEnum;

public final class ValidInteractionIds {

    public static final List<String> interactionIdlist = Collections.unmodifiableList(new ArrayList<String>() {
        {
            add("urn:nhs:names:services:gpconnect:fhir:rest:read:metadata");
            add("urn:nhs:names:services:gpconnect:fhir:rest:read:patient");
            add("urn:nhs:names:services:gpconnect:fhir:rest:search:patient");
            add("urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner");
            add("urn:nhs:names:services:gpconnect:fhir:rest:search:practitioner");
            add("urn:nhs:names:services:gpconnect:fhir:rest:read:organization");
            add("urn:nhs:names:services:gpconnect:fhir:rest:search:organization");
            add("urn:nhs:names:services:gpconnect:fhir:rest:read:location");
            add("urn:nhs:names:services:gpconnect:fhir:rest:search:location");
            add("urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord");
            add("urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule");
            add("urn:nhs:names:services:gpconnect:fhir:rest:read:appointment");
            add("urn:nhs:names:services:gpconnect:fhir:rest:create:appointment");
            add("urn:nhs:names:services:gpconnect:fhir:rest:update:appointment");
            add("urn:nhs:names:services:gpconnect:fhir:rest:create:order");
            add("urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments");
            add("urn:nhs:names:services:gpconnect:fhir:operation:gpc.registerpatient");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/AllergyIntolerance.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Condition.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/DiagnosticOrder.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/DiagnosticReport.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Encounter.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Flag.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Immunization.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationOrder.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationDispense.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/MedicationAdministration.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Observation.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Problem.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Procedures.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Referral.read");
            add("urn:nhs:names:services:gpconnect:fhir:claim:patient/Appointment.read");

        }
    });

}
