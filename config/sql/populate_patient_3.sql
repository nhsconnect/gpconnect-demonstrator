INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719974,"Current",'2014-12-10 12:17:00','2016-12-11 12:17:00',"PENICILLIN VK");

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719974,"Current","14-09-2005","Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3",NULL,NULL,NULL,NULL,"4");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719974,"Active",'2016-07-25 12:00:00','2016-07-25 12:00:00',"Type II diabetes mellitus","Major","Treated daily");