USE gpconnect0_7_3;

/* Destroy all existing data */
DROP TABLE IF EXISTS appointment_schedules;
DROP TABLE IF EXISTS appointment_slots;
DROP TABLE IF EXISTS practitioners;
DROP TABLE IF EXISTS organizations;
DROP TABLE IF EXISTS patients;
DROP TABLE IF EXISTS allergies;
DROP TABLE IF EXISTS medications_html;
DROP TABLE IF EXISTS medications;
DROP TABLE IF EXISTS medication_orders;
DROP TABLE IF EXISTS medication_dispenses;
DROP TABLE IF EXISTS medication_administrations;
DROP TABLE IF EXISTS problems;
DROP TABLE IF EXISTS referrals;
DROP TABLE IF EXISTS encounters;
DROP TABLE IF EXISTS patientsummary;
DROP TABLE IF EXISTS procedures;
DROP TABLE IF EXISTS observations;
DROP TABLE IF EXISTS immunisations;
DROP TABLE IF EXISTS adminitems;
DROP TABLE IF EXISTS clinicalitems;
DROP TABLE IF EXISTS investigations;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS appointment_appointments;
DROP TABLE IF EXISTS general_practitioners;
DROP TABLE IF EXISTS medical_departments;
DROP TABLE IF EXISTS emergencycodes;

/* Create new table schemas */

