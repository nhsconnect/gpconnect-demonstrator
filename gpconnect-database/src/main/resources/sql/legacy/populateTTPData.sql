INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9476719931,'2012-12-10 12:17:00','2012-12-10 12:17:00',"Cervical Smear Defaulter",""),
  (9476719931,'2012-12-10 12:17:00','2012-12-10 12:17:00',"No summary care record consent specified","No record");

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719931,"Historical",'2014-12-10 12:17:00','2016-12-11 12:17:00',"Dog allergy"),
  (9476719931,"Historical",'2009-03-10 12:17:00','2016-12-11 12:17:00',"Grass allergy"),
  (9476719931,"Historical",'2014-11-10 12:17:00','2016-12-11 12:17:00',"Skin allergy"),
  (9476719931,"Historical",'2014-11-10 12:17:00','2016-12-11 12:17:00',"Beer allergy"),
  (9476719931,"Current",'2014-12-10 12:17:00','2016-12-11 12:17:00',"Work allergy");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719931,'2008-04-09 12:17:00','2008-04-09 12:17:00',"Abdominal X-ray","No evidence of osteomyelitis."),
  (9476719931,'2008-05-16 12:17:00','2008-05-16 12:17:00',"Curvature of spine",""),
  (9476719931,'2012-05-16 12:17:00','2012-05-16 12:17:00',"Private referral (&amp; to doctor)",""),
  (9476719931,'2007-05-16 12:17:00','2008-05-16 12:17:00',"Referral to postnatal clinic",""),
  (9476719931,'2015-05-16 12:17:00','2015-05-16 12:17:00',"Family history of substance misuse",""),
  (9476719931,'2000-05-16 12:17:00','2000-05-16 12:17:00',"Suprapubic pain","Ongoing episode");

INSERT INTO gpconnect.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9476719931,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)","Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials"),
  (9476719931,'2015-11-03 12:17:00','2016-11-03 12:17:00',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action."),
  (9476719931,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Cynthia Carlson (Practice Nurse) - Dandelion Medical Practice (D12345)","Result : Full blood count - FBC - Normal - No Action."),
  (9476719931,'2012-11-03 12:17:00','2016-11-03 12:17:00',"Brian Harding - EMIS PCS Test Practice 1","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO gpconnect.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9476719931,'2009-06-13 09:22:00',"PediaceExpiry Date: 04-Oct-2016","Dont know","DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS","DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS"),
  (9476719931,'2002-06-13 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2003-06-13 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2004-06-13 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2005-06-13 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2006-06-13 09:22:00',"MMR III","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2007-06-13 09:22:00',"MMR I","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2008-06-13 09:22:00',"MMR II","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2009-06-13 09:22:00',"Arilvax","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2012-06-13 09:22:00',"Fluenz (AstraZeneca UK Ltd)","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know");

INSERT INTO gpconnect.investigations
  (nhsNumber,sectionDate,title,details,lastUpdated)
VALUES
  (9476719931,'2016-07-26 12:00:00','Long investigation',"Something wasn't found!",'2016-07-26 12:00:00');

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719931,"Current","14-09-2005","Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3",NULL,NULL,NULL,NULL,"4"),
  (9476719931,"Current","14-09-2011","Amoxicillin 500mg capsules Supply ( 42 ) capsule(s)","19-11-2019","Day Duration 4","3",NULL,NULL,NULL,NULL,"2"),
  (9476719931,"Current","14-09-2012","Colofac 135mg tablets (BGP Products Ltd)","19-11-2019","Day Duration 4","3",NULL,NULL,NULL,NULL,"32"),
  (9476719931,"Current","14-09-2014","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476719931,"Repeat","14-09-2014","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476719931,"Past","14-09-2014","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476719931,"Past","14-09-2014","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476719931,"Past","14-09-2014","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9476719931,'2016-03-16 12:00:00',"Full Health of the Nation Outcome Scale score","16","(Added from Questionnaire)"),
  (9476719931,'2016-03-24 12:00:00',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)"),
  (9476719931,'2016-03-16 12:00:00',"Health of Nat Outc Scale item 2 - non-accidental self injury","2","(Added from Questionnaire)"),
  (9476719931,'2016-03-16 12:00:00',"Health of Nat Outcome Scale item 8 - other ment/behav probl","16","(Added from Questionnaire)"),
  (9476719931,'2016-03-16 12:00:00',"Health of Nat Outcome Scale item 4 - other ment/behav probl","16","(Added from Questionnaire)"),
  (9476719931,'2016-03-16 12:00:00',"Health of Nat Outcome Scale item 9 - other ment/behav probl","16","(Added from Questionnaire)");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719931,"Active",'2016-07-25 12:00:00','2016-07-25 12:00:00',"Type II diabetes mellitus","Major","Treated daily"),
  (9476719931,"Inactive",'2016-07-25 12:00:00','2016-07-25 12:00:00',"Type II diabetes mellitus","Major","Treated daily"),
  (9476719931,"Active",'2000-07-25 12:00:00','2016-07-25 12:00:00',"Low Back Pain","Minor","Treated daily"),
  (9476719931,"Inactive",'2003-07-25 12:00:00','2016-07-25 12:00:00',"Asthma","Major","Treated daily"),
  (9476719931,"Inactive",'2008-07-25 12:00:00','2012-07-25 12:00:00',"Disorder of hear","Major","Treated daily"),
  (9476719931,"Inactive",'2000-07-25 12:00:00','2005-07-25 12:00:00',"Low back pain","Minor","Treated daily");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9476719931,'2016-03-03 00:00:01','Airedale Hospital','Dr Johnson &amp; Partners','Routine','From Hospital out-patient for Diabetic Medicine<br />Receiving care','2016-07-25 12:00:00'),
  (9476719931,'2013-04-09 00:00:01','Dr Johnson &amp; Partners','Leeds District Nurses','Routine','Referral to local authority weight management programme<br />Waiting For Information','2016-07-25 12:00:00'),
  (9476719931,'2012-07-14 00:00:01','Dr Johnson &amp; Partners','Leeds General Infirmary','Urgent','Back pain','2016-07-25 12:00:00');
