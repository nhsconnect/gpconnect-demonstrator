USE gpconnect;

/* Destroy all existing data */
DROP TABLE IF EXISTS gpconnect.appointment_slots_organizations;
DROP TABLE IF EXISTS gpconnect.appointment_slots_orgType;
DROP TABLE IF EXISTS gpconnect.appointment_appointments_slots;
DROP TABLE IF EXISTS gpconnect.appointment_schedules;
DROP TABLE IF EXISTS gpconnect.appointment_slots;
DROP TABLE IF EXISTS gpconnect.medication_allergies;
DROP TABLE IF EXISTS gpconnect.medication_statement_reason_codes;
DROP TABLE IF EXISTS gpconnect.medication_statement_reason_references;
DROP TABLE IF EXISTS gpconnect.medication_statement_notes;
DROP TABLE IF EXISTS gpconnect.medication_request_reason_codes;
DROP TABLE IF EXISTS gpconnect.medication_request_reason_references;
DROP TABLE IF EXISTS gpconnect.medication_request_notes;
DROP TABLE IF EXISTS gpconnect.medication_request_based_on_references;
DROP TABLE IF EXISTS gpconnect.medication_reason_codes;
DROP TABLE IF EXISTS gpconnect.medication_reason_references;
DROP TABLE IF EXISTS gpconnect.medication_notes;
DROP TABLE IF EXISTS gpconnect.medication_request_based_on;
DROP TABLE IF EXISTS gpconnect.medication_statements;
DROP TABLE IF EXISTS gpconnect.medication_requests;
DROP TABLE IF EXISTS gpconnect.patients;
DROP TABLE IF EXISTS gpconnect.medications;
DROP TABLE IF EXISTS gpconnect.organizations;
DROP TABLE IF EXISTS gpconnect.patients;
DROP TABLE IF EXISTS gpconnect.practitioners;
DROP TABLE IF EXISTS gpconnect.locations;
DROP TABLE IF EXISTS gpconnect.appointment_booking_orgz;
DROP TABLE IF EXISTS gpconnect.appointment_appointments;
DROP TABLE IF EXISTS gpconnect.medical_departments;
DROP TABLE IF EXISTS gpconnect.allergyintolerance;
DROP TABLE IF EXISTS gpconnect.addresses;
DROP TABLE IF EXISTS gpconnect.translations;

/* Create new table schemas */

