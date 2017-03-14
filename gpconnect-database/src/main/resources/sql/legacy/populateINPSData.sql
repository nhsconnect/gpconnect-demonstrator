INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9476719966,'2013-12-10 12:17:00','2013-12-10 12:17:00',"Cervical Smear Defaulter",""),
  (9476719966,'2013-12-10 12:17:00','2013-12-10 12:17:00',"No summary care record consent specified","");

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476719966,"Current",'2014-12-10 12:17:00','2015-12-11 12:17:00',"Paracetamol allergy"),
  (9476719966,"Current",'2013-12-10 12:17:00','2016-12-11 12:17:00',"Shellfish allergy - intolerance"),
  (9476719966,"Historical",'2011-12-10 12:17:00','2016-12-11 12:17:00',"H/O: drug allergy Diclofenac potassium 50mg tabletsH/O: drug allergy Diclofenac potassium 50mg tablets"),
  (9476719966,"Historical",'2010-12-10 12:17:00','2016-12-11 12:17:00',"Skin allergy"),
  (9476719966,"Historical",'2010-12-10 12:17:00','2016-12-11 12:17:00',"H/O: drug allergy Quinine sulfate 300mg tablets");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719966,'2014-12-12 12:17:00','2014-12-12 12:17:00',"Shoulder pain","In right shoulder"),
  (9476719966,'2013-12-12 12:17:00','2013-12-12 12:17:00',"Oesophageal cancer",""),
  (9476719966,'2011-12-12 12:17:00','2011-12-12 12:17:00',"Perennial rhinitis",""),
  (9476719966,'2010-12-12 12:17:00','2010-12-12 12:17:00',"Fragility # unsp osteoporosis","A lot of pain"),
  (9476719966,'2010-12-12 12:17:00','2010-12-12 12:17:00',"Osteoporosis","Osteoporosis"),
  (9476719966,'2009-12-12 12:17:00','2009-12-12 12:17:00',"Varicose eczema","Varicose eczema");

INSERT INTO gpconnect.encounters
  (nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (9476719966,'2016-04-20 00:00:01','2016-04-20 00:00:01',"Clinic - Dr Bob Ash","HBP"),
  (9476719966,'2016-04-20 00:00:01','2016-04-20 00:00:01',"Clinic - Dr Bob Ash","LBP"),
  (9476719966,'2015-04-20 00:00:01','2015-04-20 00:00:01',"Surgery Consultation - Dr Bob Ash","Mechanical low back pain");

INSERT INTO gpconnect.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9476719966,'2016-07-25 12:00:00',"FDiphtheria/Tet/Pert/Polio/Haemophil","DIPHTHERIA, TETANUS, PERTUSSIS, POLIO, HIB","Dont know","Dont Know");

INSERT INTO gpconnect.investigations
  (nhsNumber,sectionDate,title,details,lastUpdated)
VALUES
  (9476719966,'2016-07-25 12:00:00','Long investigation','Something was found!','2016-07-25 12:00:00');

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476719966,"Current","02-Apr-2016","Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3",NULL,NULL,NULL,NULL,"4"),
  (9476719966,"Repeat","14-09-2014","Colofac 135mg tablets (BGP Products Ltd)","19-11-2019","Day Duration 4","3","Acute","Issue more","Issue more","Issue more","4"),
  (9476719966,"Past","14-09-2014","Amoxicillin 500mg capsules Supply ( 42 ) capsule(s) 1 THREE TIMES A DAY","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.observations
  (nhsNumber,observationDate,entry,value,details)
VALUES
  (9476719966,'2003-05-26 12:00:00',"Percentage basophils","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719966,'2013-06-05 12:00:00',"Basophil count","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719966,'2011-07-03 12:00:00',"Percentage eosinophils","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719966,'2012-01-01 12:00:00',"Eosinophil count","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719966,'2015-03-22 12:00:00',"Percentage monocytes","0.000 and 2.000","(Added from Questionnaire)"),
  (9476719966,'2001-02-20 12:00:00',"Percentage lymphocytes","0.000 and 2.000","(Added from Questionnaire)");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476719966,"Active",'2016-07-25 12:00:00','2016-07-25 12:00:00',"Type II diabetes mellitus","Major","Treated daily");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9476719966,'2013-12-10 12:17:00','Dr An Other (GP Registrar)','Ninewells - General medicine','Routine','Depressive disorder NEC','2016-07-25 12:00:00');
