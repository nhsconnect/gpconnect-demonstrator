USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder, warningCode)
VALUES
  (9690938126,'2016-07-01 12:00:00',"Cured","Conjunctivitis","Major", "resolved","unconfirmed","environmental","17",'2016-05-01 12:00:00','2016-06-01 12:00:00',"232348003","Feather allergy (disorder)","703630003","Red eye (finding)","","", '2','confidential-items'),
  (9690938126,'2016-07-01 12:00:00',"Ongoing","Vomiting and diarrhoea","Major", "active","unconfirmed","medication","17",'2016-05-01 12:00:00','2016-06-01 12:00:00',"29476003","Biphasic insulin allergy (disorder)","49237006","Allergic diarrhea (disorder)","","", '2','confidential-items');
