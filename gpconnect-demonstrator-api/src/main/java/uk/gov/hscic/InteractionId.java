package uk.gov.hscic;

import java.util.Arrays;
import java.util.List;

public final class InteractionId {

    private InteractionId() { }

    private static final String BASE = "urn:nhs:names:services:gpconnect:fhir:";

    public static final String CLAIM_PATIENT_ALLERGY_INTOLERANCE       = BASE + "claim:patient/AllergyIntolerance.read-1";
    public static final String CLAIM_PATIENT_APPOINTMENT               = BASE + "claim:patient/Appointment.read-1";
    public static final String CLAIM_PATIENT_CONDITION                 = BASE + "claim:patient/Condition.read-1";
    public static final String CLAIM_PATIENT_DIAGNOSTIC_ORDER          = BASE + "claim:patient/DiagnosticOrder.read-1";
    public static final String CLAIM_PATIENT_DIAGNOSTIC_REPORT         = BASE + "claim:patient/DiagnosticReport.read-1";
    public static final String CLAIM_PATIENT_ENCOUNTER                 = BASE + "claim:patient/Encounter.read-1";
    public static final String CLAIM_PATIENT_FLAG                      = BASE + "claim:patient/Flag.read-1";
    public static final String CLAIM_PATIENT_IMMUNIZATION              = BASE + "claim:patient/Immunization.read-1";
    public static final String CLAIM_PATIENT_MEDICATION_ADMINISTRATION = BASE + "claim:patient/MedicationAdministration.read-1";
    public static final String CLAIM_PATIENT_MEDICATION_DISPENSE       = BASE + "claim:patient/MedicationDispense.read-1";
    public static final String CLAIM_PATIENT_MEDICATION_ORDER          = BASE + "claim:patient/MedicationOrder.read-1";
    public static final String CLAIM_PATIENT_OBSERVATION               = BASE + "claim:patient/Observation.read-1";
    public static final String CLAIM_PATIENT_PROCEDURES                = BASE + "claim:patient/Procedures.read-1";
    public static final String OPERATION_GPC_GET_CARE_RECORD           = BASE + "operation:gpc.getcarerecord-1";
    public static final String OPERATION_GPC_REGISTER_PATIENT          = BASE + "operation:gpc.registerpatient-1";
    public static final String OPERATION_GPC_GET_STRUCTURED_RECORD     = BASE + "operation:gpc.getstructuredrecord-1";
    public static final String REST_CREATE_APPOINTMENT                 = BASE + "rest:create:appointment-1";
    public static final String REST_CREATE_ORDER                       = BASE + "rest:create:order-1";
    public static final String REST_READ_APPOINTMENT                   = BASE + "rest:read:appointment-1";
    public static final String REST_READ_LOCATION                      = BASE + "rest:read:location-1";
    public static final String REST_READ_METADATA                      = BASE + "rest:read:metadata-1";
    public static final String REST_READ_ORGANIZATION                  = BASE + "rest:read:organization-1";
    public static final String REST_READ_ORGANIZATION_OLD              = BASE + "rest:read:organization-1";
    public static final String REST_READ_PATIENT                       = BASE + "rest:read:patient-1";
    public static final String REST_READ_PRACTITIONER                  = BASE + "rest:read:practitioner-1";
    public static final String REST_SEARCH_SLOT                        = BASE + "rest:search:slot-1";
    public static final String REST_SEARCH_LOCATION                    = BASE + "rest:search:location-1";
    public static final String REST_SEARCH_ORGANIZATION                = BASE + "rest:search:organization-1";
    public static final String REST_SEARCH_ORGANIZATION_OLD            = BASE + "rest:search:organization-1";
    public static final String REST_SEARCH_PATIENT                     = BASE + "rest:search:patient-1";
    public static final String REST_SEARCH_PATIENT_APPOINTMENTS        = BASE + "rest:search:patient_appointments-1";
    public static final String REST_SEARCH_PRACTITIONER                = BASE + "rest:search:practitioner-1";
    public static final String REST_UPDATE_APPOINTMENT                 = BASE + "rest:update:appointment-1";   
    public static final String REST_CANCEL_APPOINTMENT                 = BASE + "rest:cancel:appointment-1";

    // documents api
    private static final String DOCUMENTS_BASE = "urn:nhs:names:services:gpconnect:documents:fhir:";
    
    public static final String REST_SEARCH_DOCUMENT_REFERENCE          = DOCUMENTS_BASE + "rest:search:documentreference-1";
    public static final String REST_READ_BINARY                        = DOCUMENTS_BASE + "rest:read:binary-1";

    public static final List<String> IDENTIFIER_INTERACTIONS = Arrays.asList(
            REST_SEARCH_LOCATION,
            REST_SEARCH_ORGANIZATION,
            REST_SEARCH_PATIENT,
            REST_SEARCH_PRACTITIONER
            );

}
