INSERT INTO gpconnect.medication_statements
  (id,lastIssueDate,encounterId,statusCode,statusDisplay,medicationId,startDate,endDate,dateAsserted,
    patientId,takenCode,takenDisplay,dosageText,dosageInstruction,lastUpdated)
VALUES
 (2,'2018-01-04',12,'completed','Completed',5,'2018-01-04','2018-01-04','2018-01-04',3,'y','Yes','Dosage text','Dosage instructions','2018-03-15');

INSERT INTO gpconnect.medication_reason_codes
 (id,reasonCode,reasonDisplay)
VALUES
 (3,'1383008','Hallucinogen mood disorder');

INSERT INTO gpconnect.medication_notes
 (id,dateWritten,authorReferenceUrl,authorId,noteText)
VALUES
 (2,'2018-01-03','https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,'Patient 3 medication note');
 
INSERT INTO gpconnect.medication_statement_reason_codes
 (medicationStatementId,reasonCodeId)
VALUES
 (2,3);

INSERT INTO gpconnect.medication_statement_notes
 (medicationStatementId,noteId)
VALUES
 (2,2);

INSERT INTO gpconnect.medication_requests
 (id,groupIdentifier,statusCode,statusDisplay,intentCode,intentDisplay,medicationId,patientId,encounterId,authoredOn,
 requesterUrl,requesterId,authorisingPractitionerId,dosageText,dosageInstruction,dispenseRequestStartDate,dispenseRequestEndDate,
 dispenseQuantityValue,dispenseQuantityUnit,dispenseQuantityText,expectedSupplyDuration,
 dispenseRequestOrganizationId,priorMedicationRequestId,numberOfRepeatPrescriptionsAllowed,numberOfRepeatPrescriptionsIssued,
 authorisationExpiryDate,prescriptionTypeCode,prescriptionTypeDisplay,statusReasonDate,statusReason,lastUpdated)
VALUES
 (2,'group2','completed','Completed','plan','Plan',5,3,12,'2018-01-04',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,2,'Dosage Text','Dosage Instructions',
 '2018-01-04',null,null,null,'1 packet','10',2,null,0,0,'2018-02-04','acute','Acute',null,null,'2018-03-15'),
 (3,'group2','completed','Completed','order','Order',5,3,12,'2018-01-04',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,2,'Dosage Text','Dosage Instructions',
 '2018-01-04',null,null,null,'1 packet','10',2,null,0,0,'2018-02-04','acute','Acute',null,null,'2018-03-15');

UPDATE gpconnect.medication_statements SET medicationRequestId = 2 WHERE id = 2;

INSERT INTO gpconnect.medication_request_based_on
 (id,referenceUrl,referenceId)
VALUES
 (2,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1',2);

INSERT INTO gpconnect.medication_request_based_on_references
 (medicationRequestId,basedOnReferenceId)
VALUES
 (3,2);

INSERT INTO gpconnect.medication_request_reason_codes
 (medicationRequestId,reasonCodeId)
VALUES
 (2,3),
 (3,3);

INSERT INTO gpconnect.medication_request_notes
 (medicationRequestId,noteId)
VALUES
 (2,2),
 (3,2);

  INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay, recorder)
VALUES
  (9476719974,'2016-07-01 12:00:00',"Cured","Difficulty breathing","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"419063004","Allergy to horse dander (disorder)","289100008","Difficulty taking deep breaths (finding)", 'G13579135'),
  (9476719974,'2016-07-01 12:00:00',"Ongoing","Dry, red and cracked skin","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294915005","Iodophore allergy (disorder)","702757002","Severe dry skin (finding)", 'G13579135');