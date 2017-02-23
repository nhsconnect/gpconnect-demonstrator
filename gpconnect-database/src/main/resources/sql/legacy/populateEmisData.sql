INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric, startDate, endDate, details)
VALUES (
 9476719958,
  "Current",
  '2015-12-10 12:17:00',
  '2015-12-11 12:17:00',
  "Paracetamol allergy"
),(
 9476719958,
"Current",
  '2014-12-10 12:17:00',
  '2016-12-11 12:17:00',
  "Cheese allergy"
  ),(
 9476719958,
  "Historical",
  '2014-12-10 12:17:00',
  '2016-12-11 12:17:00',
  "Grass allergy"
  ),(
   9476719958,
  "Historical",
  '2014-12-10 12:17:00',
  '2016-12-11 12:17:00',
  "Skin allergy"
  ),(
   9476719958,
 "Historical",
  '2014-12-10 12:17:00',
  '2016-12-11 12:17:00',
  "Beer allergy"
  );

INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES (
  9476719958,'2016-11-03 12:17:00','2016-11-03 12:17:00',"Notes summary on computer","Pale - prob mildly anaemic. Pale and unwell"
  ),
  (
  9476719958,'2016-11-29 12:17:00','2016-11-29 12:17:00',"This is confidential",""
  ),
  (
  9476719958,'2000-11-03 12:17:00','2000-11-03 12:17:00',"","Generalised non-specific abdominal tenderness"
  ),
  (
  9476719958,'2000-01-13 12:17:00','2000-01-13 12:17:00',"Never smoked tobacco",""
  ),
  (
  9476719958,'1989-11-03 12:17:00','1989-11-03 12:17:00',"Trigger finger - acquired","Still coughing up some thick globules phlegm - leading again to choking"
  ),
  (
  9476719958,'1988-11-03 12:17:00','1988-11-03 12:17:00',"Right bundle branch block",""
  ),
  (
  9476719958,'1987-11-03 12:17:00','1987-11-03 12:17:00',"Motor vehicle traffic accidents (MVTA)","Motor vehicle traffic accidents (MVTA)"
  );

INSERT INTO gpconnect.encounters
  (nhsNumber, sectionDate, htmlPart, provider, lastUpdated)
VALUES (
 9476719958, '2016-10-03 12:17:00',
  "<table><h2>Encounters</h2><thead></thead><tbody><tr><th>03 Oct 2016</th><th>Emis Test - EMISWebCR1 50002</th></tr><tr><td/><td>Comment note : - This is confidential.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2016-10-03 15:47:00',
  "<table><thead><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>03 Oct 2016</th><th>Emis Test - EMISWebCR1 50002</th></tr><tr><td/><td>Referral : .</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2016-10-03 12:10:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>03 Oct 2016</th><th>Emis Test - EMISWebCR1 50002</th></tr><tr><td/><td>Clinical document : This is confidential - test.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2003-05-26 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>26 May 2003</th><th>Brian Harding - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Full blood count - FBC - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2003-05-26 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>26 May 2003</th><th>Brian Harding - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Blood glucose level - glucose 6.3 ? fasting, repeat fasting blood glucose. Tumour marker levels - Normal - No Action. Thyroid function test - Normal - No Action. GFR calculated abbreviated MDRD - Normal - No Action. (Non Coded Event - Lipid Profile ) - chol 6.6. Liver function test - Normal - No Action. Renal profile - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2003-05-26 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>26 May 2003</th><th>Brian Harding - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : (Non Coded Event - anti - HBs ) - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-06-12 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>12 Jun 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Microbiology - Seen and dealt with.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-06-11 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>11 Jun 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Plasma glucose level - Normal - No Action. Biochemical test - Normal - No Action. Thyroid function test - Normal - No Action. Cardiac enzymes - Normal - No Action. GFR calculated abbreviated MDRD - Normal - No Action. (Non Coded Event - Lipid Profile ) - Just out of normal range - Ok. Liver function test - Normal - No Action. (Non Coded Event - Renal Profile ) - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-06-11 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>11 Jun 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Full blood count - FBC - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-06-27 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>27 May 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Microbiology - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-06-25 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>25 May 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : (Non Coded Event - REJECTED BIOCHEMISTRY ) - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-05-14 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>14 May 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : Full blood count - FBC - Just out of normal range - Ok.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2002-05-14 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>14 May 2002</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Result : (Non Coded Event - REJECTED BIOCHEMISTRY) - Normal - No Action.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
),
(
 9476719958, '2000-06-13 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>13 Jun 2000</th><th>Neil Burgess - EMIS PCS Test Practice 1</th></tr><tr><td/><td>Problems : - Generalised non-specific abdominal tenderness.<br/>Examination (heading) : O/E Blood Pressure Reading.<br/>Social history : Never smoked tobacco.</td></tr></tbody></table>",
  "EMIS", '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.immunisations
  (nhsNumber, dateOfVac, vaccination, part, contents, details)
VALUES (
 9476719958,
 '2000-06-13 09:22:00',
 "First DTP (triple)+polio vacc.",
 "Manufacturer : fred
Batch: 1
Injection Location: Right arm
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
),(
 9476719958,
 '2002-06-13 09:22:00',
 "HIV Injection",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
);


INSERT INTO gpconnect.medications_html
  (nhsNumber, currentRepeatPast, startDate, medicationItem, scheduledEnd, daysDuration, details, lastIssued, reviewDate, numberIssued, maxIssued, typeMed)
VALUES (
  9476719966,"Current","02-Apr-2016", "Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3", NULL,NULL, NULL, NULL,"4"
),(
  9476719966,"Repeat","14-09-2014", "Colofac 135mg tablets (BGP Products Ltd)","19-11-2019","Day Duration 4","3","Acute","Issue more", "Issue more", "Issue more","4"
),(
  9476719966,"Past","14-09-2014", "Amoxicillin 500mg capsules Supply ( 42 ) capsule(s) 1 THREE TIMES A DAY","19-11-2019","Day Duration 4","3", "Issue more","Issue more", "Issue more", "Issue more","4"
);

INSERT INTO gpconnect.observations
    (nhsNumber, observationDate, entry, value, details)
VALUES (
 9476719958,'2003-05-26 12:00:00',"Percentage basophils","0.000 and 2.000","(Added from Questionnaire)"
 ),(
 9476719958,'2013-06-05 12:00:00',"Basophil count","0.000 and 2.000","(Added from Questionnaire)"
),(
 9476719958,'2011-07-03 12:00:00',"Percentage eosinophils","0.000 and 2.000","(Added from Questionnaire)"
),(
 9476719958,'2012-01-01 12:00:00',"Eosinophil count","0.000 and 2.000","(Added from Questionnaire)"
),(
 9476719958,'2015-03-22 12:00:00',"Percentage monocytes","0.000 and 2.000","(Added from Questionnaire)"
),(
 9476719958,'2001-02-20 12:00:00',"Percentage lymphocytes","0.000 and 2.000","(Added from Questionnaire)"
);


INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive, startDate,endDate,entry,significance,details)
VALUES (
  9476719958,
  "Active",
  '2016-07-25 12:00:00',
 '2016-07-25 12:00:00',
  "Type II diabetes mellitus",
  "Major",
  "Treated daily"
  );

