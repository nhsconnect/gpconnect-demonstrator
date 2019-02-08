USE gpconnect1;
INSERT INTO medical_departments
  (id,department,lastUpdated)
VALUES
  (1,'Neighbourhood','2016-07-25 12:00:00'),
  (2,'Hospital','2016-07-25 12:00:00'),
  (3,'Community Care','2016-07-25 12:00:00'),
  (4,'Primary Care','2016-07-25 12:00:00'),
  (5,'Mental Health','2016-07-25 12:00:00');

INSERT INTO organizations
  (id,org_code,org_name,lastUpdated)
VALUES
  (1,'GPC001','GP Connect Demonstrator','2016-07-25 12:00:00'),
  (2,'R1A14','Test GP Care Trust','2016-07-25 12:00:00'),
  (3,'R1A17','Test GP Second Care Trust','2016-07-25 12:00:00'),
  (4,'R1A14','The Hockey Surgery','2016-07-25 12:00:00'),
  (5,'R1A15','Test GP Care Trust Site B','2016-07-25 12:00:00'),
  (6,'R3B46','New GP Practice','2016-07-25 12:00:00'),
  (7,'A20047','Dr Legg''s Surgery','2016-07-25 12:00:00');
