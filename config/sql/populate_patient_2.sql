INSERT INTO gpconnect.encounters
  (id,nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (8,9476719931,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)","Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials"),
  (9,9476719931,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action."),
  (10,9476719931,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Miss Messenger","Result : All good - No Action."),
  (11,9476719931,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Mr Pickles","Result : Hmm, something maybe wrong - Possible Action required.");

INSERT INTO gpconnect.observations
  (id,nhsNumber,observationDate,entry,value,details)
VALUES
  (8,9476719931,'2015-05-01 12:00:00',"Full Health of the Nation Outcome Scale score","16","(Added from Questionnaire)"),
  (9,9476719931,'2016-07-01 12:00:00',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)");
 
INSERT INTO gpconnect.medication_statements
 (id,lastIssueDate,encounterId,statusCode,statusDisplay,medicationId,startDate,endDate,dateAsserted,
    patientId,takenCode,takenDisplay,dosageText,dosageInstruction,lastUpdated)
VALUES
 (1,'2017-06-12',8,'active','Active',9,'2017-06-12','2018-06-12','2017-06-12',2,'unk','Unknown','Take 2 tablets every 4 hours','Take with meals','2018-03-15'),
  (4,'2017-06-12',8,'active','Active',6,'2017-06-12','2018-06-12','2017-06-12',2,'unk','Unknown','Take 2 tablets every 4 hours','Take with meals','2018-03-15'),
  (5,'2017-06-12',8,'active','Active',12,'2017-06-12','2018-06-12','2017-06-12',2,'unk','Unknown','Take 2 tablets every 4 hours','Take with meals','2018-03-15'),
  (6,'2017-06-12',8,'active','Active',14,'2017-06-12','2018-06-12','2017-06-12',2,'unk','Unknown','Take 2 tablets every 4 hours','Take with meals','2018-03-15');

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
 (1,'group1','active','Active','plan','Plan',9,2,8,'2017-06-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-06-12',null,null,'200 Tablets','60',1,null,5,0,'2018-06-12','repeat','Repeat',null,null,'2018-03-15'),
 (8,'group1','active','Active','plan','Plan',9,2,8,'2017-06-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-05-12',null,null,'200 Tablets','60',1,null,5,0,'2018-05-12','repeat','Repeat',null,null,'2018-03-15'),
 (9,'group1','active','Active','plan','Plan',9,2,8,'2017-06-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-04-12',null,null,'200 Tablets','60',1,null,5,0,'2018-04-12','repeat','Repeat',null,null,'2018-03-15'),
 (10,'group1','active','Active','plan','Plan',9,2,8,'2017-06-12',
 'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-Practitioner-1',2,2,'Take 2 tablets every 4 hours','Take with meals',
 '2017-06-12','2018-03-12',null,null,'200 Tablets','60',1,null,5,0,'2018-03-12','repeat','Repeat',null,null,'2018-03-15');
 
UPDATE gpconnect.medication_statements SET medicationRequestId = 1 WHERE id = 1;
UPDATE gpconnect.medication_statements SET medicationRequestId = 8 WHERE id = 4;
UPDATE gpconnect.medication_statements SET medicationRequestId = 9 WHERE id = 5;
UPDATE gpconnect.medication_statements SET medicationRequestId = 10 WHERE id = 6;

INSERT INTO gpconnect.medication_request_based_on
 (id,referenceUrl,referenceId)
VALUES
 (1,'https://fhir.nhs.uk/STU3/StructureDefinition/CareConnect-GPC-MedicationRequest-1',1);

INSERT INTO gpconnect.medication_request_based_on_references
 (medicationRequestId,basedOnReferenceId)
VALUES
 (1,1);

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
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay)
VALUES
  (9476719931,'2016-07-01 12:00:00',"Cured","Dalmation","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"419271008","Allergy to dog dander (disorder)","61582004","Allergic rhinitis (disorder)"),
  (9476719931,'2016-07-01 12:00:00',"Ongoing","Rash","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy (disorder)","247472004", "Weal (disorder)");

INSERT INTO gpconnect.medication_allergies
(medicationId,allergyintoleranceId)
VALUES
  (18,14);