INSERT INTO gpconnect.referrals
  (nhsNumber, sectionDate, htmlPart, provider, lastUpdated)
VALUES (
 9476719958, '2013-12-10 12:17:00',
 "<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><tr><td>03 Oct 2016</td><td>EmisTest at SCI_Service at EMISWebCR1 50004</td><td>SCIGPService at EMISWebCR1</td><td>Routine</td><td>This is confidential<br /></td></tr></tbody></table></div>",
  "EMIS",
  '2016-08-24 12:00:00'
);

INSERT INTO gpconnect.patientsummary
  (id, html, provider, lastUpdated)
VALUES (
 9476719958,
  "<div><h2>Active Problems and Issues</h2><table><thead><tr><th>Start Date</th><th>Entry</th><th>Significance</th><th>Details</th></tr></thead><tbody><tr><td>03 Oct 2016</td><td>This is confidential</td><td>Major</td><td><br/>Episodicity :  FIRST</td></tr><tr><td>29 Sep 2016</td><td>This is confidential</td><td>Major</td><td><br/>Episodicity :  FIRST</td></tr></tbody></table><h2>Current Medication Issues</h2><table><thead><tr><th>Start Date</th><th>Medication Item</th><th>Type</th><th>Scheduled End Date</th><th>Days Duration</th><th>Details</th></tr></thead><tbody></tr><tr><td>03 Oct 2016</td><td>This is confidential - 100 tablet - Two At On set Of Attack Then One Every 4 Hours When Necessary,  Max 6 In 24 Hours</td><td>20 Oct 2016</td><td>17</td><td/></tr></tbody></table><h2>Current Repeat Medications</h2><table><thead><tr><th>Last Issued</th><th>Medication Item</th><th>Start Date</th><th>Review Date</th><th>Number Issued</th><th>Max Issues</th><th>Details</th></tr></thead><tbody><tr><td>03 Oct 2016</td><td>This is confidential - 50 gram - Apply Up To Three Times A Day</td><td>03 Oct 2016</td><td>31 Oct 2016</td><td>1</td><td/><td/></tr></tbody></table><h2>Current Allergies and Adverse Reactions</h2><table><thead><tr><th>Start Date</th><th>Details</th></tr></thead><tbody><tr><td>03 Oct 2016</td><td>This is confidential</td></tr></tbody></table><h2>Current Allergies and Adverse Reactions</h2><table><thead><tr><th>Date</th><th>Title</th><th>Details</th></tr></thead><tbody><tr><td>03 Oct 2016</td><td>This is confidential</td></tr></tbody></table><h2>Encounters</h2><table><tbody><tr><td>03 Oct 2016</td><td>Emis Test - EMISWebCR1 50002</td></tr><tr><td/><td>Comment note :  This is confidential - This is confidential.</td></tr><tr><td>03 Oct 2016</td><td>Emis Test - EMISWebCR1 50002</td></tr><tr><td/><td>Referral : This is confidential.</td></tr><tr><td>03 Oct 2016</td><td>Emis Test - EMISWebCR1 50002</td></tr><tr><td/><td>Clinical document :  This is confidential - test.</td></tr></tbody></table><h2>Current Recalls</h2>There are no current recalls in the patient's record</div>",
  "EMIS",
  '2016-08-24 12:00:00'
);
