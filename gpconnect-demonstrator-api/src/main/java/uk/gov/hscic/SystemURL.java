package uk.gov.hscic;

public final class SystemURL {

    private SystemURL() { }

    // Base Constants
    public static final String AUTHORIZATION_TOKEN = "https://authorize.fhir.nhs.net/token";
    public static final String SDS_USER_ID = "http://fhir.nhs.net/sds-user-id";

    // HL7 Constants
    public static final String HL7_BASIC_RESOURCE_TYPE = "http://hl7.org/fhir/basic-resource-type";
    public static final String HL7_VS_C80_PRACTICE_CODES = "http://hl7.org/fhir/ValueSet/c80-practice-codes";
    public static final String HL7_SPECIAL_VALUES = "http://hl7.org/fhir/special-values";




    // FHIR ID Constants
    public static final String ID_GPC_APPOINTMENT_IDENTIFIER = "https://fhir.nhs.uk/Id/gpconnect-appointment-identifier";
    public static final String ID_GPC_SCHEDULE_IDENTIFIER = "https://fhir.nhs.uk/Id/gpconnect-schedule-identifier";
    public static final String ID_NHS_NUMBER = "https://fhir.nhs.uk/Id/nhs-number";
    public static final String ID_ODS_ORGANIZATION_CODE = "https://fhir.nhs.uk/Id/ods-organization-code";
    public static final String ID_ODS_OLD_ORGANIZATION_CODE = "http://fhir.nhs.net/Id/ods-organization-code";
    public static final String ID_ODS_SITE_CODE = "https://fhir.nhs.uk/Id/ods-site-code";
    public static final String ID_LOCAL_LOCATION_IDENTIFIER = "https://fhir.nhs.uk/Id/local-location-identifier";
    public static final String ID_LOCAL_PATIENT_IDENTIFIER = "https://fhir.nhs.uk/Id/local-patient-identifier";
    public static final String ID_SDS_ROLE_PROFILE_ID = "https://fhir.nhs.uk/Id/sds-role-profile-id";
    public static final String ID_SDS_USER_ID = "https://fhir.nhs.uk/Id/sds-user-id";
    public static final String ID_MARITAL_STATUS = "http://fhir.nhs.net/ValueSet/marital-status-1";

    // FHIR StructureDefinition Constants
    public static final String SD_GPC_CARERECORD_COMPOSITION = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-CareRecord-Composition-1";
    public static final String SD_GPC_OPERATIONOUTCOME = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-OperationOutcome-1";
    public static final String SD_GPC_ORGANIZATION = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Organization-1";
    public static final String SD_GPC_PATIENT = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Patient-1";
    public static final String SD_GPC_PRACTITIONER = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1";
    public static final String SD_GPC_APPOINTMENT = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Appointment-1";
    public static final String SD_GPC_GET_SCHEDULE_BUNDLE = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Schedule-1";
    public static final String SD_GPC_LOCATION = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Location-1";
    public static final String SD_GPC_SLOT = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Slot-1";
    public static final String SD_GPC_SCHEDULE = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Schedule-1";
    public static final String SD_GPC_LIST = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-List-1";
    public static final String SD_GPC_SRCHSET_BUNDLE = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Searchset-Bundle-1";
    public static final String SD_GPC_STRUCTURED_BUNDLE = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-GetStructuredRecord-Bundle-1";
    public static final String SD_CC_ALLERGY_INTOLERANCE  = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-AllergyIntolerance-1";
    public static final String SD_GPC_MEDICATION_LIST = "https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Medication-List-1";
    public static final String SD_GPC_OBSERVATION = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Observation-1";
    public static final String SD_GPC_CONDITION = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Condition-1";
    public static final String SD_GPC_MEDICATION_STATEMENT = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationStatement-1";
    public static final String SD_GPC_MEDICATION_REQUEST = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1";
    public static final String SD_GPC_MEDICATION = "https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Medication-1";

