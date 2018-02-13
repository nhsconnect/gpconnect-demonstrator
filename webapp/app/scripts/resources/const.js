'use strict';

angular.module('gpConnect')
  .value("gpcConst", {
    "ID_NHS_NUMBER": "https://fhir.nhs.uk/Id/nhs-number",
    "ID_ODS_ORGANIZATION_CODE": "https://fhir.nhs.uk/Id/ods-organization-code",
    "ID_SDS_USER_ID": "https://fhir.nhs.uk/Id/sds-user-id",

    "SD_CC_PATIENT": "https://fhir.nhs.uk/StructureDefinition/CareConnect-GPC-Patient-1",
    "SD_EXT_GPC_APPOINT_CANC_REAS": "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-GPConnect-AppointmentCancellationReason-1",
    "SD_EXT_REG_PERIOD": "https://fhir.nhs.uk/StructureDefinition/extension-registration-period-1",
    "SD_EXT_REG_STATUS": "https://fhir.nhs.uk/StructureDefinition/extension-registration-status-1",
    "SD_EXT_REG_TYPE": "https://fhir.nhs.uk/StructureDefinition/extension-registration-type-1",
    
    "VS_SDS_JOB_ROLE_NAME": "https://fhir.nhs.uk/Id/sds-user-id",
    "VS_GPC_RECORD_SECTION": "http://fhir.nhs.net/ValueSet/gpconnect-record-section-1",
    "VS_C80_PRACTICE_CODES": "http://hl7.org/fhir/ValueSet/c80-practice-codes",
    "VS_SNOMED_SCT": "http://snomed.info/sct",
    "VS_REG_STATUS": "https://fhir.nhs.uk/ValueSet/registration-status-1",
    "VS_REG_TYPE": "https://fhir.nhs.uk/ValueSet/registration-type-1",
    "VS_BASIC_RESOURCE_TYPE": "http://hl7.org/fhir/basic-resource-type"
  });
