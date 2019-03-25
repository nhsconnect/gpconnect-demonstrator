USE gpconnect0;

INSERT INTO allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658218903 ,"Historical",'2015-05-01','2015-05-01',"Skin allergy");

INSERT INTO medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,dosageInstruction,quantity,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9658218903 ,"Repeat",'2015-05-01',"Metformin 500mg tablets","di4","q4",'2019-11-19',4,"Take with food and water",NULL,NULL,NULL,NULL,"Repeat");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658218903 ,"Inactive",'2016-07-01','2016-07-01',"Asthma","Major","Treated daily");