    // FHIR StructureDefinition Extension
    public static final String SD_CC_EXT_NHS_NUMBER_VERIF = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-NHSNumberVerificationStatus-1";
    public static final String SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-GPConnect-AppointmentCancellationReason-1";
    public static final String SD_EXTENSION_GPC_PRACTITIONER = "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-practitioner-1";
    public static final String SD_EXTENSION_GPC_PRACTITIONER_ROLE = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-GPConnect-PractitionerRole-1";
    public static final String SD_EXTENSION_GPC_DELIVERY_CHANNEL = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-GPConnect-DeliveryChannel-1";
    public static final String SD_EXTENSION_MEDICATION_QUANTITY_TEXT = "http://fhir.nhs.net/StructureDefinition/extension-medication-quantity-text-1-0";
    public static final String SD_EXTENSION_PERSCRIPTION_REPEAT_REVIEW_DATE = "http://fhir.nhs.net/StructureDefinition/extension-prescription-repeat-review-date-1-0";
    public static final String SD_EXTENSION_REGISTRATION_PERIOD = "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1";
    public static final String SD_EXTENSION_REGISTRATION_STATUS = "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1";
    public static final String SD_EXTENSION_REGISTRATION_TYPE = "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1";
    public static final String SD_CC_EXT_REGISTRATION_PERIOD = "registrationPeriod";
    public static final String SD_CC_EXT_REGISTRATION_STATUS = "registrationStatus";
    public static final String SD_CC_EXT_REGISTRATION_TYPE = "registrationType";
    public static final String SD_CC_EXT_ETHNIC_CATEGORY = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-EthnicCategory-1";
    public static final String SD_EXTENSION_CC_MAIN_LOCATION = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-MainLocation-1";
    public static final String SD_EXTENSION_CC_REG_DETAILS = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-RegistrationDetails-1";
    public static final String SD_EXTENSION_CC_ORG_PERIOD = "http://hl7.org/fhir/StructureDefinition/organization-period";

