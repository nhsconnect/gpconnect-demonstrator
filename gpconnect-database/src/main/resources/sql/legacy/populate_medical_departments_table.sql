LOCK TABLES poc_legacy.medical_departments WRITE;

INSERT INTO poc_legacy.medical_departments
  (id,      department)
VALUES
  (1,       'Neighbourhood'),
  (2,       'Hospital'),
  (3,       'Community Care'),
  (4,       'Primary Care'),
  (5,       'Mental Health');

UNLOCK TABLES;