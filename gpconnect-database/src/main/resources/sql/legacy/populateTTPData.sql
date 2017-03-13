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
  (9476719931,"Historical",'2014-11-10 12:17:00','2016-12-11 12:17:00',"Beer allergy");

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES
  (9476719931,'2008-04-09 12:17:00','2008-04-09 12:17:00',"Abdominal X-ray","No evidence of osteomyelitis."),
  (9476719931,'2008-05-16 12:17:00','2008-05-16 12:17:00',"Curvature of spine",""),
  (9476719931,'2012-05-16 12:17:00','2012-05-16 12:17:00',"Private referral (&amp; to doctor)",""),
  (9476719931,'20-05-16 12:17:00','2008-05-16 12:17:00',"Referral to postnatal clinic",""),
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
  (9476719931,'2002-06-13 09:22:00',"HIV Injection","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2003-06-13 09:22:00',"HIV Injection","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2004-06-13 09:22:00',"HIV Injection","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2005-06-13 09:22:00',"HIV Injection","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2006-06-13 09:22:00',"MMR III","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2007-06-13 09:22:00',"MMR I","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2008-06-13 09:22:00',"MMR II","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2009-06-13 09:22:00',"Arilvax","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know"),
  (9476719931,'2012-06-13 09:22:00',"Fluenz (AstraZeneca UK Ltd)","Manufacturer : fred\r\nBatch: 1\r\nInjection Location:Left leg\r\nExpiry Date: 04-Oct-2016","Dont know","Dont Know");

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
  (nhsNumber,sectionDate,htmlPart,provider,lastUpdated)
VALUES
  (9476719931,'2016-03-03 00:00:01',"<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><td>03 Mar 2016</td><td>Airedale Hospital</td><td>Dr Johnson &amp; Partners</td><td>Routine</td><td>From Hospital out-patient for Diabetic Medicine<br />Receiving care<br /></td></tbody></table></div>","TPP",'2016-07-25 12:00:00'),
  (9476719931,'2013-04-09 00:00:01',"<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><td>09 Apr 2013</td><td>Dr Johnson &amp; Partners</td><td>Leeds District Nurses</td><td>Routine</td><td>Referral to local authority weight management programme<br />Waiting For Information</td></tbody></table></div>","TPP",'2016-07-25 12:00:00'),
  (9476719931,'2012-07-14 00:00:01',"<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><td>14 Jul 2012</td><td>Dr Johnson &amp; Partners</td><td>Leeds General Infirmary</td><td>Urgent</td><td>Back pain</td></tbody></table></div>","TPP",'2016-07-25 12:00:00');

INSERT INTO gpconnect.patientsummary
  (id,html,provider,lastUpdated)
VALUES
  (9476719931,"<div><h2>Warnings</h2><table><thead><tr><th>Start Date</th><th>Entry</th><th>Significance</th><th>Details</th></tr></thead><tbody><tr><td>23 Feb 2005</td><td>Loses temper easily</td></tr></tbody></table><h2>Key Indicators</h2><table><thead><tr><th>Start Date</th><th>Medication Item</th><th>Type</th><th>Scheduled End Date</th><th>Days Duration</th><th>Details</th></tr></thead><tbody><tr><td>20 Feb 2015</td><td>Has an end of life care plan</td></tr><tr><td>13 Jan 1999</td><td>Asthmatic</td></tr></tbody></table><h2>Active Problems and Issues</h2><table><thead><tr><th>Last Issued</th><th>Medication Item</th><th>Start Date</th><th>Review Date</th><th>Number Issued</th><th>Max Issues</th><th>Details</th></tr></thead><tbody><tr><td>23 Feb 2005</td><td>Type II diabetes mellitus</td><td>Major</td><td /></tr><tr><td>10 Apr 2000</td><td>Low back pain</td><td>Minor</td><td /></tr><tr><td>13 Jan 1999</td><td>Asthma</td><td>Major</td><td /></tr></tbody></table><h2>Current Medication Issues</h2><table><thead><tr><th>Start Date</th><th>Details</th></tr></thead><tbody><tr><td>10 Apr 2016</td><td>Paracetemol 500mg tablets - 32 tablet - take 1 or 2 4 times/day</td><td>14 Apr 2016</td><td>4</td><td /></tr><tr><td>08 Apr 2016</td><td>Metformin 500mg tablets - 42 tablet - take one 3 times/day</td><td>22 Apr 2016</td><td>14</td><td>Repeat issue</td></tr></tbody></table><h2>Current Repeat Medications</h2><table><thead><tr><th>Date</th><th>Title</th><th>Details</th></tr></thead><tbody><tr><td>01 Mar 2016</td><td>Metformin 500mg tablets - 42 tablet - take one 3 times/day</td><td>Repeat</td><td>08 Apr 2016</td><td>30 Sep 2016</td><td>2</td><td>12</td><td /></tr></tbody></table><h2>Current Allergies and Adverse Reactions</h2><table><tbody><tr><th>Start date</th><th>Details</th></tr><tr><td>10 Mar 2016</td><td>PENICILLIN VK</td></tr><tr><td>29 Mar 2016</td><td>Nurofen Express 256mg caplets (Reckitt Benckiser Healthcare (UK) Ltd)</td></tr><tr><td>29 Mar 2016</td><td>Nut allergy</td></tr></tbody></table><h2>Current Recalls</h2><table><tbody><tr><th>Recall date</th><th>Details</th></tr><tr><td>29 Jan 2017</td><td>Asthma monitoring</td></tr><tr><td>10 Sep 2016</td><td>Influenza Vaccination</td></tr></tbody></table><h2>Encounters</h2><table><tbody><tr><th>10 Dec 2014 - 12:17</th><th>Jeffrey Johnson - Dr Johnson and Partners (J12345)</th></tr><tr><td /><td>History: Family history of diabetes mellitus type II.<br />Came to nurse last week with Polyuria Very worried that he is diabetic - confirmed on blood tests. Very amenable to change of lifestyle; no other symptoms at present.<br />Examination: O/E - Diastolic BP reading 98 mmHg. , O/E - Systolic BP reading 152 mmHg.<br />Diagnosis: Type II diabetes mellitus.<br />Procedure: Wants to avoid tabs for the moment - watch TChol as well as HbA1c. Review 1/12.<br />Metformin 500mg tablets - 168 tablets - take one three times a day - Patient has read up on metformin and is keen to start.<br /></td></tr><tr><th>01 Jan 2015 - 15:47</th><th>Jeffrey Johnson - Dr Johnson and Partners (J12345)</th></tr><tr><td /><td>Type II diabetic dietary review.<br />Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials - 5 vials - use as directed.</td></tr><tr><th>04 Jul 2015 - 12:10</th><th>Thomas Turnpike - Dr Johnson and Partners (J12345)</th></tr><tr><td /><td>Contact method: Telephone<br />Care plan created: Diabetes management plan.<br />Haemoglobin A1c level - IFCC standardised 23.5 mmol/mol.</td></tr></tbody></table><h2>Recent Investigations (last 3 months)</h2><table><tbody><tr><th>12 Mar 2016</th><th>Full blood count</th></tr><tr><td /><td>Result indicator: Borderline<br />Follow-up action: Make an appointment to see doctor<br />No filing comments given.<br /><br />Date Analysed: 12/03/12<br />Time Analysed: 11:22<br />Large unstained cell: 0.20 10*9/L<br />report 7:nhs003 no errors: 0.427 L/L<br /><br /><table><tbody><tr><td>Full blood count</td><td /><td /></tr><tr><td>Haemoglobin concentration</td><td /><td>15.4 g/dL [13 - 17]</td></tr><tr><td>Total white blood count</td><td>Above range</td><td>13.1 10^9/L [4 - 11]</td></tr><tr><td>Platelet count - observation</td><td /><td>244 10^9/L [150 - 400]</td></tr><tr><td>Neutrophil count</td><td>Above range</td><td>8.5 10^9/L [2 - 7.5]</td></tr><tr><td>Lymphocyte count</td><td /><td>2.8 10^9/L [1.5 - 4]</td></tr><tr><td>Monocyte count - observation</td><td>Above range</td><td>1.1 10^9/L [0.2 - 1]</td></tr><tr><td>Eosinophil count - observation</td><td>Above range</td><td>0.6 10^9/L [0 - 0.5]</td></tr><tr><td>Basophil count</td><td /><td>0.1 10^9/L [0 - 0.1]</td></tr><tr><td>Red blood cell count</td><td>Above range</td><td>5.75 10^12/L [4.5 - 5.5]</td></tr><tr><td>Packed cell volume</td><td /><td>0.465  [0.4 - 0.5]</td></tr><tr><td>Mean cell volume</td><td /><td>80.9 fL [80 - 100]</td></tr><tr><td>Mean cell haemoglobin level</td><td>Below range</td><td>26.8 pg [27 - 32]</td></tr><tr><td>Mean cell haemoglobin concentration</td><td /><td>33.1 g/dL [32 - 36]</td></tr></tbody></table></td></tr><tr><th>23 Jan 2016</th><th>Serum TSH level</th></tr><tr><td /><td>Result indicator: Normal<br />No further action required.<br />No filing comments given.<br /><br />Clinical Information: Clinical Details: THYROXINE<br /><br /><table><tbody><tr><td>Serum TSH level</td><td /><td>1.5 miu/L [0.2 - 4]<br />TSH level is consistent with optimum replacement<br />NB above comment applies only to patients on T4 for<br />treatment of primary hypothyroidism.<br />Treatment target in CA Thyroid is TSH &lt;0.10 mU/L</td></tr></tbody></table></td></tr></tbody></table></div>","TPP",'2016-07-25 12:00:00');