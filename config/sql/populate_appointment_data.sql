USE gpconnect1_2_8;
INSERT INTO appointment_schedules
  (id,practitionerId,identifier,typeCode,typeDescription,locationId,practitionerRoleCode,practitionerRoleDisplay,startDateTime,endDateTime,scheduleComment,lastUpdated,serviceId)
VALUES
  (1,1,'Schedule001','394802001','General medicine',16,'R0260','General Medical Practitioner','2016-03-22 10:00:00','2030-12-22 17:59:59','Schedule 1 for general appointments','2016-07-25 12:00:00', null),
  (2,2,'Schedule002','394814009','General practice',17,'R1480','Healthcare Assistant','2016-06-24 09:00:00','2034-06-22 09:00:00','Schedule 2 for general appointments','2016-07-25 12:00:00', null),
  (3,3,'Schedule003','394593009','Medical oncology',2,'R0260','General Medical Practitioner','2013-12-22 11:46:22','2040-12-22 11:46:22','Schedule 3 for general appointments','2016-07-25 12:00:00', null),
  (4,1,'Schedule004','394814009','General practice',16,'R0260','General Medical Practitioner','2013-12-22 11:46:22','2040-12-22 11:46:22','Schedule 4 for general appointments with practitioner Goff, Carolyn D. @ The Hepworth Surgery Main Building','2016-07-25 12:00:00', 1),
  (5,2,'Schedule005','394814009','General practice',16,'R0260','General Medical Practitioner','2013-12-22 10:46:22','2040-12-22 10:46:22','Schedule 5 for general appointments with practitioner Cash, Claire F. @ The Hepworth Surgery Main Building','2016-07-25 12:00:00', 1),
  (6,3,'Schedule006','394814009','General practice',2,'R0260','General Medical Practitioner','2013-12-22 09:46:22','2040-12-22 09:46:22','Schedule 6 for general appointments with practitioner Spencer, Hyacinth A. @ The Moore Surgery Main Building','2016-07-25 12:00:00', 1),
  (7,3,'Schedule007','394814009','General practice',3,'R0260','General Medical Practitioner','2013-12-22 09:46:22','2040-12-22 09:46:22','Schedule 7 for general appointments with practitioner Bailey, Demetrius B. @ The Hockey Surgery Main Building','2016-07-25 12:00:00', 1),
  (8,1,'Schedule008','394814009','General practice',3,'R0260','General Medical Practitioner','2013-12-22 10:46:22','2040-12-22 10:46:22','Schedule 8 for general appointments with practitioner Guthrie, Indigo D. @ The Hockey Surgery Main Building','2016-07-25 12:00:00', 2),
  (9,2,'Schedule009','394814009','General practice',4,'R0260','General Medical Practitioner','2013-12-22 09:46:22','2040-12-22 09:46:22','Schedule 9 for general appointments with practitioner Hudson, Inez G. @ The Hockey Surgery Annex','2016-07-25 12:00:00', 2),
  (10,3,'Schedule010','394814009','General practice',4,'R0260','General Medical Practitioner','2013-12-22 10:46:22','2040-12-22 10:46:22','Schedule 10 for general appointments with practitioner Joseph, May N. @ The Hockey Surgery Annex','2016-07-25 12:00:00', 2),
  (11,2,'Schedule011','394814010','General practice',5,'R0260','General Medical Practitioner','2010-10-22 01:01:01','2040-12-22 10:46:22','Schedule 11 for general appointments with practitioner','2016-07-25 12:00:00', 2),
  (12,1,'Schedule012','394814010','General practice',16,'R0620','Staff Nurse','2010-10-22 01:01:01','2040-12-22 10:46:22','Schedule 12 for phone appointments with staff nurse','2016-07-25 12:00:00', 2);
INSERT INTO appointment_slots
  (id,typeCode,typeDisplay,scheduleReference,freeBusyType,startDateTime,endDateTime,lastUpdated,gpConnectBookable)
VALUES
  (1,408443003,'General medical practice',2,'FREE','2016-06-29 09:00:00','2016-06-29 09:59:59','2016-07-25 12:00:00',1),
  (2,408443003,'General medical practice',2,'FREE','2016-06-29 10:00:00','2016-06-29 10:59:59','2016-07-25 12:00:00',1),
  (3,408443003,'General medical practice',2,'FREE','2016-06-29 11:00:00','2016-06-29 11:59:59','2016-07-25 12:00:00',1),
  (4,408443003,'General medical practice',2,'FREE','2016-06-29 13:00:00','2016-06-29 13:59:59','2016-07-25 12:00:00',1),
  (5,408443003,'General medical practice',2,'FREE','2016-06-29 14:00:00','2016-06-29 14:59:59','2016-07-25 12:00:00',1),
  (6,408443003,'General medical practice',2,'FREE','2016-06-29 15:00:00','2016-06-29 15:59:59','2016-07-25 12:00:00',1),
  (7,408443003,'General medical practice',2,'FREE','2016-06-29 16:00:00','2016-06-29 16:59:59','2016-07-25 12:00:00',1);

