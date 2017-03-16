INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9000000106,"Current",'2016-07-01 12:17:00','2016-07-01 12:17:00',"Work allergy");

INSERT INTO gpconnect.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9000000106,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)","Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials");

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9000000106,"Repeat","01-07-2016","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9000000106,"Active",'2015-05-01 12:00:00','2015-05-01 12:00:00',"Type II diabetes mellitus","Major","Treated daily");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9000000106,'2015-05-01 00:00:01','Airedale Hospital','Dr Johnson &amp; Partners','Routine','From Hospital out-patient for Diabetic Medicine<br />Receiving care','2016-07-25 12:00:00');