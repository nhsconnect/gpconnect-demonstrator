USE gpconnect0_7;

INSERT INTO medication_orders
  (id,date_written,order_status,patient_id,author_id,medication_id,dosage_text,dispense_quantity_text,dispense_review_date,dispense_medication_id,dispense_repeats_allowed,lastUpdated)
VALUES
  (1,'2015-11-24 13:05:24','active',4,2,44588005,'Test dosageText for medicationOrder 1','Dispense quantity test text for medicationOrder 1','2016-01-07 00:00:01',44588005,4,'2016-07-25 12:00:00'),
  (2,'2016-03-08 09:22:13','completed',4,2,7774008,'Test Dose Text MedOrder 2','DispQuantity Text MedicationOrder 2','2016-08-12 00:00:01',40924008,6,'2016-07-25 12:00:00'),
  (3,'2013-12-22 16:03:48','on-hold',NULL,1,44068004,'Test Dose Text MedOrder 3','DispQuantity Text MedicationOrder 3','2014-01-22 00:00:01',44068004,7,'2016-07-25 12:00:00'),
  (4,'2016-04-18 15:11:22','completed',1,1,26437003,'Test Dose Text MedOrder 4','DispQuantity Text MedicationOrder 4','2016-07-18 00:00:01',32050003,4,'2016-07-25 12:00:00'),
  (5,'2013-02-01 11:22:33','stopped',5,3,7774008,'Test Dose Text MedOrder 5','DispQuantity Text MedicationOrder 5','2014-02-01 00:00:01',40924008,52,'2016-07-25 12:00:00');

INSERT INTO medication_dispenses
  (id,status,patientId,medicationOrderId,medicationId,medicationName,dosageText,lastUpdated)
VALUES
  (1,'completed',4,2,7774008,'MedicationName From Medication Dispenses 1','Dispense Dosage Text 1','2016-07-25 12:00:00'),
  (2,'completed',4,2,7774008,'MedicationName From Medication Dispenses 2','Dispense Dosage Text 1.1','2016-07-25 12:00:00'),
  (3,'on_hold',4,1,44588005,'MedicationName From Medication Dispenses 3','Dispense Dosage Text 3','2016-07-25 12:00:00'),
  (4,'completed',5,5,40924008,'MedicationName From Medication Dispenses 4','Dispense Dosage Text 4','2016-07-25 12:00:00'),
  (5,'active',1,4,32050003,'MedicationName From Medication Dispenses 5','Dispense Dosage Text 5','2016-07-25 12:00:00'),
  (6,'completed',4,2,40924008,'MedicationName From Medication Dispenses 6','Dispense Dosage Text 6','2016-07-25 12:00:00'),
  (7,'stopped',4,3,34953000,'MedicationName From Medication Dispenses 7','Dispense Dosage Text 7','2016-07-25 12:00:00');

INSERT INTO medication_administrations
  (id,patientId,practitionerId,encounterId,prescriptionId,administrationDate,medicationId,lastUpdated)
VALUES
  (1,4,2,NULL,3,'2013-12-22 11:46:22',44068004,'2016-07-25 12:00:00'),
  (2,5,3,NULL,5,'2013-02-04 12:13:14',7774008,'2016-07-25 12:00:00'),
  (3,4,2,NULL,3,'2014-01-16 09:04:19',34953000,'2016-07-25 12:00:00');
