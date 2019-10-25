USE gpconnect0_7;

INSERT INTO adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9658218873 ,'2015-05-02','2015-05-02',"Appointment Letter","Community diabetes clinic");

INSERT INTO allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658218873 ,"Historic",'1963-06-02',null,"Hayfever, allergy to pollen"),
  (9658218873 ,"Current",'2016-03-15',null,"Allergy to Penicillin, Patient experienced rash, nausea and vomiting");

INSERT INTO clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9658218873 ,'2001-06-20','2001-06-20',"Mammogram","Normal");

INSERT INTO encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9658218873 ,'2018-02-01','2018-02-01',"Care navigation","Reception signposting regarding exercise classes");

INSERT INTO immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9658218873 ,'2010-10-04',"Influenza vaccine","1","Influenza",""),
  (9658218873 ,'2013-01-18',"Influenza vaccine","1","Influenza",""),
  (9658218873 ,'2015-08-30',"Pneumovax II","1","Pneumoccocal","");

 INSERT INTO investigations
   (nhsNumber,sectionDate,title,details,lastUpdated)
 VALUES
   (9658218873 ,'2018-04-26','Lumbar spine x-ray',"Normal result",'2018-04-26');

INSERT INTO medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,dosageInstruction,quantity,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed,discontinuationReason)
VALUES
  (9658218873 ,"Current",'2018-04-25',"Tramadol 50mg capsules","one tablet every 4 to 6 hours","28 days",NULL,28,"Take 1 or 2 tablets as directed up to 4 times per day",NULL,NULL,NULL,NULL,"NHS Medication",""),
  (9658218873 ,"Repeat",'2015-05-01',"Metformin 500mg tablets","one tablet 2 to 3 times daily","28 days",NULL,NULL,"Take one tablet three times daily",'2018-03-28','2018-10-15',3,6,"Repeat",""),
  (9658218873 ,"Repeat",'2006-03-01',"Lercanidipine 20mg tablets","one tablet daily","28 days",NULL,NULL,"Take one tablet daily",'2018-03-20','2018-10-15',3,6,"Repeat",""),
  (9658218873 ,"Past",'2005-05-01',"Metformin 500mg tablets","one tablet 2 to 3 times per day","28 days",'2005-05-01',4,"Take with food and water",'2005-05-01',NULL,NULL,NULL,"Repeat","60 tablet pack discontinued"),
  (9658218873 ,"Past",'1978-09-03',"Chloramphenicol eye drops preservative free 0.5%","one drop in the affected eye every 2 hours","5 days",NULL,NULL,"1 drop in left eye four times daily for 4 weeks.",NULL,NULL,NULL,NULL,"NHS Medication","Patient reacted badly"),
  (9658218873 ,"Past",'1978-10-03',"Chloramphenicol eye drops preservative free 0.5%","one drop in the affected eye every 2 hours","5 days",NULL,NULL,"1 drop in left eye four times daily for 4 weeks.",NULL,NULL,NULL,NULL,"NHS Medication","Patient reacted badly");
 

INSERT INTO observations
  (nhsNumber,observationDate,entry,value,theRange,details)
VALUES
  (9658218873 ,'2017-05-07',"Foot Risk Classification","Left diabetic foot at low risk","Low to High Risk","Annual review"),
  (9658218873 ,'2017-05-07',"Foot Risk Classification","Right diabetic foot at low risk","Low to High Risk","Annual review");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658218873 ,"Active",'2015-05-01',null,"Type II Diabetes mellitus","Major","Diabetes type II blood glucose management"),
  /* #262 replaced a major active with a major inactive having an end date */
  (9658218873 ,"Inactive",'2006-03-01','2010-08-03',"Essential hypertension","Major",""),
  (9658218873 ,"Active",'2018-04-25',null,"Lower back pain","Minor",""),
  (9658218873 ,"Inactive",'1958-08-04','1958-10-14',"Fracture of Clavicle","Minor","Fell off bicycle"),
  (9658218873 ,"Inactive",'1978-09-03','1978-09-15',"Conjunctivitus","Minor","");

INSERT INTO referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658218873 ,'2016-07-02','Dr Johnson and Partners','Community Diabetic Clinic','Routine','Needs further support with dietary needs','2016-07-02');
