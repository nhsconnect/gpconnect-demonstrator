drop table if exists poc_legacy.problems;
drop table if exists poc_legacy.medications;
drop table if exists poc_legacy.contacts;
drop table if exists poc_legacy.allergies;
drop table if exists poc_legacy.sites;
drop table if exists poc_legacy.transfer_details;
drop table if exists poc_legacy.transfer_cares;
drop table if exists poc_legacy.transfer_care_compositions;

create table poc_legacy.transfer_care_compositions (
  id bigint not null auto_increment,
  composition_id varchar(100) null,
  primary key (id),
  UNIQUE (composition_id)
);

create table poc_legacy.transfer_cares (
  id bigint not null auto_increment,
  transfer_care_composition_id bigint not null,
  primary key (id),
  FOREIGN KEY (transfer_care_composition_id) REFERENCES poc_legacy.transfer_care_compositions(id)
);

create table poc_legacy.transfer_details (
  id bigint not null auto_increment,
  transfer_care_id bigint not null,
  reason_for_contact varchar(256) null,
  clinical_summary varchar(256) null,
  primary key (id),
  FOREIGN KEY (transfer_care_id) REFERENCES poc_legacy.transfer_cares(id)
);

create table poc_legacy.sites (
  id bigint not null auto_increment,
  patient_id bigint not null,
  time_stamp varchar(100),
  transfer_detail_id bigint not null,
  site_to varchar(256) null,
  site_from varchar(256) null,
  primary key (id),
  FOREIGN KEY (transfer_detail_id) REFERENCES poc_legacy.transfer_details(id)
);

create table poc_legacy.allergies (
  id bigint not null auto_increment,
  transfer_care_id bigint not null,
  reaction varchar(256) null,
  cause varchar(256) null,
  causeCode varchar(256) null,
  cause_terminology varchar(256) null,
  primary key (id),
  FOREIGN KEY (transfer_care_id) REFERENCES poc_legacy.transfer_cares(id)
);

create table poc_legacy.contacts (
  id bigint not null auto_increment,
  transfer_care_id bigint not null,
  name varchar(256) null,
  contact_information varchar(256) null,
  relationship varchar(256) null,
  relationship_type varchar(256) null,
  relationship_code varchar(256) null,
  relationship_terminology varchar(256) null,
  next_of_kin boolean null,
  note varchar(256) null,
  primary key (id),
  FOREIGN KEY (transfer_care_id) REFERENCES poc_legacy.transfer_cares(id)
);

create table poc_legacy.medications (
  id bigint not null auto_increment,
  transfer_care_id bigint not null,
  name varchar(256) null,
  code varchar(256) null,
  terminology varchar(256) null,
  route varchar(256) null,
  dose_amount varchar(256) null,
  dose_timing varchar(256) null,
  start_date_time varchar(256) null,
  dose_directions varchar(256) null,
  primary key (id),
  FOREIGN KEY (transfer_care_id) REFERENCES poc_legacy.transfer_cares(id)
);

create table poc_legacy.problems (
  id bigint not null auto_increment,
  transfer_care_id bigint not null,
  problem varchar(256) null,
  description varchar(256) null,
  code varchar(256) null,
  terminology varchar(256) null,
  date_of_onset varchar(256) null,
  primary key (id),
  FOREIGN KEY (transfer_care_id) REFERENCES poc_legacy.transfer_cares(id)
);
