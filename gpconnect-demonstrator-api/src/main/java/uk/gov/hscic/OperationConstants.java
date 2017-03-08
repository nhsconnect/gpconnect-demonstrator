package uk.gov.hscic;

public final class OperationConstants {

    // System Constants
    public static final String SYSTEM_RECORD_SECTION = "http://fhir.nhs.net/ValueSet/gpconnect-record-section-1";
    public static final String SYSTEM_WARNING_CODE = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";

    // Code Constants
    public static final String CODE_BAD_REQUEST = "BAD_REQUEST";
    public static final String CODE_INVALID_IDENTIFIER_SYSTEM = "INVALID_IDENTIFIER_SYSTEM";
    public static final String CODE_INVALID_NHS_NUMBER = "INVALID_NHS_NUMBER";
    public static final String CODE_INVALID_PARAMETER = "INVALID_PARAMETER";
    public static final String CODE_NO_PATIENT_CONSENT = "NO_PATIENT_CONSENT";
    public static final String CODE_PATIENT_NOT_FOUND = "PATIENT_NOT_FOUND";
    public static final String CODE_REFERENCE_NOT_FOUND = "REFERENCE_NOT_FOUND";

    // Codable Concept Constants
    public static final String COD_CONCEPT_RECORD_INVALID_PARAMETER = "Invalid Parameter";
    public static final String COD_CONCEPT_RECORD_INVALID_REFERENCE = "Invalid Reference";
    public static final String COD_CONCEPT_RECORD_INVALID_SECTION_CODE = "Invalid Section Code";
    public static final String COD_CONCEPT_RECORD_MULTIPLE_SECTIONS_ADDED = "Multiple Sections Added";
    public static final String COD_CONCEPT_RECORD_NHS_NUMBER_INVALID = "NHS Number Invalid";
    public static final String COD_CONCEPT_RECORD_NOT_FOUND = "Patient Record Not Found";
    public static final String COD_CONCEPT_RECORD_PATIENT_DATA_CONFIDENTIAL = "Patient Data Confidential";

    // MetaProfile Constants
    public static final String META_GP_CONNECT_CARERECORD_COMPOSITION = "http://fhir.nhs.net/StructureDefinition/gpconnect-carerecord-composition-1";
    public static final String META_GP_CONNECT_OPERATIONOUTCOME = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
    public static final String META_GP_CONNECT_ORGANIZATION = "http://fhir.nhs.net/StructureDefinition/gpconnect-organization-1";
    public static final String META_GP_CONNECT_PATIENT = "http://fhir.nhs.net/StructureDefinition/gpconnect-patient-1";
    public static final String META_GP_CONNECT_PRACTITIONER = "http://fhir.nhs.net/StructureDefinition/gpconnect-practitioner-1";

    public static final String SYSTEM_INVALID = "System Invalid";
    public static final String DATE_RANGES_NOT_ALLOWED = "Date Ranges not allowed to be set";

    public static final String SUMMARY = "Summary";
}
