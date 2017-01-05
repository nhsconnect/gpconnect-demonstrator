LOCK TABLES gpconnect.patients WRITE;
INSERT INTO gpconnect.patients
  (id,    title,   first_name,   last_name,    address_1, address_2, address_3, address_4, address_5, postcode,   phone, date_of_birth, gender, nhs_number, pas_number, department_id, gp_id, lastUpdated,sensitive_flag,primary_care_code,date_of_death)
VALUE
   (1,'MRS','Minnie','DAWES','','24 GRAMMAR SCHOOL ROAD','','BRIGG','','DN20 8AF','',31/05/1952,'',9476719931,352541,1,3,21/01/2011,'','A21472',null),
   (2,'MRS','Dolly','BANTON','','11 QUEENSWAY','','SCUNTHORPE','','DN16 2BZ','',31/05/1952,'',9476719931,352541,1,3,21/01/2011,'','A21472',null),
   (3,'MRS','Minnie','DAWES','','24 GRAMMAR SCHOOL ROAD','','BRIGG','','DN20 8AF','',31/05/1952,'',9476719931,352541,1,3,21/01/2011,'','A21472',null),
   (4,'MRS','Minnie','DAWES','','24 GRAMMAR SCHOOL ROAD','','BRIGG','','DN20 8AF','',31/05/1952,'',9476719931,352541,1,3,21/01/2011,'','A21472',null);

UNLOCK TABLES;

	