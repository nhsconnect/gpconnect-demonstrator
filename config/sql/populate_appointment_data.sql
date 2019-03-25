USE gpconnect0;

INSERT INTO appointment_schedules
  (id,practitionerId,identifier,typeCode,typeDescription,locationId,startDateTime,endDateTime,scheduleComment,lastUpdated)
VALUES
  (1,2,'Schedule001','394802001','General medicine',1,'2016-03-22 10:00:00','2030-12-22 17:59:59','Schedule 1 for general appointments','2016-07-25 12:00:00'),
  (2,1,'Schedule002','394814009','General practice',1,'2016-06-24 09:00:00','2034-06-22 09:00:00','Schedule 2 for general appointments','2016-07-25 12:00:00'),
  (3,3,'Schedule003','394593009','Medical oncology',2,'2013-12-22 11:46:22','2040-12-22 11:46:22','Schedule 3 for general appointments','2016-07-25 12:00:00'),
  (4,1,'Schedule004','394814009','General practice',1,'2013-12-22 11:46:22','2040-12-22 11:46:22','Schedule 4 for general appointments with practitioner Goff, Carolyn D. @ The Hepworth Surgery Main Building','2016-07-25 12:00:00'),
  (5,2,'Schedule005','394814009','General practice',1,'2013-12-22 10:46:22','2040-12-22 10:46:22','Schedule 5 for general appointments with practitioner Cash, Claire F. @ The Hepworth Surgery Main Building','2016-07-25 12:00:00'),
  (6,3,'Schedule006','394814009','General practice',2,'2013-12-22 09:46:22','2040-12-22 09:46:22','Schedule 6 for general appointments with practitioner Spencer, Hyacinth A. @ The Moore Surgery Main Building','2016-07-25 12:00:00'),
  (7,3,'Schedule007','394814009','General practice',3,'2013-12-22 09:46:22','2040-12-22 09:46:22','Schedule 7 for general appointments with practitioner Bailey, Demetrius B. @ The Hockey Surgery Main Building','2016-07-25 12:00:00'),
  (8,1,'Schedule008','394814009','General practice',3,'2013-12-22 10:46:22','2040-12-22 10:46:22','Schedule 8 for general appointments with practitioner Guthrie, Indigo D. @ The Hockey Surgery Main Building','2016-07-25 12:00:00'),
  (9,2,'Schedule009','394814009','General practice',4,'2013-12-22 09:46:22','2040-12-22 09:46:22','Schedule 9 for general appointments with practitioner Hudson, Inez G. @ The Hockey Surgery Annex','2016-07-25 12:00:00'),
  (10,3,'Schedule010','394814009','General practice',4,'2013-12-22 10:46:22','2040-12-22 10:46:22','Schedule 10 for general appointments with practitioner Joseph, May N. @ The Hockey Surgery Annex','2016-07-25 12:00:00'),
  (11,2,'Schedule011','394814010','General practice',5,'2010-10-22 01:01:01','2040-12-22 10:46:22','Schedule 11 for general appointments with practitioner','2016-07-25 12:00:00');

INSERT INTO appointment_slots
  (id,typeCode,typeDisplay,scheduleReference,freeBusyType,startDateTime,endDateTime,lastUpdated)
VALUES
  (1,408443003,'General medical practice',2,'FREE','2016-06-29 09:00:00','2016-06-29 09:59:59','2016-07-25 12:00:00'),
  (2,408443003,'General medical practice',2,'FREE','2016-06-29 10:00:00','2016-06-29 10:59:59','2016-07-25 12:00:00'),
  (3,408443003,'General medical practice',2,'FREE','2016-06-29 11:00:00','2016-06-29 11:59:59','2016-07-25 12:00:00'),
  (4,408443003,'General medical practice',2,'FREE','2016-06-29 13:00:00','2016-06-29 13:59:59','2016-07-25 12:00:00'),
  (5,408443003,'General medical practice',2,'FREE','2016-06-29 14:00:00','2016-06-29 14:59:59','2016-07-25 12:00:00'),
  (6,408443003,'General medical practice',2,'FREE','2016-06-29 15:00:00','2016-06-29 15:59:59','2016-07-25 12:00:00'),
  (7,408443003,'General medical practice',2,'FREE','2016-06-29 16:00:00','2016-06-29 16:59:59','2016-07-25 12:00:00');

INSERT INTO locations
  (id,name,org_ods_code,org_ods_code_name,site_ods_code,site_ods_code_name,lastUpdated)
VALUES
  (1,'Building A','GPC001','GP Connect Demonstrator','Z26556','BUILDING A','2016-07-25 12:00:00'),
  (2,'Building B','GPC001','GP Connect Demonstrator','Z33436','BUILDING B','2016-07-25 12:00:00'),
  (3,'Building C','R1A14','Test GP Care Trust','Z33433','BUILDING C','2016-07-25 12:00:00'),
  (4,'Annex A','R1A15','The Hepworth Surgery','Z33435','ANNEX A','2016-07-25 12:00:00'),
  (5,'Building 5','R3B46','New GP Practice','Z12345','ANNEX','2016-07-25 12:00:00'),
  (6,'Building 6','ORG114','BUILDING A','Z33433','ANNEX','2016-07-25 12:00:00'),
  (7,'Building 7','ORG115','BUILDING A','Z12346','ANNEX','2016-07-25 12:00:00'),
  (8,'Building 8','ORG111','BUILDING A','Z33432','ANNEX','2016-07-25 12:00:00');
