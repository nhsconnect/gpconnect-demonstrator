LOCK TABLES gpconnect.medical_departments WRITE;

INSERT INTO gpconnect.medical_departments
  (id,      department)
VALUES
  (1,       'Neighbourhood'),
  (2,       'Hospital'),
  (3,       'Community Care'),
  (4,       'Primary Care'),
  (5,       'Mental Health');

UNLOCK TABLES;