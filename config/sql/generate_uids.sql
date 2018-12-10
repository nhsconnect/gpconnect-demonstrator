SET SQL_SAFE_UPDATES = 0;
UPDATE allergyintolerance SET guid=(SELECT uuid());
UPDATE medication_requests SET guid=(SELECT uuid());
UPDATE medication_statements SET guid=(SELECT uuid());
UPDATE gpconnect1.medication_statements ms join gpconnect1.medication_requests mr on cast(ms.medicationRequestId  as unsigned)= mr.id SET ms.medicationRequestId = mr.id