CREATE TABLE gpconnect.appointment_booking_orgz (
  id              BIGINT       NOT NULL,
  org_code        VARCHAR(30)  NULL,
  name            VARCHAR(100) NULL,
  telephone       VARCHAR(100) NULL,
  lastUpdated     DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.appointment_appointments (
  id                 BIGINT    NOT NULL AUTO_INCREMENT,
  cancellationReason TEXT(300) NULL,
  status             TEXT(50)  NULL,
  typeCode           BIGINT    NULL,
  typeDisplay        TEXT(100) NULL,
  typeText           TEXT(100) NULL,
  description        TEXT(300) NULL,
  startDateTime      DATETIME  NULL,
  endDateTime        DATETIME  NULL,
  commentText        TEXT(300) NULL,
  patientId          BIGINT    NULL NOT NULL,
  practitionerId     BIGINT    NULL,
  locationId         BIGINT    NULL NOT NULL,
  minutesDuration    BIGINT    NULL,
  created            DATETIME  NULL,
  priority    		   BIGINT    NULL,
  lastUpdated        DATETIME  NULL,
  PRIMARY KEY (id)
);

ALTER TABLE gpconnect.appointment_booking_orgz
ADD CONSTRAINT fk_appointment_appointments_booking_orgz
FOREIGN KEY (id) REFERENCES gpconnect.appointment_appointments(id);

CREATE TABLE gpconnect.appointment_schedules (
  id              BIGINT    NOT NULL AUTO_INCREMENT,
  practitionerId  BIGINT    NULL,
  identifier      TEXT(50)  NULL,
  typeCode        TEXT(50)  NULL,
  typeDescription TEXT(250) NULL,
  locationId      BIGINT    NULL,
  practitionerRoleCode	TEXT(50)  NULL,
  practitionerRoleDisplay  TEXT(50)  NULL,
  startDateTime   DATETIME  NULL,
  endDateTime     DATETIME  NULL,
  scheduleComment TEXT(300) NULL,
  lastUpdated     DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.appointment_slots (
  id                BIGINT    NOT NULL AUTO_INCREMENT,
  typeCode          BIGINT    NULL,
  typeDisplay       TEXT(300) NULL,
  scheduleReference BIGINT    NULL,
  freeBusyType      TEXT(50)  NULL,
  startDateTime     DATETIME  NULL,
  endDateTime       DATETIME  NULL,
  lastUpdated       DATETIME  NULL,
  gpConnectBookable BOOLEAN   NULL,
  deliveryChannelCodes TEXT(50)   NULL,
  PRIMARY KEY (id)
);

CREATE TABLE `appointment_slots_orgType` (
  `slotId` bigint(20) NOT NULL,
  `bookableOrgTypes` varchar(255) NOT NULL,
  KEY `slotId` (`slotId`),
  CONSTRAINT `appointment_slots_orgType_ibfk_1` FOREIGN KEY (`slotId`) REFERENCES `appointment_slots` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE appointment_appointments_slots (
	appointmentId	BIGINT    NOT NULL,
	slotId			BIGINT    NOT NULL,
	FOREIGN KEY (appointmentId) REFERENCES gpconnect.appointment_appointments(id),
	FOREIGN KEY (slotId) 		REFERENCES gpconnect.appointment_slots(id)
);

CREATE TABLE gpconnect.practitioners (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  userid            VARCHAR(30)  NULL,
  p_role_ids        VARCHAR(30)  NOT NULL,
  p_name_family     VARCHAR(100) NULL,
  p_name_given      VARCHAR(100) NULL,
  p_name_prefix     VARCHAR(20)  NULL,
  p_gender          VARCHAR(10)  NULL,
  p_organization_id BIGINT       NULL,
  p_role_code       VARCHAR(30)  NULL,
  p_role_display    VARCHAR(100) NULL,
  p_com_code        VARCHAR(30)  NULL,
  p_com_display     VARCHAR(100) NULL,
  lastUpdated       DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.organizations (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  org_code    VARCHAR(30)  NULL,
  org_name    VARCHAR(100) NULL,
  lastUpdated DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE appointment_slots_organizations (
	slotId       	BIGINT    NOT NULL,
	organizationId	BIGINT    NOT NULL,
	FOREIGN KEY (organizationId) REFERENCES gpconnect.organizations(id),
	FOREIGN KEY (slotId) 		REFERENCES gpconnect.appointment_slots(id)
);

CREATE TABLE gpconnect.medical_departments (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  department  VARCHAR(150) NULL,
  lastUpdated DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.patients (
  id                  BIGINT       NOT NULL AUTO_INCREMENT,
  title               VARCHAR(10)  NULL,
  first_name          VARCHAR(300) NULL,
  last_name           VARCHAR(300) NULL,
  address_1           VARCHAR(100) NULL,
  address_2           VARCHAR(100) NULL,
  address_3           VARCHAR(100) NULL,
  address_4           VARCHAR(100) NULL,
  address_5           VARCHAR(100) NULL,
  postcode            VARCHAR(10)  NULL,
  phone               VARCHAR(20)  NULL,
  date_of_birth       DATE         NULL,
  gender              VARCHAR(10)  NULL,
  nhs_number          VARCHAR(20)  NULL,
  pas_number          VARCHAR(20)  NULL,
  department_id       BIGINT       NULL,
  gp_id               BIGINT       NULL,
  lastUpdated         DATETIME     NULL,
  registration_start  DATETIME     NULL,
  registration_end    DATETIME     NULL,
  registration_status VARCHAR(10)  NULL,
  registration_type   VARCHAR(10)  NULL,
  sensitive_flag      BOOLEAN      NULL,
  multiple_birth      BOOLEAN      NULL,
  deceased			  DATETIME	   NULL,
  marital_status      VARCHAR(10)  NULL,
  managing_organization BIGINT     NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (department_id) REFERENCES gpconnect.medical_departments(id),
  FOREIGN KEY (gp_id) REFERENCES gpconnect.practitioners(id)
);

CREATE TABLE gpconnect.medications (
  id                        BIGINT    NOT NULL AUTO_INCREMENT,
  concept_code              TEXT(20)  NULL,
  concept_display	        TEXT(100) NULL,
  desc_code                 TEXT(20)  NULL,
  desc_display	            TEXT(100) NULL,
  code_translation_ref		TEXT(10)  NULL,
  text		                TEXT(100) NULL, 
  batchNumber               TEXT(50)  NULL, 
  expiryDate                DATETIME  NULL,
  lastUpdated               DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_statements (
  id	              BIGINT       NOT NULL AUTO_INCREMENT,
  lastIssueDate       DATETIME     NULL,
  medicationRequestId VARCHAR(50)       NULL,
  statusCode          VARCHAR(50)  NULL,
  statusDisplay       VARCHAR(50)  NULL,
  medicationId        BIGINT       NULL,
  startDate           DATETIME     NULL,
  endDate             DATETIME     NULL,
  dateAsserted        DATETIME     NULL,
  patientId           BIGINT       NULL, 
  takenCode           VARCHAR(50)  NULL,
  takenDisplay        VARCHAR(50)  NULL,
  dosageText          VARCHAR(250) NULL,
  dosageInstruction   VARCHAR(250)  NULL,
  lastUpdated         DATETIME     NULL,
  prescribingAgency  VARCHAR(250) NULL,
  guid               VARCHAR(250) NULL,
  warningCode         VARCHAR(250)  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_requests (
  id	                             BIGINT       NOT NULL AUTO_INCREMENT,
  groupIdentifier                    VARCHAR(250) NULL,
  statusCode                         VARCHAR(50)  NULL,
  statusDisplay                      VARCHAR(50)  NULL,
  intentCode                         VARCHAR(50)  NULL,
  intentDisplay                      VARCHAR(50)  NULL, 
  medicationId                       BIGINT       NULL, 
  patientId                          BIGINT       NULL, 
  authoredOn                         DATETIME     NULL,
  requesterUrl                       TEXT(100)    NULL,
  requesterId                        BIGINT       NULL,
  authorisingPractitionerId          BIGINT       NULL,
  dosageText                         VARCHAR(250) NULL,
  dosageInstruction                  VARCHAR(250)  NULL,
  dispenseRequestStartDate           DATETIME     NULL,
  dispenseRequestEndDate             DATETIME     NULL,
  dispenseQuantityValue              VARCHAR(20)  NULL,
  dispenseQuantityUnit               VARCHAR(20)  NULL,
  dispenseQuantityText               VARCHAR(100) NULL,
  expectedSupplyDuration             VARCHAR(20)  NULL,
  dispenseRequestOrganizationId      BIGINT       NULL,
  priorMedicationRequestId           BIGINT       NULL,
  numberOfRepeatPrescriptionsAllowed INT          NULL, 
  numberOfRepeatPrescriptionsIssued  INT          NULL,
  authorisationExpiryDate            DATETIME     NULL,
  prescriptionTypeCode               VARCHAR(20)  NULL,
  prescriptionTypeDisplay            VARCHAR(20)  NULL,
  statusReasonDate                   DATETIME     NULL,
  statusReason                       VARCHAR(50)  NULL,
  lastUpdated                        DATETIME     NULL,
  guid                               VARCHAR(250) NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (medicationId) REFERENCES gpconnect.medications(id)
);

CREATE TABLE gpconnect.medication_reason_references (
  id           BIGINT NOT NULL AUTO_INCREMENT,
  referenceUrl VARCHAR(100) NULL,
  referenceId  BIGINT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_request_based_on (
  id           BIGINT NOT NULL AUTO_INCREMENT,
  referenceUrl VARCHAR(100) NULL,
  referenceId  BIGINT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_notes (
  id                 BIGINT       NOT NULL AUTO_INCREMENT,
  dateWritten        DATETIME     NULL,
  authorReferenceUrl VARCHAR(100) NULL,
  authorId           BIGINT       NULL,
  noteText           TEXT(300)    NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_reason_codes (
  id            BIGINT      NOT NULL AUTO_INCREMENT,
  reasonCode    VARCHAR(50) NULL,
  reasonDisplay VARCHAR(150) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_statement_reason_codes (
  medicationStatementId	BIGINT NOT NULL,
  reasonCodeId			BIGINT NOT NULL,
  FOREIGN KEY (medicationStatementId) REFERENCES gpconnect.medication_statements(id),
  FOREIGN KEY (reasonCodeId) 		  REFERENCES gpconnect.medication_reason_codes(id)
);

CREATE TABLE gpconnect.medication_statement_reason_references (
  medicationStatementId	BIGINT NOT NULL,
  reasonReferenceId		BIGINT NOT NULL,
  FOREIGN KEY (medicationStatementId) REFERENCES gpconnect.medication_statements(id),
  FOREIGN KEY (reasonReferenceId) 	  REFERENCES gpconnect.medication_reason_references(id)
);

CREATE TABLE gpconnect.medication_statement_notes (
  medicationStatementId	BIGINT NOT NULL,
  noteId		    	BIGINT NOT NULL,
  FOREIGN KEY (medicationStatementId) REFERENCES gpconnect.medication_statements(id),
  FOREIGN KEY (noteId) 		          REFERENCES gpconnect.medication_notes(id)
);

CREATE TABLE gpconnect.medication_request_reason_codes (
  medicationRequestId	BIGINT NOT NULL,
  reasonCodeId			BIGINT NOT NULL,
  FOREIGN KEY (medicationRequestId) REFERENCES gpconnect.medication_requests(id),
  FOREIGN KEY (reasonCodeId) 	   	  REFERENCES gpconnect.medication_reason_codes(id)
);

CREATE TABLE gpconnect.medication_request_reason_references (
  medicationRequestId	BIGINT NOT NULL,
  reasonReferenceId		BIGINT NOT NULL,
  FOREIGN KEY (medicationRequestId) REFERENCES gpconnect.medication_requests(id),
  FOREIGN KEY (reasonReferenceId)  	  REFERENCES gpconnect.medication_reason_references(id)
);

CREATE TABLE gpconnect.medication_request_based_on_references (
  medicationRequestId	BIGINT NOT NULL,
  basedOnReferenceId	BIGINT NOT NULL,
  FOREIGN KEY (medicationRequestId) REFERENCES gpconnect.medication_requests(id),
  FOREIGN KEY (basedOnReferenceId)  	  REFERENCES gpconnect.medication_request_based_on(id)
);

CREATE TABLE gpconnect.medication_request_notes (
  medicationRequestId BIGINT NOT NULL,
  noteId			  BIGINT NOT NULL,
  FOREIGN KEY (medicationRequestId) REFERENCES gpconnect.medication_requests(id),
  FOREIGN KEY (noteId) 		        REFERENCES gpconnect.medication_notes(id)
);

CREATE TABLE gpconnect.addresses (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  line   	  VARCHAR(100)NULL,
  city        VARCHAR(50) NULL,
  district    VARCHAR(50) NULL,
  state       VARCHAR(50) NULL,
  postalCode  VARCHAR(10) NULL,
  country     VARCHAR(50) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.locations (
  id                 BIGINT       NOT NULL AUTO_INCREMENT,
  name               VARCHAR(250) NOT NULL,
  org_ods_code       VARCHAR(250) NOT NULL,
  org_ods_code_name  VARCHAR(250) NOT NULL,
  site_ods_code      VARCHAR(250) NOT NULL,
  site_ods_code_name VARCHAR(250) NOT NULL,
  status             VARCHAR(100) NULL,
  lastUpdated        DATETIME     NULL,
  address_id         BIGINT       NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (address_id) REFERENCES gpconnect.addresses(id)
);

CREATE TABLE gpconnect.allergyintolerance (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber         BIGINT       NULL,
  endDate  		  DATETIME  NULL,
  endReason			VARCHAR(250) NULL,
  note				VARCHAR(250) NULL,
  reactionDescription VARCHAR(250) NULL,
  clinicalStatus VARCHAR(250) NULL,
  verificationStatus VARCHAR(250) NULL,
  category VARCHAR(250) NULL,
  patientRef VARCHAR(250) NULL,
  onSetDateTime DATETIME  NULL,
  assertedDate DATETIME  NULL,
  concept_code              VARCHAR(20)  NULL,
  concept_display	        VARCHAR(100) NULL,
  desc_code                 VARCHAR(20)  NULL,
  desc_display	            VARCHAR(100) NULL,
  code_translation_ref		VARCHAR(10)  NULL,
  manCoding VARCHAR(250) NULL,
  manDisplay VARCHAR(250) NULL,
  manDescCoding VARCHAR(250) NULL,
  manDescDisplay VARCHAR(250) NULL,
  man_translation_ref		VARCHAR(10)  NULL,
  recorder VARCHAR(250) NOT NULL,
  guid      VARCHAR(250) NULL,
  warningCode VARCHAR(250) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE gpconnect.medication_allergies (
  medicationId BIGINT NOT NULL,
  allergyintoleranceId BIGINT NOT NULL,
  patientNhsnumber BIGINT NOT NULL,
  FOREIGN KEY (medicationId) REFERENCES gpconnect.medications(id),
  FOREIGN KEY (allergyintoleranceId) REFERENCES gpconnect.allergyintolerance(id)
  );

CREATE TABLE gpconnect.translations (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  system   	  VARCHAR(100)NULL,
  code        VARCHAR(20) NULL,
  display     VARCHAR(250) NULL,
  PRIMARY KEY (id)
);
