package uk.gov.hscic;

public final class SystemURL {

    private SystemURL() { }

    // Base Constants
    public static final String AUTHORIZATION_TOKEN = "https://authorize.fhir.nhs.net/token";
    public static final String SDS_USER_ID = "http://fhir.nhs.net/sds-user-id";

    // HL7 Constants
    public static final String HL7_BASIC_RESOURCE_TYPE = "http://hl7.org/fhir/basic-resource-type";
    public static final String HL7_VS_C80_PRACTICE_CODES = "http://hl7.org/fhir/ValueSet/c80-practice-codes";

    // FHIR ID Constants
    public static final String ID_GPC_APPOINTMENT_IDENTIFIER = "https://fhir.nhs.uk/Id/gpconnect-appointment-identifier";
    public static final String ID_GPC_SCHEDULE_IDENTIFIER = "https://fhir.nhs.uk/Id/gpconnect-schedule-identifier";
    public static final String ID_NHS_NUMBER = "https://fhir.nhs.uk/Id/nhs-number";
    public static final String ID_ODS_ORGANIZATION_CODE = "https://fhir.nhs.uk/Id/ods-organization-code";
    public static final String ID_ODS_SITE_CODE = "https://fhir.nhs.uk/Id/ods-site-code";
    public static final String ID_LOCAL_LOCATION_IDENTIFIER = "https://fhir.nhs.uk/Id/local-location-identifier";
    public static final String ID_LOCAL_PATIENT_IDENTIFIER = "https://fhir.nhs.uk/Id/local-patient-identifier";
    public static final String ID_SDS_ROLE_PROFILE_ID = "https://fhir.nhs.uk/Id/sds-role-profile-id";
    public static final String ID_SDS_USER_ID = "https://fhir.nhs.uk/Id/sds-user-id";
    public static final String ID_MARITAL_STATUS = "http://fhir.nhs.net/ValueSet/marital-status-1";


    // FHIR StructureDefinition Constants
    public static final String SD_GPC_CARERECORD_COMPOSITION = "http://fhir.nhs.net/StructureDefinition/gpconnect-carerecord-composition-1";
    public static final String SD_GPC_OPERATIONOUTCOME = "http://fhir.nhs.net/StructureDefinition/gpconnect-operationoutcome-1";
    public static final String SD_GPC_ORGANIZATION = "http://fhir.nhs.net/StructureDefinition/CareConnect-GPC-Organization-1";
    public static final String SD_GPC_PATIENT = "https://fhir.nhs.uk/StructureDefinition/CareConnect-GPC-Patient-1";
    public static final String SD_GPC_PRACTITIONER = "http://fhir.nhs.net/StructureDefinition/CareConnect-GPC-Practitioner-1";
    public static final String SD_GPC_APPOINTMENT = "http://fhir.nhs.net/StructureDefinition/gpconnect-appointment-1";
    public static final String SD_GPC_GET_SCHEDULE_BUNDLE = "http://fhir.nhs.net/StructureDefinition/gpconnect-getschedule-bundle-1";
    public static final String SD_GPC_LOCATION = "http://fhir.nhs.net/StructureDefinition/CareConnect-GPC-Location-1";
    public static final String SD_GPC_SLOT = "http://fhir.nhs.net/StructureDefinition/gpconnect-slot-1";
    public static final String SD_GPC_SCHEDULE = "http://fhir.nhs.net/StructureDefinition/gpconnect-schedule-1";
    public static final String SD_GPC_SRCHSET_BUNDLE = "http://fhir.nhs.net/StructureDefinition/gpconnect-searchset-bundle-1";
    
