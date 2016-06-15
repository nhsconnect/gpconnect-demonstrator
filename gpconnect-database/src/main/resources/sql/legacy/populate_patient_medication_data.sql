INSERT INTO gpconnect.medication_orders
  (id, date_written, order_status, patient_id, author_id, medication_id, dosage_text, dispense_quantity_text, dispense_review_date, dispense_medication_id, dispense_repeats_allowed)
VALUES (1, '2015-11-24 13:05:24', 'active', 9000000033, 2, 44588005, 'Test dosageText for medicationOrder 1', 'Dispense quantity test text for medicationOrder 1', '2016-01-07 00:00:01', 44588005, 4),
  (2, '2016-03-08 09:22:13', 'completed', 9000000033, 2, 7774008, 'Test Dose Text MedOrder 2', 'DispQuantity Text MedicationOrder 2', '2016-08-12 00:00:01', 40924008, 6),
  (3, '2013-12-22 16:03:48', 'on-hold', 9000000041, 1, 44068004, 'Test Dose Text MedOrder 3', 'DispQuantity Text MedicationOrder 3', '2014-01-22 00:00:01', 44068004, 7),
  (4, '2016-04-18 15:11:22', 'completed', 9000000009, 1, 26437003, 'Test Dose Text MedOrder 4', 'DispQuantity Text MedicationOrder 4', '2016-07-18 00:00:01', 32050003, 4),
  (5, '2013-02-01 11:22:33', 'stopped', 9000000041, 3, 7774008, 'Test Dose Text MedOrder 5', 'DispQuantity Text MedicationOrder 5', '2014-02-01 00:00:01', 40924008, 52);