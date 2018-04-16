INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719958,"Historical",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Skin allergy");

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719958,"Repeat","01-05-2015","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719958,"Inactive",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Asthma","Major","Treated daily");
  
  
  INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay)
VALUES
  (9476719958,'2016-07-01 12:00:00',"Cured","Allergic rhinitis","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"21719001","Allergic rhinitis caused by pollen (disorder)","61582004","Allergic rhinitis (disorder)"),
  (9476719958,'2016-07-01 12:00:00',"Ongoing","Anaphylaxis","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"293585002","Salicylate allergy (disorder)","39579001","Anaphylaxis (disorder)");