USE gpconnect0_7;

INSERT INTO allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658220150 ,"Current",'2016-07-01','2016-07-01',"Work allergy");

INSERT INTO encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9658220150 ,'2015-05-01','2015-05-01',"Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)","Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials");

INSERT INTO medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,dosageInstruction,quantity,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9658220150 ,"Repeat",'2016-07-01',"Metformin 500mg tablets","one tablet 2 to 3 times daily","28 days",'2019-11-19',4,"Take with food and water",NULL,NULL,NULL,NULL,"Repeat");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658220150 ,"Active",'2015-05-01','2015-05-01',"Type II diabetes mellitus","Major","Treated daily");

INSERT INTO referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658220150 ,'2015-05-01','Airedale Hospital','Dr Johnson &amp; Partners','Routine','From Hospital out-patient for Diabetic Medicine<br />Receiving care','2016-07-25');
