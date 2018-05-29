SET SQL_SAFE_UPDATES = 0;
UPDATE allergyintolerance SET guid=(SELECT uuid());
UPDATE medication_requests SET guid=(SELECT uuid());
UPDATE medication_statements SET guid=(SELECT uuid());