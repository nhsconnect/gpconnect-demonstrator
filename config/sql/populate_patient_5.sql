INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,manCoding,manDisplay, recorder, encounter)
VALUES
  (9658218989 ,'2018-03-14',"","","", "no known","unconfirmed","","5",'2010-07-22','2010-07-22',"716186003","No known allergy","","",'9476719923', '');

INSERT INTO gpconnect.medication_statements
 (id,lastIssueDate,encounterId,statusCode,statusDisplay,medicationId,startDate,endDate,dateAsserted,
    patientId,takenCode,takenDisplay,dosageText,dosageInstruction,lastUpdated, prescribingAgency)
VALUES
 (3,'2018-03-14',13,'active','Active',5,'2018-01-12','2020-07-12','2018-01-12',5,'unk','Unknown','Use as often as required','Apply the medication after cleaning and drying the affected area','2018-03-15', 'prescribingAgency');

INSERT INTO gpconnect.medication_reason_codes
 (id,reasonCode,reasonDisplay)
VALUES
 (4,'409002','Food allergy diet');

INSERT INTO gpconnect.medication_notes
 (id,dateWritten,authorReferenceUrl,authorId,noteText)
VALUES
 (3,'2018-01-12','https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,'Patient complains of light sensitivity'),
 (4,'2018-02-14','https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,'Patient does not like taste');
 
INSERT INTO gpconnect.medication_statement_reason_codes
 (medicationStatementId,reasonCodeId)
VALUES
 (3,4);
 
INSERT INTO gpconnect.medication_statement_notes
 (medicationStatementId,noteId)
VALUES
 (3,3),
 (3,4);
 
INSERT INTO gpconnect.medication_reason_references
 (id,referenceUrl,referenceId)
VALUES
 (2,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Observation-1',1);
 
INSERT INTO gpconnect.medication_statement_reason_references
 (medicationStatementId,reasonReferenceId)
VALUES
 (3,2);

INSERT INTO gpconnect.medication_requests
 (id,groupIdentifier,statusCode,statusDisplay,intentCode,intentDisplay,medicationId,patientId,encounterId,authoredOn,
 requesterUrl,requesterId,authorisingPractitionerId,dosageText,dosageInstruction,dispenseRequestStartDate,dispenseRequestEndDate,
 dispenseQuantityValue,dispenseQuantityUnit,dispenseQuantityText,expectedSupplyDuration,
 dispenseRequestOrganizationId,priorMedicationRequestId,numberOfRepeatPrescriptionsAllowed,numberOfRepeatPrescriptionsIssued,
 authorisationExpiryDate,prescriptionTypeCode,prescriptionTypeDisplay,statusReasonDate,statusReason,lastUpdated)
VALUES
 (4,'group3','active','Active','plan','Plan',5,5,13,'2018-01-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,1,'Use as often as required','Apply the medication after cleaning and drying the affected area',
 '2018-01-18','2018-07-04',null,null,'250ml','28',2,null,6,4,'2018-07-16','repeat','Repeat',null,null,'2018-04-15'),
 (5,'group3','active','Active','order','Order',5,5,13,'2018-01-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,1,'Use as often as required','Apply the medication after cleaning and drying the affected area',
 '2018-01-18','2018-02-15',null,null,'250ml','28',2,null,6,1,'2018-07-16','repeat','Repeat',null,null,'2018-01-15'),
 (6,'group3','active','Active','order','Order',5,5,13,'2018-01-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,1,'Use as often as required','Apply the medication after cleaning and drying the affected area',
 '2018-02-12','2018-03-12',null,null,'250ml','28',2,5,6,2,'2018-07-16','repeat','Repeat',null,null,'2018-03-15'),
 (7,'group3','active','Active','order','Order',5,5,13,'2018-01-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',1,1,'Use as often as required','Apply the medication after cleaning and drying the affected area',
 '2018-03-12','2018-04-09',null,null,'250ml','28',2,6,6,3,'2018-07-16','repeat','Repeat',null,null,'2018-03-15');

UPDATE gpconnect.medication_statements SET medicationRequestId = 4 WHERE id = 3;

INSERT INTO gpconnect.medication_request_based_on
 (id,referenceUrl,referenceId)
VALUES
 (3,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1',4);

INSERT INTO gpconnect.medication_request_based_on_references
 (medicationRequestId,basedOnReferenceId)
VALUES
 (5,3),
 (6,3),
 (7,3);
 
INSERT INTO gpconnect.medication_request_reason_codes
 (medicationRequestId,reasonCodeId)
VALUES
 (4,4),
 (5,4),
 (6,4),
 (7,4);
 
INSERT INTO gpconnect.medication_request_notes
 (medicationRequestId,noteId)
VALUES
 (4,3),
 (5,3),
 (6,3),
 (7,3),
 (4,4),
 (5,4),
 (6,4),
 (7,4);
