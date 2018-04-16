INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476718870,"Repeat","01-05-2015","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476718870,"Repeat","01-07-2016","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476718870,"Past","01-05-2015","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476718870,"Past","01-07-2016","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");
  
INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display,manCoding,manDisplay)
VALUES
  (9476718870,'2016-07-01 12:00:00',"Cured","Sneezing, red eyes","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"21719001","Allergic rhinitis caused by pollen (disorder)","61582004","Allergic rhinitis (disorder)"),
  (9476718870,'2016-07-01 12:00:00',"Ongoing","Difficulty breathing","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy (disorder)","405720007","Allergic bronchitis (disorder)");