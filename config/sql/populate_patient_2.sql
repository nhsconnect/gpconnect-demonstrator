INSERT INTO gpconnect.medication_statements
 (id,lastIssueDate,encounterId,statusCode,statusDisplay,medicationId,startDate,endDate,dateAsserted,
    patientId,takenCode,takenDisplay,dosageText,dosageInstruction,lastUpdated, warningCode)
VALUES
 (1,'2017-06-12',8,'active','Active',9,'2017-06-12','2018-06-12','2017-06-12',2,'unk','Unknown','Use 1-4cm each time you use Ibuprofen Gel.','Apply the gel as a thin layer over the affected area.','2018-03-15', '	confidential-items'),
  (4,'2018-01-01',8,'completed','Completed',6,'2018-01-01',null,'2018-01-01',2,'unk','Unknown','Take 2-3 times a day','Take without food','2018-03-15', '	confidential-items'),
  (5,'2018-03-01',8,'completed','Completed',12,'2018-03-01',null,'2018-03-01',2,'unk','Unknown','Use as often as required','Clean and dry the affected area before use','2018-03-15', '	confidential-items'),
  (6,'2018-02-12',8,'completed','Completed',14,'2018-02-12',null,'2018-02-12',2,'unk','Unknown','Take 2 tablets every 8 hours','Take with a full glass of water','2018-03-15', '	confidential-items');

INSERT INTO gpconnect.medication_reason_codes
 (id,reasonCode,reasonDisplay)
VALUES
 (1,'241006','Epilepsia partialis continua'),
 (2,'297009','Acute myringitis');
 
INSERT INTO gpconnect.medication_reason_references
 (id,referenceUrl,referenceId)
VALUES
 (1,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Observation-1',8);
 
INSERT INTO gpconnect.medication_notes
 (id,dateWritten,authorReferenceUrl,authorId,noteText)
VALUES
 (1,'2017-06-12','https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,'medication note');
 
INSERT INTO gpconnect.medication_statement_reason_codes
 (medicationStatementId,reasonCodeId)
VALUES
 (1,1),
 (1,2);

INSERT INTO gpconnect.medication_statement_reason_references
 (medicationStatementId,reasonReferenceId)
VALUES
 (1,1);
 
INSERT INTO gpconnect.medication_statement_notes
 (medicationStatementId,noteId)
VALUES
 (1,1);

INSERT INTO gpconnect.medication_requests
 (id,groupIdentifier,statusCode,statusDisplay,intentCode,intentDisplay,medicationId,patientId,encounterId,authoredOn,
 requesterUrl,requesterId,authorisingPractitionerId,dosageText,dosageInstruction,dispenseRequestStartDate,dispenseRequestEndDate,
 dispenseQuantityValue,dispenseQuantityUnit,dispenseQuantityText,expectedSupplyDuration,
 dispenseRequestOrganizationId,priorMedicationRequestId,numberOfRepeatPrescriptionsAllowed,numberOfRepeatPrescriptionsIssued,
 authorisationExpiryDate,prescriptionTypeCode,prescriptionTypeDisplay,statusReasonDate,statusReason,lastUpdated)
VALUES
 (1,'group1','active','Active','plan','Plan',9,2,8,'2017-07-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-06-12',null,null,'200 Tablets','60',1,null,5,0,'2018-06-12','repeat','Repeat',null,null,'2018-03-15'),
 (8,'group1','active','Active','order','Order',9,2,8,'2017-07-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-05-12',null,null,'200 Tablets','60',1,null,5,0,'2018-05-12','repeat','Repeat',null,null,'2018-03-15'),
 (9,'group1','active','Active','order','Order',9,2,8,'2017-07-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-04-12',null,null,'200 Tablets','60',1,null,5,0,'2018-04-12','repeat','Repeat',null,null,'2018-03-15'),
 (10,'group1','active','Active','order','Order',9,2,8,'2017-07-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-03-12',null,null,'200 Tablets','60',1,null,5,0,'2018-03-12','repeat','Repeat',null,null,'2018-03-15'),
 (11,'one','completed','Complete','plan','Plan',6,2,9,'2016-06-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2-3 times a day','Take without food',
 '2017-06-12',null,null,null,'200 Tablets','60',1,null,5,0,'2018-04-12','acute','Acute',null,null,'2018-03-15'),
 (12,'two','completed','Complete','plan','Plan',12,2,10,'2015-05-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Use as often as required','Clean and dry the affected area before use',
 '2017-06-12',null,null,null,'200 Tablets','60',1,null,5,0,'2018-04-12','acute','Acute',null,null,'2018-03-15'),
 (13,'three','completed','Complete','plan','Plan',14,2,11,'2014-04-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 8 hours','Take with a full glass of water',
 '2017-06-12',null,null,null,'200 Tablets','60',1,null,5,0,'2018-04-12','acute','Acute',null,null,'2018-03-15');
 
UPDATE gpconnect.medication_statements SET medicationRequestId = 1 WHERE id = 1;
UPDATE gpconnect.medication_statements SET medicationRequestId = 11 WHERE id = 4;
UPDATE gpconnect.medication_statements SET medicationRequestId = 12 WHERE id = 5;
UPDATE gpconnect.medication_statements SET medicationRequestId = 13 WHERE id = 6;

INSERT INTO gpconnect.medication_request_based_on
 (id,referenceUrl,referenceId)
VALUES
 (1,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1',1);

INSERT INTO gpconnect.medication_request_based_on_references
 (medicationRequestId,basedOnReferenceId)
VALUES
 (1,1),
 (8,1),
 (9,1),
 (10,1);

INSERT INTO gpconnect.medication_request_reason_codes
 (medicationRequestId,reasonCodeId)
VALUES
 (1,1),
 (1,2);

INSERT INTO gpconnect.medication_request_reason_references
 (medicationRequestId,reasonReferenceId)
VALUES
 (1,1);
 
INSERT INTO gpconnect.medication_request_notes
 (medicationRequestId,noteId)
VALUES
 (1,1);


 INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay, recorder, encounter)
VALUES
  (9476719931,'2016-07-01 12:00:00',"Cured","Dalmation","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"419271008","Allergy to dog dander (disorder)","61582004","Allergic rhinitis (disorder)", '9476719931', '11'),
  (9476719931,'2016-07-01 12:00:00',"Ongoing","Rash","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy (disorder)","247472004", "Weal (disorder)", '9476719931', '');

INSERT INTO gpconnect.medication_allergies
(medicationId,allergyintoleranceId, patientNhsnumber)
VALUES
  (18,14,9476719931);