    public static final String SD_CC_EXT_RELIGIOUS_AFFILI = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-ReligiousAffiliation-1";   
    public static final String SD_PATIENT_CADAVERIC_DON = "http://hl7.org/fhir/StructureDefinition/patient-cadavericDonor";   
    public static final String SD_CC_EXT_RESIDENTIAL_STATUS = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-ResidentialStatus-1";   
    public static final String SD_CC_EXT_TREATMENT_CAT = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-TreatmentCategory-1";   
    public static final String SD_CC_EXT_NHS_COMMUNICATION = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-NHSCommunication-1";   
    public static final String SD_CC_EXT_COMM_LANGUAGE = "Language";   
    public static final String SD_CC_COMM_PREFERRED = "Preferred";   
    public static final String SD_CC_MODE_OF_COMM = "modeOfCommunication";   
    public static final String SD_CC_COMM_PROFICIENCY = "communicationProficiency";   
    public static final String SD_CC_INTERPRETER_REQUIRED = "interpreterRequired";   
    public static final String SD_CC_APPOINTMENT_BOOKINGORG = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-GPConnect-BookingOrganisation-1";
    public static final String SD_CC_APPOINTMENT_CREATED = "https://fhir.nhs.uk/StructureDefinition/extension-gpconnect-appointment-created-1";
    public static final String SD_CC_EXT_ENCOUNTER_ACCOCIATED_ENCOUNTER = "http://hl7.org/fhir/StructureDefinition/encounter-associatedEncounter";
    public static final String SD_CC_EXT_ALLERGY_INTOLERANCE_END = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-AllergyIntoleranceEnd-1";
    public static final String SD_CC_EXT_MEDICATION_STATEMENT_LAST_ISSUE = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-MedicationStatementLastIssueDate-1";
    public static final String SD_CC_EXT_MEDICATION_QUANTITY_TEXT = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-MedicationQuantityText-1";
    public static final String SD_CC_EXT_MEDICATION_REPEAT_INFORMATION = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-MedicationRepeatInformation-1";
    public static final String SD_CC_EXT_MEDICATION_PRESCRIPTION_TYPE = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-PrescriptionType-1";
    public static final String SD_CC_EXT_MEDICATION_STATUS_REASON = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-MedicationStatusReason-1";
	public static final String SD_CC_EXT_REGISTRATION_DETAILS = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-RegistrationDetails-1";
    public static final String SD_EXT_SCT_DESC_ID = "https://fhir.hl7.org.uk/STU3/StructureDefinition/Extension-coding-sctdescid";
    // FHIR ValueSet Constants
    public static final String VS_GPC_ERROR_WARNING_CODE = "https://fhir.nhs.uk/STU3/ValueSet/Spine-ErrorOrWarningCode-1";
    public static final String VS_GPC_REASON_TYPE = "http://fhir.nhs.net/ValueSet/gpconnect-reason-type-1-0";
    public static final String VS_GPC_RECORD_SECTION = "https://fhir.nhs.uk/STU3/ValueSet/GPConnect-RecordSection-1";
    public static final String VS_GPC_DELIVERY_CHANNEL = "https://fhir.nhs.uk/STU3/CodeSystem/GPConnect-DeliveryChannel-1";
    public static final String VS_GPC_PRACTITIONER_ROLE = "https://fhir.nhs.uk/STU3/ValueSet/GPConnect-PractitionerRole-1";
    public static final String VS_HUMAN_LANGUAGE = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-HumanLanguage-1";
    public static final String VS_REGISTRATION_STATUS = "http://fhir.nhs.net/ValueSet/registration-status-1";
    public static final String VS_REGISTRATION_TYPE = "http://fhir.nhs.net/ValueSet/registration-type-1";
    public static final String VS_SDS_JOB_ROLE_NAME = "https://fhir.hl7.org.uk/ValueSet/CareConnect-SDSJobRoleName-1";
    public static final String VS_CC_ORGANISATION_TYPE = "http://hl7.org/fhir/ValueSet/organization-type";
    public static final String VS_CC_ORG_CT_ENTITYTYPE = "http://hl7.org/fhir/ValueSet/contactentity-type";
    public static final String VS_CC_SER_DEL_LOCROLETYPE = "http://hl7.org/fhir/ValueSet/v3-ServiceDeliveryLocationRoleType";
    public static final String VS_CC_MARITAL_STATUS = "https://fhir.nhs.uk/ValueSet/CareConnect-MaritalStatus-1";
    public static final String VS_CC_ETHNIC_CATEGORY = "https://fhir.nhs.uk/ValueSet/CareConnect-EthnicCategory-1";
    public static final String VS_CC_HUMAN_LANG = "https://fhir.nhs.uk/ValueSet/CareConnect-HumanLanguage-1";
    public static final String VS_CC_LANG_ABILITY_MODE = "https://fhir.nhs.uk/ValueSet/CareConnect-LanguageAbilityMode-1";
    public static final String VS_CC_LANG_ABILITY_PROFI = "https://fhir.nhs.uk/ValueSet/CareConnect-LanguageAbilityProficiency-1";
    public static final String VS_CC_RELIGIOUS_AFFILI = "https://fhir.nhs.uk/ValueSet/CareConnect-ReligiousAffiliation-1";
    public static final String VS_CC_RESIDENTIAL_STATUS = "https://fhir.nhs.uk/ValueSet/CareConnect-ResidentialStatus-1";
    public static final String VS_CC_TREATMENT_CAT = "https://fhir.nhs.uk/ValueSet/CareConnect-TreatmentCategory-1";
    public static final String VS_IDENTIFIER_TYPE = "http://hl7.org/fhir/ValueSet/identifier-type";
    public static final String VS_PATIENT_CONTACT_REL = "http://hl7.org/fhir/ValueSet/patient-contact-relationship";
    public static final String VS_SLOT_IDENTIFIER = "http://fhir.nhs.net/Id/gpconnect-slot-identifier";
    public static final String VS_GPC_ORG_TYPE = "https://fhir.nhs.uk/STU3/ValueSet/GPConnect-OrganisationType-1";
    public static final String VS_CONDITION_CODE = "http://hl7.org/fhir/stu3/valueset-condition-code.html";
    public static final String VS_CC_PRESCRIPTION_TYPE = "https://fhir.nhs.uk/STU3/ValueSet/CareConnect-PrescriptionType-1";
    public static final String VS_CC_NHS_NUMBERVERIF = "https://fhir.nhs.uk/STU3/ValueSet/CareConnect-NHSNumberVerificationStatus-1";
    public static final String VS_CC_ETHNIC_CATEGORY_STU3 = "https://fhir.nhs.uk/STU3/ValueSet/CareConnect-EthnicCategory-1";
    public static final String VS_CC_REGISTRATION_TYPE = "https://fhir.nhs.uk/STU3/ValueSet/CareConnect-RegistrationType-1";
//    public static final String VS_HUMAN_LANGUAGE = "http://fhir.nhs.net/ValueSet/human-language-1";

