USE gpconnect0;

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
  (9658218881 ,"Current",'1996-07-03',"Beclometasone 100 micrograms/dose breath activated inhaler","di3","q3",NULL,NULL,"2 puffs twice a day",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9658218881 ,"Current",'1996-07-03',"Salbutamol 100 mcgs/dose inhaler","di3a","q3a",NULL,NULL,"2 puffs when required",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9658218881 ,"Current",'1996-07-03',"Cetirizine 10 mg tablets","di3b","q3b",NULL,NULL,"1 to be taken twice daily",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9658218881 ,"Repeat",'1996-07-03',"Salbutamol 100 mcgs/dose inhaler","di3c","q3c",NULL,NULL,"Inhale 2 doses as needed",'2018-04-06','2019-04-20',1,20,"Repeat"),
  (9658218881 ,"Repeat",'1996-07-01',"Cetirizine 10 mg tablets","di3d","q3d",NULL,NULL,"1 to be taken twice daily",'2018-04-06','2019-04-20',1,20,"Repeat");

INSERT INTO observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9658218881 ,'2016-03-01',"Blood pressure","123/70 mmHg","");

INSERT INTO problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9658218881 ,"Active",'1996-07-03',null,"Asthma","Major Active",""),
  (9658218881 ,"Active",'2016-07-01',null,"Urticaria","Minor Active",""),
  (9658218881 ,"Inactive",'2010-11-02','2010-11-02',"Urinary Tract Infection","Minor Past","");

INSERT INTO referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9658218881 ,'2016-03-01','Dr Parsons','Referral to tier 2 MSK services','Routine','Pain in buttock and leg','2016-03-01');