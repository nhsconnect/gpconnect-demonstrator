INSERT INTO gpconnect.medication_orders
  (id, date_written, order_status, patient_id, author_id, medication_id, dosage_text, dispense_quantity_text, dispense_review_date, dispense_medication_id, dispense_repeats_allowed)
VALUES (1, '2015-11-24 13:05:24', 'active', 9000000033, 2, 44588005, 'Test dosageText for medicationOrder 1', 'Dispense quantity test text for medicationOrder 1', '2016-01-07 00:00:01', 44588005, 4),
  (2, '2016-03-08 09:22:13', 'completed', 9000000033, 2, 7774008, 'Test Dose Text MedOrder 2', 'DispQuantity Text MedicationOrder 2', '2016-08-12 00:00:01', 40924008, 6),
  (3, '2013-12-22 16:03:48', 'on-hold', null, 1, 44068004, 'Test Dose Text MedOrder 3', 'DispQuantity Text MedicationOrder 3', '2014-01-22 00:00:01', 44068004, 7),
  (4, '2016-04-18 15:11:22', 'completed', 9000000009, 1, 26437003, 'Test Dose Text MedOrder 4', 'DispQuantity Text MedicationOrder 4', '2016-07-18 00:00:01', 32050003, 4),
  (5, '2013-02-01 11:22:33', 'stopped', 9000000041, 3, 7774008, 'Test Dose Text MedOrder 5', 'DispQuantity Text MedicationOrder 5', '2014-02-01 00:00:01', 40924008, 52);
  
INSERT INTO gpconnect.medication_dispenses
  (id, status, patientId, medicationOrderId, medicationId, medicationName, dosageText)
VALUES (1, 'completed', 9000000033, 2, 7774008, 'MedicationName From Medication Dispenses 1', 'Dispense Dosage Text 1'),
(2, 'completed', 9000000033, 2, 7774008, 'MedicationName From Medication Dispenses 2', 'Dispense Dosage Text 1.1'),
(3, 'on_hold', 9000000033, 1, 44588005, 'MedicationName From Medication Dispenses 3', 'Dispense Dosage Text 3'),
(4, 'completed', 9000000041, 5, 40924008, 'MedicationName From Medication Dispenses 4', 'Dispense Dosage Text 4'),
(5, 'active', 9000000009, 4, 32050003, 'MedicationName From Medication Dispenses 5', 'Dispense Dosage Text 5'),
(6, 'completed', 9000000033, 2, 40924008, 'MedicationName From Medication Dispenses 6', 'Dispense Dosage Text 6'),
(7, 'stopped', 9000000033, 3, 34953000, 'MedicationName From Medication Dispenses 7', 'Dispense Dosage Text 7');