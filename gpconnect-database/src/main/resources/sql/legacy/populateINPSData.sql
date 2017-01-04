INSERT INTO gpconnect.adminitems
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><table><thead><tr><th>Date</th><th>Entry</th><th>Details</th></tr></thead><tbody><tr><td /><td>Cervical Smear Defaulter</td><td></td></tr><tr><td /><td>No summary care record consent specified</td><td></td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.allergies
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><h2>Non-Drug</h2><table><thead><tr><th>Date</th><th>Description</th><th>Who</th></tr></thead><tbody><tr><td>28-Nov-2015</td><td>Shellfish allergy - intolerance</td><td>Dr Tim Torbay</td></tr></tbody></table><h2>Drug</h2><table><thead><tr><th>Date</th><th>Description</th><th>Reaction</th><th>Severity</th><th>Certainty</th><th>Who</th></tr></thead><tbody><tr><td>15-Dec-2015</td><td>Paracetamol 500mg tablets</td><td>Intolerance</td><td>Mild</td><td>Possible</td><td>Dr Tim Torbay</td></tr><tr><td>13-Aug-2014</td><td>H/O: drug allergy Diclofenac potassium 50mg tablets</td><td>Adverse</td><td>Severe</td><td>Likely</td><td>Dr Tim Torbay</td></tr><tr><td>13-Aug-2014</td><td>H/O: drug allergy Quinine sulfate 300mg tablets</td><td>Adverse</td><td>Severe</td><td>Likely</td><td>Dr Tim Torbay</td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.clinicalitems
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><table><thead><tr><th>Date</th><th>Term</th><th>Details</th></tr></thead><tbody><tr><td>13-Aug-2014</td><td>Shoulder pain</td><td>In right shoulder</td></tr><tr><td>16-Sep-2013</td><td>Oesophageal cancer</td><td /></tr><tr><td>14-Jan-2011</td><td>Perennial rhinitis</td><td /></tr><tr><td>12-Aug-2010</td><td>Fragility # unsp osteoporosis</td><td /></tr><tr><td>12-Aug-2010</td><td>Osteoporosis</td><td /></tr><tr><td>04-Sep-2009</td><td>Varicose eczema</td><td /></tr><tr><td>18-May-2009</td><td>Fungal nail infection</td><td>On left hand</td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.encounters
  (nhsNumber, sectionDate, htmlPart, provider, lastUpdated)
