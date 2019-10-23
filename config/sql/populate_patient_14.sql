USE gpconnect0;

INSERT INTO adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9000000149,'2015-05-01','2015-05-01',"Cervical Smear Defaulter","");

INSERT INTO allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9000000149,"Historical",'2015-05-01','2015-05-01',"Skin allergy"),
  (9000000149,"Current",'2015-05-01','2015-05-01',"Work allergy");

INSERT INTO clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9000000149,'2015-05-01','2015-05-01',"Abdominal X-ray","No evidence of osteomyelitis.");

INSERT INTO encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9000000149,'2016-07-01','2016-07-01',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9000000149,'2016-07-01',"HIV Injection",NULL,NULL,"Dont Know");

INSERT INTO medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,dosageInstruction,quantity,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed,discontinuationReason)
VALUES
  (9000000149,"Current",'2016-07-01',"Amoxicillin 500mg capsules Supply (42) capsule(s)","3 times per day","14 days",'2019-11-19',7,"Take every 8 hours (three times a day) with or without food",NULL,NULL,NULL,NULL,"Acute",""),
  (9000000149,"Repeat",'2016-07-01',"Metformin 500mg tablets","one tablet 2 to 3 time daily","28 days",'2019-11-19',4,"Take with food and water",NULL,NULL,NULL,NULL,"Repeat",""),
  (9000000149,"Past",'2016-07-01',"Metformin 500mg tablets","one tablet 2 to 3 time daily","28 days",'2019-11-19',4,"Take with food and water",NULL,NULL,NULL,NULL,"Repeat","Patient felt queasy");

INSERT INTO observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9000000149,'2016-07-01',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9000000149,"Active",'2016-07-01','2016-07-01',"Low Back Pain","Minor","Treated daily"),
  (9000000149,"Inactive",'2016-07-01','2016-07-01',"Asthma","Major","Treated daily");

INSERT INTO referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9000000149,'2016-07-01','Dr Johnson &amp; Partners','Leeds District Nurses','Routine','Referral to local authority weight management programme<br />Waiting For Information','2016-07-25');
