USE gpconnect0_7_3;

INSERT INTO adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9658218881 ,'2016-03-08','2016-03-08',"Clinical Letter","Tier 2 MSK services");

INSERT INTO allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9658218881 ,"Current",'2000-05-04',null,"Trimethoprim - itchy rash");

INSERT INTO clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9658218881 ,'2017-11-01','2017-11-01',"Standard chest x-ray","Result normal");

INSERT INTO encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9658218881 ,'1996-08-09','1996-08-09',"Respiratory St James Hospital","Clinical Letter");

INSERT INTO immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9658218881 ,'2015-06-02',"Tetanus Vaccine","3","Tetanus Vaccine (generic)","");

INSERT INTO investigations
   (nhsNumber,sectionDate,title,details,lastUpdated)
 VALUES
   (9658218881 ,'2010-11-02','Microscopy, culture and sensitivity',"Abnormal but expected",'2000-05-06 14:00:00');

INSERT INTO medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,dosageInstruction,quantity,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9658218881 ,"Current",'1996-07-03',"Beclometasone 100 micrograms/dose breath activated inhaler","Twice daily","28 days",NULL,NULL,"2 puffs twice a day",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9658218881 ,"Current",'1996-07-03',"Salbutamol 100 mcgs/dose inhaler","1 to 2 puffs when needed","28 days",NULL,NULL,"2 puffs when required",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9658218881 ,"Current",'1996-07-03',"Cetirizine 10 mg tablets","once daily","28 days",NULL,NULL,"1 to be taken twice daily",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9658218881 ,"Repeat",'1996-07-03',"Salbutamol 100 mcgs/dose inhaler","1 to 2 puffs when needed","28 days",NULL,NULL,"Inhale 2 doses as needed",'2018-04-06','2019-04-20',1,20,"Repeat"),
  (9658218881 ,"Repeat",'1996-07-01',"Cetirizine 10 mg tablets","once daily","28 days",NULL,NULL,"1 to be taken twice daily",'2018-04-06','2019-04-20',1,20,"Repeat");

INSERT INTO observations
  (nhsNumber,observationDate,entry,value,theRange,details)
VALUES
  (9658218881 ,'2016-03-01',"Blood pressure","123/70 mmHg","90/60 - 120/80 (normal ranges)",""),
  (9658218881 ,'2018-03-11',"Oxygen saturation","98%","90 to 100%",""),
  (9658218881 ,'2018-03-11',"Body Mass Index","24.2","18-25",""),
  (9658218881 ,'2018-03-11',"FEV1","75%","Equal to or greater than 70%",""),
  (9658218881 ,'2018-03-11',"Pulse","82","60 - 100 beats per minute","");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658218881 ,"Active",'1996-07-03',null,"Asthma","Major",""),
  (9658218881 ,"Active",'2016-07-01',null,"Urticaria","Minor",""),
  (9658218881 ,"Inactive",'2010-11-02','2010-11-02',"Urinary Tract Infection","Minor","");

INSERT INTO referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658218881 ,'2016-03-01','Dr Parsons','Referral to tier 2 MSK services','Routine','Pain in buttock and leg','2016-03-01');
