INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9476719974,'2016-03-08','2016-03-08',"Clinical Letter","Tier 2 MSK services");

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719974,"Current",'2000-05-04',null,"Trimethoprim - itchy rash");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719974,'2017-11-01','2017-11-01',"Standard chest x-ray","Result normal");

INSERT INTO gpconnect.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9476719974,'1996-08-09','1996-08-09',"Respiratory St James Hospital","Clinical Letter");

INSERT INTO gpconnect.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9476719974,'2015-06-02',"Tetanus Vaccine","3","Tetanus Vaccine (generic)","");

 INSERT INTO gpconnect.investigations
   (nhsNumber,sectionDate,title,details,lastUpdated)
 VALUES
   (9476719974,'2000-05-06','Microscopy, culture and sensitivity',"Abnormal but expected",'2000-05-06 14:00:00');

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719974,"Current","1991-07-03","Beclometasone 100 micrograms/dose breath activated inhaler","","","2 puffs twice a day",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9476719974,"Current","1991-07-03","Salbutamol 100 mcgs/dose inhaler","","","2 puffs when required",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9476719974,"Current","2000-05-04","Cetirizine 10 mg tablets","","","1 to be taken twice daily",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9476719974,"Repeat","1992-07-03","Salbutamol 100 mcgs/dose inhaler","","","Inhale 2 doses as needed","2018-04-06","2019-04-20","1 out of 4","20","Repeat"),
  (9476719974,"Repeat","2000-05-04","Cetirizine 10 mg tablets","","","1 to be taken twice daily","2018-04-06","2019-04-20","1 out of 4","34","Repeat");

INSERT INTO gpconnect.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9476719974,'2016-03-01 15:15:00',"Blood pressure","123/70 mmHg","");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719974,"Active",'1992-07-03',null,"Asthma","Major Active",""),
  (9476719974,"Active",'2016-07-01',null,"Urticaria","Minor Active",""),
  (9476719974,"Inactive",'2000-05-04','2000-05-11',"Urinary Tract Infection","Minor Past","");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9476719974,'2016-06-01 06:00:00','Dr Parsonss','Referral to tier 2 MSK services','Routine','Pain in buttock and leg','2016-06-01');