CREATE TABLE appointment_appointments (
  id                 BIGINT    NOT NULL AUTO_INCREMENT,
  cancellationReason TEXT(300) NULL,
  status             TEXT(50)  NULL,
  typeCode           BIGINT    NULL,
  typeDisplay        TEXT(100) NULL,
  reasonCode         BIGINT    NULL,
  reasonDisplay      TEXT(100) NULL,
  startDateTime      DATETIME  NULL,
  endDateTime        DATETIME  NULL,
  commentText        TEXT(300) NULL,
  patientId          BIGINT    NULL,
  practitionerId     BIGINT    NULL,
  locationId         BIGINT    NULL,
  lastUpdated        DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE appointment_schedules (
  id              BIGINT    NOT NULL AUTO_INCREMENT,
  practitionerId  BIGINT    NULL,
  identifier      TEXT(50)  NULL,
  typeCode        TEXT(50)  NULL,
  typeDescription TEXT(250) NULL,
  locationId      BIGINT    NULL,
  startDateTime   DATETIME  NULL,
  endDateTime     DATETIME  NULL,
  scheduleComment TEXT(300) NULL,
  lastUpdated     DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE appointment_slots (
  id                BIGINT    NOT NULL AUTO_INCREMENT,
  appointmentId     BIGINT    NULL,
  typeCode          BIGINT    NULL,
  typeDisplay       TEXT(300) NULL,
  scheduleReference BIGINT    NULL,
  freeBusyType      TEXT(50)  NULL,
  startDateTime     DATETIME  NULL,
  endDateTime       DATETIME  NULL,
  lastUpdated       DATETIME  NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (appointmentId) REFERENCES appointment_appointments(id)
);

CREATE TABLE general_practitioners (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  gp_name     VARCHAR(150) NULL,
  address_1   VARCHAR(100) NULL,
  address_2   VARCHAR(100) NULL,
  address_3   VARCHAR(100) NULL,
  address_4   VARCHAR(100) NULL,
  address_5   VARCHAR(100) NULL,
  postcode    VARCHAR(10)  NULL,
  lastUpdated DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE practitioners (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  userid            VARCHAR(30)  NULL,
  p_role_id         VARCHAR(30)  NULL,
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

CREATE TABLE organizations (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  org_code    VARCHAR(30)  NULL,
  site_code   VARCHAR(30)  NULL,
  org_name    VARCHAR(100) NULL,
  lastUpdated DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE medical_departments (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  department  VARCHAR(150) NULL,
  lastUpdated DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE patients (
  id                  BIGINT       NOT NULL AUTO_INCREMENT,
  title               VARCHAR(10)  NULL,
  first_name          VARCHAR(30)  NULL,
  last_name           VARCHAR(30)  NULL,
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
  PRIMARY KEY (id),
  FOREIGN KEY (department_id) REFERENCES medical_departments(id),
  FOREIGN KEY (gp_id) REFERENCES general_practitioners(id)
);

CREATE TABLE allergies (
  id                BIGINT        NOT NULL AUTO_INCREMENT,
  nhsNumber         BIGINT        NULL,
  currentOrHistoric VARCHAR(4096) NULL,
  startDate         DATETIME      NULL,
  endDate           DATETIME      NULL,
  details           VARCHAR(4096) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE medications_html (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber         VARCHAR(100) NULL,
  currentRepeatPast VARCHAR(100) NOT NULL,
  startDate         DATETIME NULL,
  medicationItem    VARCHAR(100) NULL,
  dosageInstruction VARCHAR(100) NULL,
  quantity          VARCHAR(100) NULL,
  scheduledEnd      DATETIME NULL,
  daysDuration      BIGINT NULL,
  details           VARCHAR(100) NULL,
  lastIssued        DATETIME NULL,
  reviewDate        DATETIME NULL,
  numberIssued      BIGINT NULL,
  maxIssues         BIGINT NULL,
  typeMed           VARCHAR(100) NULL,
  discontinuationReason           VARCHAR(100) NULL,
  discontinuationDate      DATETIME NULL,
  PRIMARY KEY (id)
);

CREATE TABLE medications (
  id          BIGINT    NOT NULL AUTO_INCREMENT,
  name        TEXT(100) NULL,
  lastUpdated DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE medication_orders (
  id                       BIGINT    NOT NULL AUTO_INCREMENT,
  date_written             DATETIME  NULL,
  order_status             TEXT(50)  NULL,
  patient_id               BIGINT    NULL,
  author_id                BIGINT    NULL,
  medication_id            BIGINT    NULL,
  dosage_text              TEXT(300) NULL,
  dispense_quantity_text   TEXT(300) NULL,
  dispense_review_date     DATETIME  NULL,
  dispense_medication_id   BIGINT    NULL,
  dispense_repeats_allowed INT       NULL,
  lastUpdated              DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE medication_dispenses (
  id                BIGINT    NOT NULL AUTO_INCREMENT,
  status            TEXT(50)  NULL,
  patientId         BIGINT    NULL,
  medicationOrderId BIGINT    NULL,
  medicationId      BIGINT    NULL,
  medicationName    TEXT(300) NULL,
  dosageText        TEXT(300) NULL,
  lastUpdated       DATETIME  NULL,
  PRIMARY KEY (id)
);

CREATE TABLE medication_administrations (
  id                 BIGINT   NOT NULL AUTO_INCREMENT,
  patientId          BIGINT   NULL,
  practitionerId     BIGINT   NULL,
  encounterId        BIGINT   NULL,
  prescriptionId     BIGINT   NULL,
  administrationDate DATETIME NULL,
  medicationId       BIGINT   NULL,
  lastUpdated        DATETIME NULL,
  PRIMARY KEY (id)
);

CREATE TABLE problems (
  id               BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber        BIGINT       NULL,
  activeOrInactive VARCHAR(100) NULL,
  startDate        DATETIME     NULL,
  endDate          DATETIME     NULL,
  entry            VARCHAR(200) NULL,
  significance     VARCHAR(200) NULL,
  details          VARCHAR(200) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE referrals (
  id            BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber     BIGINT       NULL,
  sectionDate   DATETIME     NULL,
  referral_from VARCHAR(200) NULL,
  referral_to   VARCHAR(200) NULL,
  priority      VARCHAR(200) NULL,
  details       VARCHAR(200) NULL,
  lastUpdated   DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE encounters (
  id            BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber     VARCHAR(100) NULL,
  sectionDate   DATETIME     NULL,
  encounterDate DATETIME     NULL,
  title         VARCHAR(200) NULL,
  details       VARCHAR(400) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE procedures (
  id          BIGINT        NOT NULL AUTO_INCREMENT,
  html        VARCHAR(4096) NULL,
  provider    VARCHAR(10)   NULL,
  lastUpdated DATETIME      NULL,
  PRIMARY KEY (id)
);

CREATE TABLE observations (
  id              BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber       BIGINT       NULL,
  observationDate DATETIME NULL,
  entry           VARCHAR(100) NULL,
  value           VARCHAR(100) NULL,
  theRange           VARCHAR(100) NULL,
  details         VARCHAR(100) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE immunisations (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber   BIGINT       NULL,
  dateOfVac   DATETIME     NULL,
  vaccination VARCHAR(100) NULL,
  part        BIGINT NULL,
  contents    VARCHAR(200) NULL,
  details     VARCHAR(200) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE adminitems (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber   BIGINT       NULL,
  sectionDate DATETIME     NULL,
  adminDate   VARCHAR(100) NULL,
  entry       VARCHAR(100) NULL,
  details     VARCHAR(100) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE clinicalitems (
  id          BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber   BIGINT       NULL,
  sectionDate DATETIME     NULL,
  dateOfItem  VARCHAR(100) NULL,
  entry       VARCHAR(100) NULL,
  details     VARCHAR(100) NULL,
  PRIMARY KEY (id)
);

CREATE TABLE investigations (
   id          BIGINT         NOT NULL AUTO_INCREMENT,
   nhsNumber   BIGINT         NULL,
   sectionDate DATETIME       NULL,
   title       VARCHAR(100)   NULL,
   details     VARCHAR(1000)  NULL,
   lastUpdated DATETIME       NULL,
   PRIMARY KEY (id)
);

CREATE TABLE locations (
  id                 BIGINT       NOT NULL AUTO_INCREMENT,
  name               VARCHAR(250) NOT NULL,
  org_ods_code       VARCHAR(250) NOT NULL,
  org_ods_code_name  VARCHAR(250) NOT NULL,
  site_ods_code      VARCHAR(250) NOT NULL,
  site_ods_code_name VARCHAR(250) NOT NULL,
  lastUpdated        DATETIME     NULL,
  PRIMARY KEY (id)
);

CREATE TABLE orders (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  identifier        VARCHAR(250) NULL,
  orderDate         DATETIME     NULL,
  subjectPatientId  BIGINT       NULL,
  sourceOrgId       BIGINT       NULL,
  targetOrgId       BIGINT       NULL,
  reasonCode        BIGINT       NULL,
  reasonDescription VARCHAR(250) NULL,
  reasonText        VARCHAR(300) NULL,
  detail            VARCHAR(300) NULL,
  recieved          BOOLEAN      NULL,
  PRIMARY KEY (id)
);

CREATE TABLE emergencycodes (
  id                BIGINT       NOT NULL AUTO_INCREMENT,
  nhsNumber        VARCHAR(250) NULL,
  emergencyCodeDate         DATETIME     NULL,
  entry             VARCHAR(300) NULL,
  details           VARCHAR(300) NULL,
  location          VARCHAR(300) NULL,
  PRIMARY KEY (id)
);