VALUES (
  9000000041, '2016-04-20 00:00:01',
  "<table><thead><tr><th>20-Apr-2016</th><th>Clinic - Dr Bob Ash</th></tr></thead><tbody><tr><td></td><td><div>BP 120 / 80 at 15:20:00 taken Sitting Cuff: Standard O/E - blood pressure reading</div><div>Repeat Cetirizine 10mg tablets Last issued: 01-Mar-2016 maximum 4 allowed Supply ( 7 ) tablet TAKE 1 A DAY FOR UP TO 7 DAYS</div><div>Issue 1 Cetirizine 10mg tablets Supply ( 7 ) tablet TAKE 1 A DAY FOR UP TO 7 DAYS</div></td></tr></tbody></table>",
  "INPS", '2016-07-25 12:00:00'
),
(
  9000000041, '2013-06-02 00:00:01',
  "<table><thead><tr><th>02-Jun-2013</th><th>Surgery Consultation - Dr Bob Ash</th></tr></thead><tbody><tr><td/><td><div>BP 120 / 72 at: 02-Jun-2013 09:56:00 taken Sitting from Left Cuff: Standard O/E - blood pressure reading</div><div>O/E - pulse rate 79 beats/min</div></td></tr></tbody></table>",
  "INPS", '2016-07-25 12:00:00'
),
(
  9000000041, '2010-09-02 00:00:01',
  "<table><thead><tr><th>02-Sep-2010</th><th>Surgery Consultation - Dr Bob Ash</th></tr></thead><tbody><tr><td/><td><div>Mechanical low back pain</div></td></tr></tbody></table>",
  "INPS", '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.immunisations
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><table><thead><tr><th>Date</th><th>Vaccination</th><th>Part</th><th>Contents</th><th>Details</th></tr></thead><tbody><tr><td>25-Mar-1999</td><td>Diphtheria/Tet/Pert/Polio/Haemophil</td><td>1</td><td>DIPHTHERIA, TETANUS, PERTUSSIS, POLIO, HIB</td><td>Given</td></tr><tr><td>25-Mar-1999</td><td>Tetanus</td><td>B</td><td /><td>Given, Due 11/06/2003</td></tr><tr><td>25-Jul-1997</td><td>MMR (Measles/Mumps/Rubella)</td><td /><td /><td>Refused</td></tr><tr><td>13-Apr-1994</td><td>Tetanus</td><td>B</td><td /><td>Given, Due 23/05/1999</td></tr><tr><td>25-Jul-1992</td><td>BCG</td><td>0</td><td /><td>Given</td></tr><tr><td>18-Apr-1991</td><td>Rubella</td><td>0</td><td /><td>Given</td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.investigations
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><table><thead><tr><th>Date</th><th>Report</th></tr></thead><tbody><tr><td>21-May-2015</td><td>Score:   12   Negative Method:  3RT  for:  Asthma</td></tr><tr><td>19-Nov-2014</td><td>ECG normal Dr Janet Outside Practice</td></tr><tr><td>19-Nov-2014</td><td>Thyroid hormone tests = 0</td></tr><tr><td>19-Nov-2014</td><td>Prostate specific antigen = 0.6</td></tr><tr><td>10-Oct-2014</td><td>Hb estimation = 14.9 </td></tr>					<tr><td>10-Oct-2014</td><td>Serum cholesterol = 4.9 </td></tr>				<tr><td>10-Oct-2014</td><td>Serum triglycerides normal = 0.74</td></tr>					<tr><td>10-Oct-2014</td><td>Blood Glucose  = 5.4</td></tr>				<tr><td>10-Oct-2014</td><td>Serum creatinine NOS = 93</td></tr>				<tr><td>10-Oct-2014</td><td>Serum urea level = 5.1</td></tr>				<tr><td>10-Oct-2014</td><td>Serum potassium = 3.8</td></tr>				<tr><td>10-Oct-2014</td><td>Serum sodium = 141</td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.medications_html
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><h2>Summary</h2><table id='med_summary'><thead><tr><th>Drug</th><th>Count</th><th>Ever a repeat?</th><th>Current?</th><th>Last issued</th></tr></thead><tbody><tr><td>Lansoprazole 15mg gastro-resistant capsules</td><td>2</td><td></td><td>Yes</td><td>02-Apr-2016</td></tr><tr><td>Amoxicillin 500mg capsules Supply ( 42 ) capsule(s)</td><td>24</td><td>Yes</td><td>Yes</td><td>02-Oct-2015</td></tr><tr><td>Colofac 135mg tablets (BGP Products Ltd)</td><td>1</td><td></td><td></td><td>18-Jul-2014</td></tr><tr><td>Metformin 500mg tablets</td><td>6</td><td>Yes</td><td></td><td>12-Feb-2012</td></tr></tbody></table><h2>Current Medication Issues</h2><table><thead><tr><th>Start Date</th><th>Drug</th><th>Scheduled end</th><th>Days duration</th><th>Details</th></tr></thead><tbody><tr><td>02-Apr-2016</td><td>Lansoprazole 15mg gastro-resistant capsules maximum 6 allowed Supply ( 56 ) capsule(s) TAKE ONE ONCE DAILY</td><td>28-May-2016</td><td>56</td><td>Repeat issue</td></tr></tbody></table><h2>Current Repeat Medications</h2><table><thead><tr><th>Start date</th><th>Drug</th><th>Status</th><th>Type</th><th>Last issued</th><th>Review date</th><th>Number issued</th><th>Max Issues</th><th>Details</th></tr></thead><tbody><tr><td>02-Apr-2016</td><td>Lansoprazole 15mg gastro-resistant capsules maximum 6 allowed Supply ( 56 ) capsule(s) TAKE ONE ONCE DAILY</td><td>Current</td><td>Repeat</td><td>02-Apr-2016</td><td>28-May-2016</td><td>1</td><td>5</td><td></td></tr></tbody></table><h2>Past Medications</h2><table><thead><tr><th>Start date</th><th>Drug</th><th>Type</th><th>Last issued</th><th>Review date</th><th>Number issued</th><th>Max issues</th>		<th>Details</th></tr></thead><tbody><tr><td>02-Oct-2015</td><td>Amoxicillin 500mg capsules Supply ( 42 ) capsule(s) 1 THREE TIMES A DAY</td><td>Repeat</td><td>20-Feb-2016</td><td>28-Feb-2016</td><td>12</td><td>12</td><td></td></tr><tr><td>02-Oct-2015</td><td>Amoxicillin 500mg capsules Supply ( 42 ) capsule(s) 1 THREE TIMES A DAY</td><td>Acute</td><td>20-Feb-2016</td><td>28-Feb-2016</td><td>12</td><td></td><td></td></tr><tr><td>18-Jul-2014</td><td>Colofac 135mg tablets (BGP Products Ltd) - 24 tablet - take two a day</td><td>Acute</td><td></td><td></td><td></td><td></td><td></td></tr><tr><td>12-Feb-2012</td><td>Metformin 500mg tablets maximum 6 allowed Supply ( 112 ) tablet(s) TAKE ONE TWICE DAILY</td><td>Repeat</td><td>11-Oct-2012</td><td></td><td>6</td><td>6</td><td></td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.observations
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><table><thead><tr><th>Date</th><th>Entry</th><th>Value</th><th>Details</th></tr></thead><tbody><tr><td>16-Dec-2015</td><td>Peak exp. flow rate: PEFR/PFR Updated peakflow</td><td>150 L/min</td><td /></tr><tr><td>13-Aug-2014</td><td>O/E - blood pressure reading</td><td>115 / 68 mmHg</td><td /></tr><tr><td>13-Aug-2014</td><td>O/E - weight</td><td>79.4 kgs</td><td /></tr><tr><td>21-Oct-2013</td><td>O/E - blood pressure reading</td><td>112 / 66 mmHg</td><td /></tr><tr><td>22-Oct-2012</td><td>O/E - BP reading normal</td><td>105 / 70 mmHg</td><td /></tr><tr><td>22-Oct-2012</td><td>O/E - weight</td><td>72.5 kgs</td><td /></tr><tr><td>22-Oct-2012</td><td>O/E - weight</td><td>72.5 kgs</td><td /></tr><tr><td>23-Aug-2012</td><td>O/E - Left retina normal</td><td /><td /></tr><tr><td>23-Aug-2012</td><td>O/E - Right retina normal</td><td /><td /></tr><tr><td>07-Sep-2011</td><td>O/E - no L diabet retinopathy</td><td /><td /></tr><tr><td>07-Sep-2011</td><td>O/E - no R diabet retinopathy</td><td /><td /></tr><tr><td>03-Apr-2011</td><td>O/E - weight</td><td>70 kgs</td><td /></tr><tr><td>03-Apr-2011</td><td>O/E - weight</td><td>70 kgs</td><td /></tr><tr><td>03-Apr-2011</td><td>O/E - weight</td><td>70 kgs</td><td /></tr><tr><td>03-Apr-2011</td><td>O/E - BP reading normal</td><td>113 / 76 mmHg</td><td /></tr><tr><td>03-Apr-2011</td><td>O/E - blood pressure reading</td><td>113 / 76 mmHg</td><td /></tr><tr><td>02-Jan-2011</td><td>O/E - weight</td><td>74.5 kgs</td><td /></tr><tr><td>12-Sep-2010</td><td>O/E - weight</td><td>80.5 kgs</td><td /></tr><tr><td>12-Sep-2010</td><td>O/E - BP reading normal</td><td>105 / 74 mmHg</td><td /></tr><tr><td>24-Nov-2009</td><td>O/E - Left retina normal</td><td /><td /></tr><tr><td>24-Nov-2009</td><td>O/E - Right retina normal</td><td /><td /></tr><tr><td>12-Oct-2009</td><td>O/E - weight</td><td>73 kgs</td><td /></tr><tr><td>12-Oct-2009</td><td>O/E - BP reading normal</td><td>110 / 72 mmHg</td><td /></tr><tr><td>12-Oct-2009</td><td>O/E - weight</td><td>73.5 kgs</td><td /></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.problems
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><h2>Active Problems &amp; Issues</h2><table><thead><tr><th>Date</th><th>Read Term</th><th>Significance</th><th>Details</th></tr></thead><tbody><tr><td>13-Apr-2016</td><td>Drug induced rash</td><td>Active</td><td /></tr><tr><td>13-Apr-2016</td><td>Anger reaction</td><td>Active</td><td /></tr><tr><td>13-Dec-2015</td><td>Hyperlipidaemia</td><td>Active</td><td /></tr><tr><td>09-Sep-2014</td><td>Type 2 diabetes mellitus</td><td>Flagged as significant</td><td /></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.referrals
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><table id='referral'><thead><tr><th>Date</th><th>Who From</th><th>Who To</th><th>Priority</th><th>Details</th></tr></thead><tbody><tr><td>13-Jul-2009</td><td>Dr An Other (GP Registrar)</td><td>Ninewells - General medicine</td><td>Routine</td><td>Depressive disorder NEC</td></tr><tr><td>24-Dec-2004</td><td>Dr Patrick Slater (Partner)</td><td>Ninewells - Accident and Emergency</td><td>Immediately</td><td>Poisoning - self-inflicted</td></tr><tr><td>26-Mar-2001</td><td>Mr P. Slane (Other Health Care Professional)</td><td>Ninewells - Obstetrics (Hospital Bed or Delivery Facilities)</td><td>Routine</td><td>Patient pregnant</td></tr></tbody></table></div>",
  "INPS",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.patientsummary
  (id, html, provider, lastUpdated)
VALUES (
  9000000041,
  "<div><div><h2>Active Problems &amp; Issues</h2>		<table><thead><tr><th>Eventdate</th><th>Notes</th></tr></thead><tbody><tr><td>29-Oct-2010</td><td>Arterial embolus and thrombosis</td></tr><tr><td>29-Oct-2010</td><td>Preproliferative diabetic retinopathy</td></tr><tr><td>25-Nov-2009</td><td>Gout</td></tr><tr><td>21-Apr-2006</td><td>Fracture of radius NOS</td></tr><tr><td>2006</td><td>Constrictive cardiomyopathy</td></tr><tr><td>26-Aug-2005</td><td>Background diabetic retinopathy</td></tr><tr><td>16-Nov-1999</td><td>Type 2 diabetes mellitus</td></tr><tr><td>24-Aug-1999</td><td>Knee osteoarthritis NOS</td></tr><tr><td>14-Aug-1995</td><td>Essential hypertension</td></tr></tbody></table><h2>urrent Medication Issues</h2>		<table><tbody><tr><td>12-Feb-2016</td><td>Repeat Warfarin 3mg tablets Until: 29-Oct-2011 Last issued: 03-Oct-2011 Issued: 6 maximum 6 allowed Supply ( 60 ) tablet(s) AS DIRECTED</td></tr><tr><td>29-Mar-2016</td><td>Repeat Co-codamol 8mg/500mg tablets Until: 29-Oct-2011 Last issued: 08-Aug-2011 Issued: 6 maximum 6 allowed Supply ( 200 ) tablet(s) TAKE 1 OR 2 4 TIMES/DAY WHEN REQUIRED</td></tr></tbody></table><h2>Current Allergies and Adverse Reactions</h2>		<table><tbody><tr><td>08-Jan-2013</td><td>H/O: penicillin allergy   Certain Mild Allergy  to  Amoxicillin 250mg capsules  causing  C/O: a rash</td></tr><tr><td>08-Jan-2013</td><td>Likely Moderate Allergy H/O: cat allergy</td></tr></tbody></table><h2>Current Recalls</h2>				<table><tbody><tr><td>08-Jan-2013</td><td>Recall on 08-Jan-2013 for Hypertension annual review with Dr A Sapphire Status: Outstanding</td></tr></tbody></table><h2>Last 3 Encounters</h2><table><tbody><tr><td><div class='encounters'><table><tbody><tr><th colspan='6'>Clinic</th></tr><tr><td>20-Apr-2016</td><td>BP 120 / 80 at 15:20:00 taken Sitting Cuff: Standard O/E - blood pressure reading</td><td>Dr Bob Ash</td></tr><tr><td>20-Apr-2016</td><td>Issue 1 Amobarbital 50mg / Secobarbital sodium 50mg capsules Supply ( 21 ) capsule Three times a day</td><td>Dr Bob Ash</td></tr><tr><td>20-Apr-2016</td><td>Repeat Cocaine powder Until: 15-Sep-2014 Last issued: 21-Oct-2008 Issued: 1 maximum 2 allowed Supply ( 2 ) gram(s) 3</td><td>Dr Bob Ash</td></tr></tbody></table><table><tbody><tr><th colspan='6'>Surgery Consultation</th></tr><tr><td>02-Jun-2013</td><td>BP 120 / 72 at: 02-Jun-2013 09:56:00 taken Sitting from Left Cuff: Standard O/E - blood pressure reading</td><td>Dr Bob Ash</td></tr><tr><td>02-Jun-2013</td><td>O/E - pulse rate 79 beats/min</td><td>Dr Bob Ash</td></tr></tbody></table><table><tbody><tr><th colspan='6'>Surgery Consultation</th></tr><tr><td>02-Sep-2010</td><td>Mechanical low back pain</td><td>Dr Bob Ash</td></tr></tbody></table></div></td></tr></tbody></table><h2>Recent Investigations (last 3 months) </h2><table><thead><tr><th>Date</th><th>Report</th></tr></thead><tbody><tr><td>21-May-2016</td><td>Score:   12   Negative Method:  3RT  for:  Asthma</td></tr><tr><td>19-May-2016</td><td>ECG normal Dr Janet Outside Practice</td></tr><tr><td>19-May-2016</td><td>Thyroid hormone tests = 0</td></tr><tr><td>19-May-2016</td><td>Prostate specific antigen = 0.6</td></tr></tbody></table></div></div>",
  "INPS",
  '2016-07-25 12:00:00'
);
