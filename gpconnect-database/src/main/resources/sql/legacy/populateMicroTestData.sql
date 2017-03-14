INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9476719974,'2014-12-30 12:17:00','2014-12-30 12:17:00',"Clinical letter","");

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719974,"Current",'2014-12-10 12:17:00','2016-12-11 12:17:00',"PENICILLIN VK"),
  (9476719974,"Current",'2015-12-10 12:17:00','2016-12-11 12:17:00',"Nurofen Express 256mg caplets (Reckitt Benckiser Healthcare (UK) Ltd)"),
  (9476719974,"Current",'2011-12-10 12:17:00','2016-12-11 12:17:00',"Nut allergy"),
  (9476719974,"Current",'2011-12-10 12:17:00','2014-12-11 12:17:00',"Cat allergy");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719974,'2014-12-12 12:17:00','2014-12-12 12:17:00',"Shoulder pain","In right shoulder");

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719974,"Current","14-09-2005","Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3",NULL,NULL,NULL,NULL,"4"),
  (9476719974,"Repeat","14-09-2014","Paracetamol 240mg suppositories","19-11-2019","Day Duration 4","3","Acute","Issue more","Issue more","Issue more","4"),
  (9476719974,"Past","14-09-2014","Javlor 250 mg/10ml concentrate for solution for infusion (Pierre Fabre Ltd)","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9476719974,'2016-01-04 12:00:00',"Personal History of Stroke","S","(Added from Questionnaire)"),
  (9476719974,'2016-01-04 12:00:00',"Patient histroy of TIA","Y","(Added from Questionnaire)"),
  (9476719974,'2016-01-04 12:00:00',"Transient cerebral ischaemia Attack (y/n)","S","(Added from Questionnaire)"),
  (9476719974,'2016-01-04 12:00:00',"eldercare trigger tia episode","S","(Added from Questionnaire)"),
  (9476719974,'2016-01-04 12:00:00',"Patient history of CVA","S","(Added from Questionnaire)"),
  (9476719974,'2016-04-04 12:00:00',"Flu vaccination done","S","(Added from Questionnaire)"),
  (9476719974,'2016-06-04 12:00:00',"flu vacc been done?","S","(Added from Questionnaire)"),
  (9476719974,'2016-07-04 12:00:00',"Heart Failure","S","(Added from Questionnaire)"),
  (9476719974,'2016-08-04 12:00:00',"Echocardiogram result normal/abnormal","S","(Added from Questionnaire)"),
  (9476719974,'2016-09-04 12:00:00',"sys or dia lvh on echoe","S","(Added from Questionnaire)"),
  (9476719974,'2016-10-04 12:00:00',"Referal Cardiologist","S","(Added from Questionnaire)"),
  (9476719974,'2016-11-04 12:00:00',"echocardiogram declined YN","S","(Added from Questionnaire)"),
  (9476719974,'2016-12-04 12:00:00',"AII antagonists","S","(Added from Questionnaire)");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719974,"Active",'2016-07-25 12:00:00','2016-07-25 12:00:00',"Type II diabetes mellitus","Major","Treated daily");