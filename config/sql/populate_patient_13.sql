INSERT INTO gpconnect0.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9658219691 ,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Cervical Smear Defaulter","");

INSERT INTO gpconnect0.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658219691 ,"Historical",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Skin allergy"),
  (9658219691 ,"Current",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Work allergy");

INSERT INTO gpconnect0.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9658219691 ,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Abdominal X-ray","No evidence of osteomyelitis.");

INSERT INTO gpconnect0.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9658219691 ,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO gpconnect0.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9658219691 ,'2016-07-01 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know");

INSERT INTO gpconnect0.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9658219691 ,"Current","01-07-2016","Amoxicillin 500mg capsules Supply (42) capsule(s)","19-11-2019","7 /7","Take every 8 hours (three times a day) with or without food",NULL,NULL,NULL,NULL,"Acute"),
  (9658219691 ,"Repeat","01-07-2016","Metformin 500mg tablets","19-11-2019","4 /7","Take with food and water","Issue more","Issue more","Issue more","Issue more","Repeat"),
  (9658219691 ,"Past","01-07-2016","Metformin 500mg tablets","19-11-2019","4 /7","Take with food and water","Issue more","Issue more","Issue more","Issue more","Repeat");

INSERT INTO gpconnect0.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9658219691 ,'2016-07-01 12:00:00',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)");

INSERT INTO gpconnect0.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658219691 ,"Active",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Low Back Pain","Minor","Treated daily"),
  (9658219691 ,"Inactive",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Asthma","Major","Treated daily");

INSERT INTO gpconnect0.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658219691 ,'2016-07-01 00:00:01','Dr Johnson &amp; Partners','Leeds District Nurses','Routine','Referral to local authority weight management programme<br />Waiting For Information','2016-07-25 12:00:00');