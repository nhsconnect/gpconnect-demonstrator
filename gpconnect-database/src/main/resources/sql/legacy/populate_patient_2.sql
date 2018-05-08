INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9476719931,'2015-04-22','2015-04-22',"Appointment Letter","Community diabetes clinic");

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719931,"Historic",'1963-06-02',null,"Hayfever, allergy to pollen"),
  (9476719931,"Current",'2016-03-15',null,"Allergy to Penicillin, Patient experienced rash, nausea and vomiting");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719931,'2001-06-20','2001-06-20',"Mammogram","Normal");

INSERT INTO gpconnect.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9476719931,'2018-02-01','2018-02-01',"Care navigation","Reception signposting regarding exercise classes");

INSERT INTO gpconnect.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9476719931,'2010-10-04',"Influenza vaccine","1","Influenza",""),
  (9476719931,'2013-01-18',"Influenza vaccine","1","Influenza",""),
  (9476719931,'2015-08-30',"Pneomovax II","1","Pneumoccocal","");

 INSERT INTO gpconnect.investigations
   (nhsNumber,sectionDate,title,details,lastUpdated)
 VALUES
   (9476719931,'2018-04-26','Lumbar spine x-ray',"Normal result",'2018-04-26');

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719931,"Current","2018-04-25","Tramadol 50mg capsules","","28 days","Take 1 or 2 tablets as directed up to 4 times per day",NULL,NULL,NULL,NULL,"NHS Medication"),
  (9476719931,"Repeat","2015-05-01","Metformin 500mg tablets","","","Take one tablet three times daily","2018-03-28","2018-10-15","3 issues","6 issues",""),
  (9476719931,"Repeat","2006-03-01","Lercanidipine 20mg tablets","","","Take one tablet daily","2018-03-28","2018-10-15","3 issues","6 issues",""),
  (9476719931,"Past","1978-09-03","Chloramphenicol eye drops preservative free 0.5%","","","1 drop in left eye four times daily for 4 weeks.",'','','','',"NHS Medication");

INSERT INTO gpconnect.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9476719931,'2017-05-07',"Foot Risk Classification","Left diabetic foot at low risk","Annual review"),
  (9476719931,'2017-05-07',"Foot Risk Classification","Right diabetic foot at low risk","Annual review");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719931,"Active",'2015-05-01',null,"Type II Diabetes mellitus","Major Active","Diabetes type II blood glucose management"),
  (9476719931,"Active",'2006-03-01',null,"Essential hypertension","Major Active",""),
  (9476719931,"Active",'2018-04-25',null,"Lower back pain","Minor Active",""),
  (9476719931,"Inactive",'1958-08-04',null,"Fracture of Clavicle","Minor Past","Fell off bicycle"),
  (9476719931,"Inactive",'1978-09-03',null,"Conjunctivitus","Minor Past","");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9476719931,'2016-07-02','Dr Johnson and Partners','Community Diabetic Clinic','Routine','Needs further support with dietary needs','2016-07-02');