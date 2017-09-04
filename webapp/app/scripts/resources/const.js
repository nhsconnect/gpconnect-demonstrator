'use strict';

angular.module('gpConnect')
  .value("gpcConst", {
    "ID_NHS_NUMBER": "https://fhir.nhs.uk/Id/nhs-number",
    "ID_ODS_ORGANIZATION_CODE": "https://fhir.nhs.uk/Id/ods-organization-code",
    "ID_SDS_USER_ID": "http://fhir.nhs.net/sds-user-id",

    "SD_EXT_GPC_APPOINT_CANC_REAS": "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1",
    "SD_EXT_REG_PERIOD": "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1",
    "SD_EXT_REG_STATUS": "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1",
    "SD_EXT_REG_TYPE": "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1",
    
    "VS_SDS_JOB_ROLE_NAME": "http://fhir.nhs.net/ValueSet/sds-job-role-name-1",
    "VS_GPC_RECORD_SECTION": "http://fhir.nhs.net/ValueSet/gpconnect-record-section-1",
    "VS_C80_PRACTICE_CODES": "http://hl7.org/fhir/ValueSet/c80-practice-codes",
    "VS_SNOMED_SCT": "http://snomed.info/sct",
    "VS_REG_STATUS": "http://fhir.nhs.net/ValueSet/registration-status-1",
    "VS_REG_TYPE": "http://fhir.nhs.net/ValueSet/registration-type-1",
    "VS_BASIC_RESOURCE_TYPE": "http://hl7.org/fhir/basic-resource-type"
  });