    // FHIR StructureDefinition Extension
    public static final String SD_CC_EXT_NHS_NUMBER_VERIF = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-NHSNumberVerificationStatus-1";
    public static final String SD_EXTENSION_GPC_APPOINTMENT_CANCELLATION_REASON = "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1";
    public static final String SD_EXTENSION_GPC_PRACTITIONER = "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-practitioner-1";
    public static final String SD_EXTENSION_MEDICATION_QUANTITY_TEXT = "http://fhir.nhs.net/StructureDefinition/extension-medication-quantity-text-1-0";
    public static final String SD_EXTENSION_PERSCRIPTION_REPEAT_REVIEW_DATE = "http://fhir.nhs.net/StructureDefinition/extension-prescription-repeat-review-date-1-0";
    public static final String SD_EXTENSION_REGISTRATION_PERIOD = "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1";
    public static final String SD_EXTENSION_REGISTRATION_STATUS = "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1";
    public static final String SD_EXTENSION_REGISTRATION_TYPE = "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1";
    public static final String SD_CC_EXT_REGISTRATION_PERIOD = "registrationPeriod";
    public static final String SD_CC_EXT_REGISTRATION_STATUS = "registrationStatus";
    public static final String SD_CC_EXT_REGISTRATION_TYPE = "registrationType";
    public static final String SD_CC_EXT_ETHNIC_CATEGORY = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-EthnicCategory-1";
    public static final String SD_EXTENSION_CC_MAIN_LOCATION = "https://fhir.hl7.org.uk/StructureDefinition/Extension-CareConnect-GPC-MainLocation-1";
    public static final String SD_EXTENSION_CC_REG_DETAILS = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-RegistrationDetails-1";
    public static final String SD_EXTENSION_CC_ORG_PERIOD = "http://hl7.org/fhir/StructureDefinition/organization-period";
    public static final String SD_CC_EXT_RELIGIOUS_AFFILI = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-ReligiousAffiliation-1";   
    public static final String SD_PATIENT_CADAVERIC_DON = "http://hl7.org/fhir/StructureDefinition/patient-cadavericDonor";   
    public static final String SD_CC_EXT_RESIDENTIAL_STATUS = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-ResidentialStatus-1";   
    public static final String SD_CC_EXT_TREATMENT_CAT = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-TreatmentCategory-1";   
    public static final String SD_CC_EXT_NHS_COMMUNICATION = "https://fhir.nhs.uk/StructureDefinition/Extension-CareConnect-GPC-NHSCommunication-1";   
    public static final String SD_CC_EXT_COMM_LANGUAGE = "Language";   
    public static final String SD_CC_COMM_PREFERRED = "Preferred";   
    public static final String SD_CC_MODE_OF_COMM = "modeOfCommunication";   
    public static final String SD_CC_COMM_PROFICIENCY = "communicationProficiency";   
    public static final String SD_CC_INTERPRETER_REQUIRED = "interpreterRequired";   
    

    // FHIR ValueSet Constants
    public static final String VS_GPC_ERROR_WARNING_CODE = "http://fhir.nhs.net/ValueSet/gpconnect-error-or-warning-code-1";
    public static final String VS_GPC_REASON_TYPE = "http://fhir.nhs.net/ValueSet/gpconnect-reason-type-1-0";
    public static final String VS_GPC_RECORD_SECTION = "http://fhir.nhs.net/ValueSet/gpconnect-record-section-1";
    public static final String VS_HUMAN_LANGUAGE = "http://fhir.nhs.net/ValueSet/human-language-1";
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
    
    // FHIR CodeSystem Constants
    public static final String CS_REGISTRATION_STATUS = "https://fhir.nhs.uk/CareConnect-RegistrationStatus-1";
    public static final String CS_REGISTRATION_TYPE = "https://fhir.nhs.uk/CareConnect-RegistrationType-1";
    public static final String CS_CC_ETHNIC_CATEGORY = "https://fhir.nhs.uk/CareConnect-EthnicCategory-1";
    public static final String CS_CC_RELIGIOUS_AFFILI = "https://fhir.nhs.uk/CareConnect-ReligiousAffiliation-1";
    public static final String CS_CC_RESIDENTIAL_STATUS = "https://fhir.nhs.uk/CareConnect-ResidentialStatus-1";
    public static final String CS_CC_TREATMENT_CAT = "https://fhir.nhs.uk/CareConnect-TreatmentCategory-1";
    public static final String CS_CC_HUMAN_LANG = "https://fhir.nhs.uk/CareConnect-HumanLanguage-1";
    public static final String CS_CC_LANG_ABILITY_MODE = "https://fhir.nhs.uk/CareConnect-LanguageAbilityMode-1";
    public static final String CS_CC_LANG_ABILITY_PROFI = "https://fhir.nhs.uk/CareConnect-LanguageAbilityProficiency-1";
    public static final String CS_CC_NHS_NUMBER_VERIF = "https://fhir.nhs.uk/CareConnect-NHSNumberVerificationStatus-1";
    
    //Appointment Reason URL
    public static final String DEFAULTREASONURL = "DefaultReasonURL";
    
}
