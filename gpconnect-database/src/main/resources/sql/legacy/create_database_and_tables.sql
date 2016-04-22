/* Create a new poc_legacy database and begin configuration */
DROP DATABASE IF EXISTS gpconnect;
CREATE DATABASE         gpconnect DEFAULT CHARACTER SET utf8;
USE                     gpconnect;

/* Destroy all existing data */
DROP TABLE IF EXISTS gpconnect.general_practitioners;
DROP TABLE IF EXISTS gpconnect.medical_departments;
DROP TABLE IF EXISTS gpconnect.patients;
DROP TABLE IF EXISTS gpconnect.allergies;
DROP TABLE IF EXISTS gpconnect.medications;
DROP TABLE IF EXISTS gpconnect.problems;

DROP TABLE IF EXISTS gpconnect.transfers_of_care;
DROP TABLE IF EXISTS gpconnect.allergy_headlines;
DROP TABLE IF EXISTS gpconnect.contact_headlines;
DROP TABLE IF EXISTS gpconnect.medication_headlines;
DROP TABLE IF EXISTS gpconnect.problem_headlines;

/* Create new table schemas */
CREATE TABLE gpconnect.general_practitioners (
  id            BIGINT        NOT NULL    AUTO_INCREMENT,
  gp_name       VARCHAR(150)  NULL,
  address_1     VARCHAR(100)  NULL,
  address_2     VARCHAR(100)  NULL,
  address_3     VARCHAR(100)  NULL,
  address_4     VARCHAR(100)  NULL,
  address_5     VARCHAR(100)  NULL,
  postcode      VARCHAR(10)   NULL,
  PRIMARY KEY   (id)
);

CREATE TABLE gpconnect.medical_departments (
  id            BIGINT        NOT NULL    AUTO_INCREMENT,
  department    VARCHAR(150)  NULL,
  PRIMARY KEY   (id)
);

CREATE TABLE gpconnect.patients (
  id              BIGINT          NOT NULL    AUTO_INCREMENT,
  title           VARCHAR(10)     NULL,
  first_name      VARCHAR(30)     NULL,
  last_name       VARCHAR(30)     NULL,
  address_1       VARCHAR(100)    NULL,
  address_2       VARCHAR(100)    NULL,
  address_3       VARCHAR(100)    NULL,
  address_4       VARCHAR(100)    NULL,
  address_5       VARCHAR(100)    NULL,
  postcode        VARCHAR(10)     NULL,
  phone           VARCHAR(20)     NULL,
  date_of_birth   DATE            NULL,
  gender          VARCHAR(10)     NULL,
  nhs_number      VARCHAR(20)     NULL,
  pas_number      VARCHAR(20)     NULL,
  department_id   BIGINT          NOT NULL,
  gp_id           BIGINT          NOT NULL,
  PRIMARY KEY     (id),
  FOREIGN KEY     (department_id) REFERENCES  gpconnect.medical_departments(id),
  FOREIGN KEY     (gp_id)         REFERENCES  gpconnect.general_practitioners(id)
);

CREATE TABLE gpconnect.allergies (
  id                  BIGINT        NOT NULL    AUTO_INCREMENT,
  html                VARCHAR(4096) NULL,
  provider            VARCHAR(10)   NULL,
  PRIMARY KEY         (id)
);

CREATE TABLE gpconnect.medications (
  id                  BIGINT        NOT NULL    AUTO_INCREMENT,
  html                VARCHAR(4096) NULL,
  provider            VARCHAR(10)   NULL,
  PRIMARY KEY         (id)
);

CREATE TABLE gpconnect.problems (
 id                  BIGINT        NOT NULL    AUTO_INCREMENT,
 html                VARCHAR(4096) NULL,
 provider            VARCHAR(10)   NULL,
 PRIMARY KEY         (id)
 );

/* Delete the answer user (grant all to workaround MySQL not supporting 'IF EXISTS' for users) */
GRANT ALL ON gpconnect.* TO 'answer' IDENTIFIED BY 'answer99q';
DROP USER 'answer';
FLUSH PRIVILEGES;

/* Create a new answer user with full privileges */
CREATE USER 'answer'                              IDENTIFIED BY 'answer99q';
GRANT ALL ON gpconnect.* TO 'answer'@'%'          IDENTIFIED BY 'answer99q';
GRANT ALL ON gpconnect.* TO 'answer'@'localhost'  IDENTIFIED BY 'answer99q';
FLUSH PRIVILEGES;