INSERT INTO addresses
  (id,line,city,district,state,postalCode,country)
VALUES
  (1,'23 Main Street','Pudsey','Leeds','West Yorkshire','GPC 111','UK'),
  (2,'2 Garforth Avenue','Ardsley','Leeds','West Yorkshire','GPC 112','England'),
  (3,'24 Back Lane','Farsley','Leeds','West Yorkshire','GPC 113','U.K.'),
  (4,'58 St Albans','Bramley','Leeds','West Yorkshire','GPC 114','UK'),
  (5,'12 Frog Terrace','Roundhay','Leeds','West Yorkshire','GPC 115','UK'),
  (6,'Woodlands','Harehills','Leeds','West Yorkshire','GPC 116','UK'),
  (7,'34 Woodlands Grove','Hunslet','Leeds','West Yorkshire','GPC 117','UK'),
  (8,'23 Daniel Street','Beeston','Leeds','West Yorkshire','GPC 118','UK'),
  (9,'Juniper Bay','Morley','Leeds','West Yorkshire','GPC 119','UK'),
  (10,'Cloverfield','Tingley','Leeds','West Yorkshire','GPC 120','UK'),
  (11,'17 Hopefield Ct','Normanton','Leeds','West Yorkshire','GPC 121','UK'),
  (12,'Leigh View','Cleckheaton','Leeds','West Yorkshire','GPC 122','UK'),
  (13,'69 Michael Lane','Garforth','Leeds','West Yorkshire','GPC 123','UK'),
  (14,'West View','Armley','Leeds','West Yorkshire','GPC 124','UK'),
  (15,'25 George Avenue','Calverley','Leeds','West Yorkshire','GPC 125','UK'),
  (16,'NHS NPFIT Test Data Manager','Princes Exchange','Leeds','West Yorkshire','LS1 4HY','UK'),
  (17,'NHS Digital Test Data Manager','Whitehall 2','Leeds','West Yorkshire','LS1 4HR','UK');
  
INSERT INTO locations
  (id,name,org_ods_code,org_ods_code_name,site_ods_code,site_ods_code_name,status,lastUpdated,address_id)
VALUES
  (1,'Building A','GPC001','GP Connect Demonstrator','Z26556','BUILDING A','active','2016-07-25 12:00:00',1),
  (2,'Building B','GPC001','GP Connect Demonstrator','Z33436','BUILDING B','active','2016-07-25 12:00:00',2),
  (3,'Building C','R1A14','Test GP Care Trust','Z33433','BUILDING C','suspended','2016-07-25 12:00:00',3),
  (4,'Annex A','R1A15','The Hepworth Surgery','Z33435','ANNEX A','active','2016-07-25 12:00:00',4),
  (5,'Building 5','R3B46','New GP Practice','Z12345','ANNEX','active','2016-07-25 12:00:00',5),
  (6,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','active','2016-07-25 12:00:00',6),
  (7,'Building 7','ORG115','BUILDING A','Z12346','ANNEX','active','2016-07-25 12:00:00',7),
  (8,'Building 8','R1A17','BUILDING A','Z33432','ANNEX','active','2016-07-25 12:00:00',8),
  (9,'Building 8','R1A17','BUILDING B','Z33432','ANNEX','suspended','2016-07-25 12:00:00',9),
  (10,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','suspended','2016-07-25 12:00:00',10),
  (11,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','suspended','2016-07-25 12:00:00',11),
  (12,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','suspended','2016-07-25 12:00:00',12),
  (13,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','suspended','2016-07-25 12:00:00',13),
  (14,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','suspended','2016-07-25 12:00:00',14),
  (15,'Building 6','ORG112','BUILDING A','Z33433','ANNEX','suspended','2016-07-25 12:00:00',15),
  (16,'Dr Legg''s Main Surgery','A20047','BUILDING Z','Z55555','ANNEX','active','2016-07-25 12:00:00',16),
  (17,'Dr Legg''s Branch Surgery','A20047','BUILDING X','Z55556','ANNEX','active','2016-07-25 12:00:00',17);

INSERT INTO appointment_healthcareservices
  (id,identifier,healthcareservice_name,organizationId)
VALUES
  (1,'99998','Dr Legg''s Surgery',7),
  (2,'99999','Dr Legg''s Surgery Hub',7);