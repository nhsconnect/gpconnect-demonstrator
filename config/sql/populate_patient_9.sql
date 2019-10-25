USE gpconnect0_7;

INSERT INTO adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9658220142 ,'2015-05-01','2015-05-01',"Cervical Smear Defaulter",""),
  (9658220142 ,'2016-07-01','2016-07-01',"No summary care record consent specified","No record");

INSERT INTO allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658220142 ,"Historical",'2015-05-01','2015-05-01',"Skin allergy"),
  (9658220142 ,"Historical",'2016-07-01','2016-07-01',"Beer allergy"),
  (9658220142 ,"Current",'2015-05-01','2015-05-01',"Work allergy"),
  (9658220142 ,"Current",'2016-07-01','2016-07-01',"Work allergy");

INSERT INTO clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9658220142 ,'2015-05-01','2015-05-01',"Abdominal X-ray","No evidence of osteomyelitis."),
  (9658220142 ,'2016-07-01','2016-07-01',"Curvature of spine","");

INSERT INTO encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9658220142 ,'2015-05-01','2015-05-01',"Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)","Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials"),
  (9658220142 ,'2016-07-01','2016-07-01',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9658220142 ,'2015-05-01 09:22:00',"PediaceExpiry Date: 04-Oct-2016",NULL,"DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS","DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS"),
  (9658220142 ,'2016-07-01 09:22:00',"HIV Injection",NULL,NULL,"Dont Know");

-- INSERT INTO investigations
--   (nhsNumber,sectionDate,title,details,lastUpdated)
-- VALUES
--   (9658220142 ,'2015-05-01','Long investigation',"Something wasn't found!",'2016-07-26'),
--   (9658220142 ,'2016-07-01','Long investigation',"Something wasn't found!",'2016-07-26');

INSERT INTO medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,dosageInstruction,quantity,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed,discontinuationReason)
VALUES
  (9658220142 ,"Current",'2015-05-01',"Lansoprazole 15mg gastro-resistant capsules","one tablet daily","28 days",'2019-11-19',2,"Take your doses 30 minutes before breakfast when your stomach is empty",NULL,NULL,NULL,NULL,"Acute",""),
  (9658220142 ,"Current",'2016-07-01',"Amoxicillin 500mg capsules Supply (42) capsule(s)","three times per day","14 days",'2019-11-19',7,"Take every 8 hours (three times a day) with or without food",NULL,NULL,NULL,NULL,"Acute",""),
  (9658220142 ,"Repeat",'2015-05-01',"Metformin 500mg tablets","one tablet 2 to 3 time daily","28 days",'2019-11-19',4,"Take with food and water",NULL,NULL,NULL,NULL,"Repeat",""),
  (9658220142 ,"Repeat",'2016-07-01',"Metformin 500mg tablets","one tablet 2 to 3 time daily","28 days",'2019-11-19',4,"Take with food and water",NULL,NULL,NULL,NULL,"Repeat",""),
  (9658220142 ,"Past",'2015-05-01',"Metformin 500mg tablets","one tablet 2 to 3 time daily","28 days",'2019-11-19',4,"Take with food and water",'2015-05-01',NULL,NULL,NULL,"Repeat","r9"),
  (9658220142 ,"Past",'2016-07-01',"Metformin 500mg tablets","one tablet 2 to 3 time daily","28 days",'2016-07-01',4,"Take with food and water",'2016-07-01',NULL,NULL,NULL,"Repeat","r9a");

INSERT INTO observations
  (nhsNumber,observationDate,entry,value,theRange,details)
VALUES
  (9658220142 ,'2015-05-01',"Full Health of the Nation Outcome Scale score","16","","(Added from Questionnaire)"),
  (9658220142 ,'2016-07-01',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","","(Added from Questionnaire)"),
  (9658220142 ,'2017-07-01',"ABPI (Ankle Brachial Pressure Index)","Left leg - 1","0.92 - 1.23","Annual Review"),
  (9658220142 ,'2017-07-01',"ABPI (Ankle Brachial Pressure Index)","Right leg - 1.2","0.92 - 1.23","Annual Review");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658220142 ,"Active",'2015-05-01','2015-05-01',"Type II diabetes mellitus","Major","Treated daily"),
  (9658220142 ,"Active",'2016-07-01','2016-07-01',"Low Back Pain","Minor","Treated daily"),
  (9658220142 ,"Inactive",'2015-05-01','2015-05-01',"Type II diabetes mellitus","Major","Treated daily"),
  (9658220142 ,"Inactive",'2016-07-01','2016-07-01',"Asthma","Major","Treated daily");

INSERT INTO referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658220142 ,'2015-05-01','Airedale Hospital','Dr Johnson &amp; Partners','Routine','From Hospital out-patient for Diabetic Medicine<br />Receiving care','2016-07-25'),
  (9658220142 ,'2016-07-01','Dr Johnson &amp; Partners','Leeds District Nurses','Routine','Referral to local authority weight management programme<br />Waiting For Information','2016-07-25');
