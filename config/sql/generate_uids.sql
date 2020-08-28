USE gpconnect1_5;
SET SQL_SAFE_UPDATES = 0;
UPDATE allergyintolerance SET guid=(SELECT uuid());
UPDATE medication_requests SET guid=(SELECT uuid());
UPDATE medication_statements SET guid=(SELECT uuid());
UPDATE gpconnect1_5.medication_statements ms join medication_requests mr on cast(ms.medicationRequestId  as unsigned)= mr.id SET ms.medicationRequestId = mr.id
