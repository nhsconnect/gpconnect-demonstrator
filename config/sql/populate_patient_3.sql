USE gpconnect1;
INSERT INTO medication_statements
  (id,lastIssueDate,statusCode,statusDisplay,medicationId,startDate,endDate,dateAsserted,
    patientId,takenCode,takenDisplay,dosageText,dosageInstruction,dosageLastChanged, prescribingAgency)
VALUES
 (2,'2018-01-04','completed','Completed',5,'2018-01-04',null,'2018-01-04',3,'unk','Yes','Use as often as required','Apply the medication after cleaning and drying the affected area','2018-03-15','prescribed-at-gp-practice');

INSERT INTO medication_reason_codes
 (id,reasonCode,reasonDisplay)
VALUES
 (3,'1383008','Hallucinogen mood disorder');

INSERT INTO medication_notes
 (id,dateWritten,authorReferenceUrl,authorId,noteText)
VALUES
 (2,'2018-01-03','https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,'Patient feels nauseous after taking');
 
INSERT INTO medication_statement_reason_codes
 (medicationStatementId,reasonCodeId)
VALUES
 (2,3);

INSERT INTO medication_statement_notes
 (medicationStatementId,noteId)
VALUES
 (2,2);

INSERT INTO medication_requests
 (id,groupIdentifier,statusCode,statusDisplay,intentCode,intentDisplay,medicationId,patientId,authoredOn,
 requesterUrl,requesterId,authorisingPractitionerId,dosageText,dosageInstruction,dispenseRequestStartDate,dispenseRequestEndDate,
 dispenseQuantityValue,dispenseQuantityUnit,dispenseQuantityText,expectedSupplyDuration,
 dispenseRequestOrganizationId,priorMedicationRequestId,numberOfRepeatPrescriptionsAllowed,numberOfRepeatPrescriptionsIssued,
 authorisationExpiryDate,prescriptionTypeCode,prescriptionTypeDisplay,statusReasonDate,statusReason,lastUpdated)
VALUES
 (2,'group2','completed','Completed','plan','Plan',5,3,'2018-01-04',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,2,'Use as often as required','Apply the medication after cleaning and drying the affected area',
 '2018-01-04',null,null,null,'1 packet','10',2,null,0,0,'2018-02-04','acute','Acute',null,null,'2018-03-15'),
 (3,'group2','completed','Completed','order','Order',5,3,'2018-01-04',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,2,'Use as often as required','Apply the medication after cleaning and drying the affected area',
 '2018-01-04',null,null,null,'1 packet','10',2,null,0,0,'2018-02-04','acute','Acute',null,null,'2018-03-15');

UPDATE medication_statements SET medicationRequestId = 2 WHERE id = 2;

INSERT INTO medication_request_based_on
 (id,referenceUrl,referenceId)
VALUES
 (2,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1',2);

INSERT INTO medication_request_based_on_references
 (medicationRequestId,basedOnReferenceId)
VALUES
 (3,2);

INSERT INTO medication_request_reason_codes
 (medicationRequestId,reasonCodeId)
VALUES
 (2,3),
 (3,3);

INSERT INTO medication_request_notes
 (medicationRequestId,noteId)
VALUES
 (2,2),
 (3,2);

INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder)
VALUES
  (9658218881 ,'2016-07-01 12:00:00',"Cured","Difficulty breathing","Major", "resolved","unconfirmed","environmental","3",'2016-05-01 12:00:00','2016-06-01 12:00:00',"419063004","Allergy to horse dander (disorder)","289100008","Difficulty taking deep breaths (finding)","","", '1'),
  (9658218881 ,'2016-07-01 12:00:00',"Ongoing","Dry, red and cracked skin","Major", "active","unconfirmed","medication","3",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294915005","Iodophore allergy (disorder)","702757002","Severe dry skin (finding)","","", '1');
