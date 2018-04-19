INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay)
VALUES
  (9476718927,'2016-07-01 12:00:00',"Cured","Swollen lips, tongue, eyes","Major", "resolved","unconfirmed","medication","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294505008","Amoxicillin allergy (disorder)","703630003","Red eye (finding)"),
  (9476718927,'2016-07-01 12:00:00',"Ongoing","Wheezing, chest tightness, shortness of breath","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"293585002","Salicylate allergy (disorder)","23924001","Tight chest (finding)");
  
  INSERT INTO gpconnect.medication_statements
  (id,lastIssueDate,encounterId,statusCode,statusDisplay,medicationId,startDate,endDate,dateAsserted,
    patientId,takenCode,takenDisplay,dosageText,dosageInstruction,lastUpdated)
VALUES
 (7,'1958-01-04',12,'completed','Completed',2,'1958-01-04','1958-01-04','1958-01-04',3,'y','Yes','Take one tablet three times a day','Take with a full glass of water','1958-03-15');

INSERT INTO gpconnect.medication_reason_codes
 (id,reasonCode,reasonDisplay)
VALUES
 (7,'1383008','Hallucinogen mood disorder');

INSERT INTO gpconnect.medication_notes
 (id,dateWritten,authorReferenceUrl,authorId,noteText)
VALUES
 (6,'1958-01-03','https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,'Patient feels nauseous after taking');
 
INSERT INTO gpconnect.medication_statement_reason_codes
 (medicationStatementId,reasonCodeId)
VALUES
 (7,7);

INSERT INTO gpconnect.medication_statement_notes
 (medicationStatementId,noteId)
VALUES
 (7,6);

INSERT INTO gpconnect.medication_requests
 (id,groupIdentifier,statusCode,statusDisplay,intentCode,intentDisplay,medicationId,patientId,encounterId,authoredOn,
 requesterUrl,requesterId,authorisingPractitionerId,dosageText,dosageInstruction,dispenseRequestStartDate,dispenseRequestEndDate,
 dispenseQuantityValue,dispenseQuantityUnit,dispenseQuantityText,expectedSupplyDuration,
 dispenseRequestOrganizationId,priorMedicationRequestId,numberOfRepeatPrescriptionsAllowed,numberOfRepeatPrescriptionsIssued,
 authorisationExpiryDate,prescriptionTypeCode,prescriptionTypeDisplay,statusReasonDate,statusReason,lastUpdated)
VALUES
 (15,'group7','completed','Completed','plan','Plan',5,3,12,'1958-01-04',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,2,'Take one tablet three times a day','Take with a full glass of water',
 '1958-01-04',null,null,null,'90 tablets','1',2,null,0,0,'1958-02-04','acute','Acute',null,null,'1958-03-15'),
 (16,'group7','completed','Completed','order','Order',5,3,12,'1958-01-04',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,2,'Take one tablet three times a day','Take with a full glass of water',
 '1958-01-04',null,null,null,'90 tabletst','1',2,null,0,0,'1958-02-04','acute','Acute',null,null,'1958-03-15');

UPDATE gpconnect.medication_statements SET medicationRequestId = 14 WHERE id = 4;

INSERT INTO gpconnect.medication_request_based_on
 (id,referenceUrl,referenceId)
VALUES
 (5,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1',2);

INSERT INTO gpconnect.medication_request_based_on_references
 (medicationRequestId,basedOnReferenceId)
VALUES
 (15,5);

INSERT INTO gpconnect.medication_request_reason_codes
 (medicationRequestId,reasonCodeId)
VALUES
 (15,7),
 (16,7);

INSERT INTO gpconnect.medication_request_notes
 (medicationRequestId,noteId)
VALUES
 (15,6),
 (16,6);