    // FHIR CodeSystem Constants
    public static final String CS_REGISTRATION_STATUS = "https://fhir.nhs.uk/CareConnect-RegistrationStatus-1";
    public static final String CS_REGISTRATION_TYPE = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-RegistrationType-1";
    public static final String CS_CC_ETHNIC_CATEGORY = "https://fhir.nhs.uk/CareConnect-EthnicCategory-1";
    public static final String CS_CC_RELIGIOUS_AFFILI = "https://fhir.nhs.uk/CareConnect-ReligiousAffiliation-1";
    public static final String CS_CC_RESIDENTIAL_STATUS = "https://fhir.nhs.uk/CareConnect-ResidentialStatus-1";
    public static final String CS_CC_TREATMENT_CAT = "https://fhir.nhs.uk/CareConnect-TreatmentCategory-1";
    public static final String CS_CC_HUMAN_LANG = "https://fhir.nhs.uk/CareConnect-HumanLanguage-1";
    public static final String CS_CC_LANG_ABILITY_MODE = "https://fhir.nhs.uk/CareConnect-LanguageAbilityMode-1";
    public static final String CS_CC_LANG_ABILITY_PROFI = "https://fhir.nhs.uk/CareConnect-LanguageAbilityProficiency-1";
    public static final String CS_CC_NHS_NUMBER_VERIF = "https://fhir.nhs.uk/CareConnect-NHSNumberVerificationStatus-1";
    public static final String CS_CC_NHS_NUMBER_VERIF_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-NHSNumberVerificationStatus-1";
    public static final String CS_CC_ETHNIC_CATEGORY_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-EthnicCategory-1";
    public static final String CS_CC_RESIDENTIAL_STATUS_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-ResidentialStatus-1";
    public static final String CS_CC_TREATMENT_CAT_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-TreatmentCategory-1";
    public static final String CS_CC_HUMAN_LANG_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-HumanLanguage-1";
    public static final String CS_CC_LANG_ABILITY_MODE_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-LanguageAbilityMode-1";
    public static final String CS_CC_LANG_ABILITY_PROFI_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-LanguageAbilityProficiency-1";
    public static final String CS_CC_PRESCRIPTION_TYPE_STU3 = "https://fhir.nhs.uk/STU3/CodeSystem/CareConnect-PrescriptionType-1";

    public static final String CS_MARITAL_STATUS = "http://hl7.org/fhir/v3/MaritalStatus";
    public static final String CS_NULL_FLAVOUR = "http://hl7.org/fhir/v3/NullFlavor";
    public static final String CS_LIST_ORDER = "http://hl7.org/fhir/codesystem-list-order.html";

    //Appointment Reason URL
    public static final String DEFAULTREASONURL = "DefaultReasonURL";
    
    //CodeSystem URLs
    public static final String VS_SNOWMED = "http://snomed.info/sct";
    public static final String CS_UNITS_OF_MEASURE = "http://unitsofmeasure.org";

    //Include Allergies Constant
    public static final String INCLUDE_ALLERGIES = "includeAllergies";

}
