INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate,adminDate,Entry,Details)
VALUES
  (9476718943,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Cervical Smear Defaulter","");

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric,startDate,endDate,details)
VALUES
  (9476718943,"Historical",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Skin allergy"),
  (9476718943,"Current",'2015-05-01 12:17:00','2015-05-01 12:17:00',"Work allergy");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476718943,'2015-05-01 12:17:00','2015-05-01 12:17:00',"Abdominal X-ray","No evidence of osteomyelitis.");

INSERT INTO gpconnect.encounters
  (id,nhsNumber,sectionDate,encounterDate,title,details)
VALUES
  (5,9476718943,'2016-07-01 12:17:00','2016-07-01 12:17:00',"Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)","Result : Full blood count - FBC - Normal - No Action.");

INSERT INTO gpconnect.immunisations
  (nhsNumber,dateOfVac,vaccination,part,contents,details)
VALUES
  (9476718943,'2016-07-01 09:22:00',"HIV Injection","Manufacturer : fred<br />Batch: 1<br />Injection Location:Left leg<br />Expiry Date: 04-Oct-2016","Dont know","Dont Know");

INSERT INTO gpconnect.medications_html
  (nhsNumber,currentRepeatPast,startDate,medicationItem,scheduledEnd,daysDuration,details,lastIssued,reviewDate,numberIssued,maxIssues,typeMed)
VALUES
  (9476718943,"Current","01-07-2016","Amoxicillin 500mg capsules Supply (42) capsule(s)","19-11-2019","Day Duration 4","3",NULL,NULL,NULL,NULL,"2"),
  (9476718943,"Repeat","01-07-2016","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4"),
  (9476718943,"Past","01-07-2016","Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more","Issue more","Issue more","4");

INSERT INTO gpconnect.observations
  (id,nhsNumber,observationDate,entry,value,details)
VALUES
  (5,9476718943,'2016-07-01 12:00:00',"Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour","0","(Added from Questionnaire)");

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive,startDate,endDate,entry,significance,details)
VALUES
  (9476718943,"Active",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Low Back Pain","Minor","Treated daily"),
  (9476718943,"Inactive",'2016-07-01 12:00:00','2016-07-01 12:00:00',"Asthma","Major","Treated daily");

INSERT INTO gpconnect.referrals
  (nhsNumber,sectionDate,referral_from,referral_to,priority,details,lastUpdated)
VALUES
  (9476718943,'2016-07-01 00:00:01','Dr Johnson &amp; Partners','Leeds District Nurses','Routine','Referral to local authority weight management programme<br />Waiting For Information','2016-07-25 12:00:00');
  
INSERT INTO gpconnect.allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,coding,display)
VALUES
  (9476718943,'2016-07-01 12:00:00',"Cured","Conjunctivitis","Major", "resolved","unconfirmed","environmental","2",'2016-05-01 12:00:00','2016-06-01 12:00:00',"232348003","Feather allergy (disorder)"),
  (9476718943,'2016-07-01 12:00:00',"Ongoing","Vomiting and diarrhoea","Major", "active","unconfirmed","medication","1",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy (disorder)");