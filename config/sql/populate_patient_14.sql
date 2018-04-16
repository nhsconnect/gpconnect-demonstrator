INSERT INTO gpconnect.encounters
  (id,nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (5,9476718943,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO gpconnect.observations
  (id,nhsNumber,observationDate,entry,value,details)
VALUES
  (5,9476718943,'2016-07-01 12:00:00',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)");

INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay)
VALUES
  (9476718943,'2016-07-01 12:00:00',"Cured","Conjunctivitis","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"232348003","Feather allergy (disorder)","703630003","Red eye (finding)"),
  (9476718943,'2016-07-01 12:00:00',"Ongoing","Vomiting and diarrhoea","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy (disorder)","49237006","Allergic diarrhea (disorder)");