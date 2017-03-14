INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719958,"Current",'2015-12-10 12:17:00','2015-12-11 12:17:00',"Paracetamol allergy"),
  (9476719958,"Current",'2014-12-10 12:17:00','2016-12-11 12:17:00',"Cheese allergy"),
  (9476719958,"Historical",'2014-12-10 12:17:00','2016-12-11 12:17:00',"Grass allergy"),
  (9476719958,"Historical",'2014-12-10 12:17:00','2016-12-11 12:17:00',"Skin allergy"),
  (9476719958,"Historical",'2014-12-10 12:17:00','2016-12-11 12:17:00',"Beer allergy");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Notes summary on computer","Pale - prob mildly anaemic. Pale and unwell"),
  (9476719958,'2016-11-29 12:17:00','2016-11-29 12:17:00',"This is confidential",""),
  (9476719958,'2000-11-03 12:17:00','2000-11-03 12:17:00',"","Generalised non-specific abdominal tenderness"),
  (9476719958,'2000-01-13 12:17:00','2000-01-13 12:17:00',"Never smoked tobacco",""),
  (9476719958,'1989-11-03 12:17:00','1989-11-03 12:17:00',"Trigger finger - acquired","Still coughing up some thick globules phlegm - leading again to choking"),
  (9476719958,'1988-11-03 12:17:00','1988-11-03 12:17:00',"Right bundle branch block",""),
  (9476719958,'1987-11-03 12:17:00','1987-11-03 12:17:00',"Motor vehicle traffic accidents (MVTA)","Motor vehicle traffic accidents (MVTA)");

INSERT INTO gpconnect.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-07-25 12:00:00','2016-07-25 12:00:00',"Emis Test - EMISWebCR1 50002","Referral : ."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Clinical document : This is confidential - test"),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Brian Harding - EMIS PCS Test Practice 1","Result : Full blood count - FBC - Normal - No Action."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Brian Harding - EMIS PCS Test Practice 1","Result : Blood glucose level - glucose 6.3 ? fasting, repeat fasting blood glucose. Tumour marker levels - Normal - No Action. Thyroid function test - Normal - No Action. GFR calculated abbreviated MDRD - Normal - No Action. (Non Coded Event - Lipid Profile ) - chol 6.6. Liver function test - Normal - No Action. Renal profile - Normal - No Action."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential."),
  (9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Emis Test - EMISWebCR1 50002","Comment note : - This is confidential.");

INSERT INTO gpconnect.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9476719958,'2000-06-13 09:22:00',"First DTP (triple)+polio vacc.","Manufacturer : fred<br />Batch: 1<br />Injection Location: Right arm<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719958,'2002-06-13 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location: Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know");

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719966,"Current","02-Apr-2016","Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3",NULL,NULL,NULL,NULL,"4"),
  (9476719966,"Repeat","14-09-2014","Colofac 135mg tablets (BGP Products Ltd)","19-11-2019","Day Duration 4","3","Acute","Issue more","Issue more","Issue more","4"),
  (9476719966,"Past","14-09-2014","Amoxicillin 500mg capsules Supply ( 42 ) capsule(s) 1 THREE TIMES A DAY","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9476719958,'2003-05-26 12:00:00',"Percentage basophils","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719958,'2013-06-05 12:00:00',"Basophil count","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719958,'2011-07-03 12:00:00',"Percentage eosinophils","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719958,'2012-01-01 12:00:00',"Eosinophil count","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719958,'2015-03-22 12:00:00',"Percentage monocytes","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719958,'2001-02-20 12:00:00',"Percentage lymphocytes","0.000 and 2.000","(Added from Questionnaire)");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719958,"Active",'2016-07-25 12:00:00','2016-07-25 12:00:00',"Type II diabetes mellitus","Major","Treated daily");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9476719958,'2013-12-10 12:17:00','EmisTest at SCI_Service at EMISWebCR1 50004','SCIGPService at EMISWebCR1','Routine','This is confidential','2016-08-24 12:00:00');
