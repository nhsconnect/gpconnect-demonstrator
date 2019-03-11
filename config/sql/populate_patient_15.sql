INSERT INTO gpconnect0.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9658220169 ,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Cervical Smear Defaulter",""),
  (9658220169 ,'2016-07-01 12:17:00','2016-07-01 12:17:00',"No summary care record consent specified","No record");

INSERT INTO gpconnect0.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658220169 ,"Historical",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Skin allergy"),
  (9658220169 ,"Historical",'2016-07-01 12:17:00','2016-07-01 12:17:00',"Beer allergy"),
  (9658220169 ,"Current",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Work allergy"),
  (9658220169 ,"Current",'2016-07-01 12:17:00','2016-07-01 12:17:00',"Work allergy");

INSERT INTO gpconnect0.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9658220169 ,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Abdominal X-ray","No evidence of osteomyelitis."),
  (9658220169 ,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Curvature of spine","");

INSERT INTO gpconnect0.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9658220169 ,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)","Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials"),
  (9658220169 ,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO gpconnect0.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9658220169 ,'2015-05-01 09:22:00',"PediaceExpiry Date: 04-Oct-2016","Dont know","DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS","DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS"),
  (9658220169 ,'2016-07-01 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know");

-- INSERT INTO gpconnect0.investigations
--   (nhsNumber,sectionDate,title,details,lastUpdated)
-- VALUES
--   (9658220169 ,'2015-05-01 12:00:00','Long investigation',"Something wasn't found!",'2016-07-26 12:00:00'),
--   (9658220169 ,'2016-07-01 12:00:00','Long investigation',"Something wasn't found!",'2016-07-26 12:00:00');

INSERT INTO gpconnect0.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9658220169 ,"Current","01-05-2015","Lansoprazole 15mg gastro-resistant capsules","19-11-2019","2 /7","Take your doses 30 minutes before breakfast when your stomach is empty",NULL,NULL,NULL,NULL,"Acute"),
  (9658220169 ,"Current","01-07-2016","Amoxicillin 500mg capsules Supply (42) capsule(s)","19-11-2019","7 /7","Take every 8 hours (three times a day) with or without food",NULL,NULL,NULL,NULL,"Acute"),
  (9658220169 ,"Repeat","01-05-2015","Metformin 500mg tablets","19-11-2019","4 /7","Take with food and water","Issue more","Issue more","Issue more","Issue more","Repeat"),
  (9658220169 ,"Repeat","01-07-2016","Metformin 500mg tablets","19-11-2019","4 /7","Take with food and water","Issue more","Issue more","Issue more","Issue more","Repeat"),
  (9658220169 ,"Past","01-05-2015","Metformin 500mg tablets","19-11-2019","4 /7","Take with food and water","Issue more","Issue more","Issue more","Issue more","Repeat"),
  (9658220169 ,"Past","01-07-2016","Metformin 500mg tablets","19-11-2019","4 /7","Take with food and water","Issue more","Issue more","Issue more","Issue more","Repeat");

INSERT INTO gpconnect0.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9658220169 ,'2015-05-01 12:00:00',"Full Health of the Nation Outcome Scale score","16","(Added from Questionnaire)"),
  (9658220169 ,'2016-07-01 12:00:00',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)");

INSERT INTO gpconnect0.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658220169 ,"Active",'2015-05-01 12:00:00','2015-05-01 12:00:00',"Type II diabetes mellitus","Major","Treated daily"),
  (9658220169 ,"Active",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Low Back Pain","Minor","Treated daily"),
  (9658220169 ,"Inactive",'2015-05-01 12:00:00','2015-05-01 12:00:00',"Type II diabetes mellitus","Major","Treated daily"),
  (9658220169 ,"Inactive",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Asthma","Major","Treated daily");

INSERT INTO gpconnect0.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658220169 ,'2015-05-01 00:00:01','Airedale Hospital','Dr Johnson &amp; Partners','Routine','From Hospital out-patient for Diabetic Medicine<br />Receiving care','2016-07-25 12:00:00'),
  (9658220169 ,'2016-07-01 00:00:01','Dr Johnson &amp; Partners','Leeds District Nurses','Routine','Referral to local authority weight management programme<br />Waiting For Information','2016-07-25 12:00:00');