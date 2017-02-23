INSERT INTO gpconnect.adminitems
  (nhsNumber,sectionDate, htmlPart,provider, lastUpdated)
VALUES (
  9476719931,'2012-12-10 12:17:00',
  "<div><h2>Administrative Items</h2><table><thead><tr><th>Date</th><th>Entry</th><th>Details</th></tr></thead><tbody><tr><td>Cervical Smear Defaulter</td><td></td></tr><tr><td /><td>No summary care record consent specified</td><td></td></tr></tbody><PageSectionIndex>1</PageSectionIndex></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.allergies
  (nhsNumber,currentOrHistoric, startDate, endDate, details)
VALUES (
  9476719931,
 "Historical",
  '2014-12-10 12:17:00',
  '2016-12-11 12:17:00',
  "Dog allergy"
  ),(
 9476719931,
  "Historical",
  '2009-03-10 12:17:00',
  '2016-12-11 12:17:00',
  "Grass allergy"
  ),(
   9476719931,
  "Historical",
  '2014-11-10 12:17:00',
  '2016-12-11 12:17:00',
  "Skin allergy"
  ),(
   9476719931,
 "Historical",
  '2014-11-10 12:17:00',
  '2016-12-11 12:17:00',
  "Beer allergy"
  );
INSERT INTO gpconnect.clinicalitems
  (nhsNumber,sectionDate,dateOfItem,Entry,Details)
VALUES (
  9476719931,'2008-04-09 12:17:00','2008-04-09 12:17:00',"Abdominal X-ray","No evidence of osteomyelitis."
  ),
  (
  9476719931,'2008-05-16 12:17:00','2008-05-16 12:17:00',"Curvature of spine",""
	 ),
  (
  9476719931,'2012-05-16 12:17:00','2012-05-16 12:17:00',"Private referral (&amp; to doctor)",""
	 ),
  (
  9476719931,'20-05-16 12:17:00','2008-05-16 12:17:00',"Referral to postnatal clinic",""
	 ),
  (
  9476719931,'2015-05-16 12:17:00','2015-05-16 12:17:00',"Family history of substance misuse",""
	 ),
  (
   9476719931,'2000-05-16 12:17:00','2000-05-16 12:17:00',"Suprapubic pain","Ongoing episode"
);

INSERT INTO gpconnect.encounters
  (nhsNumber, sectionDate, htmlPart, provider, lastUpdated)
VALUES (
  9476719931, '2014-12-10 12:17:00',
  "<table><h2>Encounters</h2><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>10 Dec 2014 - 12:17</th><th>Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)</th></tr><tr><td></td><td>History: Family history of diabetes mellitus type II.<br />Came to nurse last week with Polyuria Very worried that he is diabetic - confirmed on blood tests. Very amenable to change of lifestyle; no other symptoms at present.<br />Examination: O/E - Diastolic BP reading 98 mmHg. , O/E - Systolic BP reading 152 mmHg.<br />Diagnosis: Type II diabetes mellitus.<br />Procedure: Wants to avoid tabs for the moment - watch TChol as well as HbA1c. Review 1/12.<br />Metformin 500mg tablets - 168 tablets - take one three times a day - Patient has read up on metformin and is keen to start.</td></tr></tbody></table>",
  "TPP", '2016-07-25 12:00:00'
),
(
  9476719931, '2015-01-01 15:47:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>01 Jan 2015 - 15:47</th><th>Dr Jeffrey Johnson - Dr Johnson and Partners (J12345)</th>				</tr><tr><td></td><td>Type II diabetic dietary review.<br />Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials - 5 vials - use as directed.</td></tr></tbody></table>",
  "TPP", '2016-07-25 12:00:00'
),
(
  9476719931, '2015-07-04 12:10:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>04 Jul 2015 - 12:10</th><th>Miss Tanya Turnpike (Practice Nurse) - Dr Johnson and Partners (J12345)</th></tr><tr><td></td><td>Contact method: Telephone<br />Care plan created: Diabetes management plan.<br />Haemoglobin A1c level - IFCC standardised 23.5 mmol/mol.</td></tr></tbody></table>",
  "TPP", '2016-07-25 12:00:00'
),
(
  9476719931, '2016-01-08 09:22:00',
  "<table><thead><th>Date</th><th>Title</th><th>Details</th></thead><tbody><tr><th>08 Jan 2016 - 09:22</th><th>Cynthia Carlson (Practice Nurse) - Dandelion Medical Practice (D12345)</th> </tr><tr><td></td><td>Peanut allergy<br />Sensitivity: Penicillin<br />Haemoglobin A1c level - IFCC standardised 25.5 mmol/mol.<br />Here is some free text that applies to the whole encounter.</td></tr></tbody></table>",
  "TPP", '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.immunisations
  (nhsNumber, dateOfVac, vaccination, part, contents, details)
VALUES (
  9476719931,
  '2009-06-13 09:22:00',
 "PediaceExpiry Date: 04-Oct-2016",
 "Dont know",
 "DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS",
"DIPTHERIA, HIB, PERTUSSIS, POLIO, TETANUS"
 ),(
 9476719931,
 '2002-06-13 09:22:00',
 "HIV Injection",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2003-06-13 09:22:00',
 "HIV Injection",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2004-06-13 09:22:00',
 "HIV Injection",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2005-06-13 09:22:00',
 "HIV Injection",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2006-06-13 09:22:00',
 "MMR III",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2007-06-13 09:22:00',
 "MMR I",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2008-06-13 09:22:00',
 "MMR II",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2009-06-13 09:22:00',
 "Arilvax",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 ),(
 9476719931,
 '2012-06-13 09:22:00',
 "Fluenz (AstraZeneca UK Ltd)",
 "Manufacturer : fred
Batch: 1
Injection Location:Left leg
Expiry Date: 04-Oct-2016",
 "Dont know",
 "Dont Know"
 );
 


INSERT INTO gpconnect.investigations
  (id, html, provider, lastUpdated)
VALUES (
  9476719931,
  "<div><table class='\&quot;groupedtable\&quot;'><tbody><tr><th>12 Mar 2016</th><th>Full blood count</th></tr><tr><td /><td>Result indicator: Borderline<br />Follow-up action: Make an appointment to see doctor<br />No filing comments given.<br /><br />Date Analysed: 12/03/12<br />Time Analysed: 11:22<br />Large unstained cell: 0.20 10*9/L<br />report 7:nhs003 no errors: 0.427 L/L<br /><br /><table class='\&quot;innertable\&quot;'><tbody><tr><td>Full blood count</td><td /><td /></tr><tr><td>Haemoglobin concentration</td><td /><td>15.4 g/dL [13 - 17]</td></tr><tr><td>Total white blood count</td><td>Above range</td><td>13.1 10^9/L [4 - 11]</td></tr><tr><td>Platelet count - observation</td><td /><td>244 10^9/L [150 - 400]</td></tr><tr><td>Neutrophil count</td><td>Above range</td><td>8.5 10^9/L [2 - 7.5]</td></tr><tr><td>Lymphocyte count</td><td /><td>2.8 10^9/L [1.5 - 4]</td></tr><tr><td>Monocyte count - observation</td><td>Above range</td><td>1.1 10^9/L [0.2 - 1]</td></tr><tr><td>Eosinophil count - observation</td><td>Above range</td><td>0.6 10^9/L [0 - 0.5]</td></tr><tr><td>Basophil count</td><td /><td>0.1 10^9/L [0 - 0.1]</td></tr><tr><td>Red blood cell count</td><td>Above range</td><td>5.75 10^12/L [4.5 - 5.5]</td></tr><tr><td>Packed cell volume</td><td /><td>0.465  [0.4 - 0.5]</td></tr><tr><td>Mean cell volume</td><td /><td>80.9 fL [80 - 100]</td></tr><tr><td>Mean cell haemoglobin level</td><td>Below range</td><td>26.8 pg [27 - 32]</td></tr><tr><td>Mean cell haemoglobin concentration</td><td /><td>33.1 g/dL [32 - 36]</td></tr></tbody></table></td></tr><tr><th>23 Jan 2016</th><th>Serum TSH level</th></tr><tr><td /><td>Result indicator: Normal<br />No further action required.<br />No filing comments given.<br /><br />Clinical Information: Clinical Details: THYROXINE<br /><br /><table class='\&quot;innertable\&quot;'><tbody><tr><td>Serum TSH level</td><td /><td>1.5 miu/L [0.2 - 4]<br />TSH level is consistent with optimum replacement<br />NB above comment applies only to patients on T4 for<br />treatment of primary hypothyroidism.<br />Treatment target in CA Thyroid is TSH &lt;0.10 mU/L</td></tr></tbody></table></td></tr></tbody></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.medications_html
  (nhsNumber, currentRepeatPast, startDate, medicationItem, scheduledEnd, daysDuration, details, lastIssued, reviewDate, numberIssued, maxIssued, typeMed)
VALUES (
  9476719931,"Current","14-09-2005", "Lansoprazole 15mg gastro-resistant capsules","19-11-2019","Day Duration 2","3", NULL,NULL, NULL, NULL,"4"
),(
  9476719931,"Current","14-09-2011", "Amoxicillin 500mg capsules Supply ( 42 ) capsule(s)","19-11-2019","Day Duration 4","3", NULL,NULL, NULL, NULL,"2"
),(
  9476719931,"Current","14-09-2012", "Colofac 135mg tablets (BGP Products Ltd)","19-11-2019","Day Duration 4","3", NULL,NULL, NULL, NULL,"32"
),(
  9476719931,"Current","14-09-2014", "Metformin 500mg tablets","19-11-2019","Day Duration 4","3", "Issue more","Issue more", "Issue more", "Issue more","4"
),(
  9476719931,"Repeat","14-09-2014", "Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more", "Issue more", "Issue more","4"
),(
  9476719931,"Past","14-09-2014", "Metformin 500mg tablets","19-11-2019","Day Duration 4","3", "Issue more","Issue more", "Issue more", "Issue more","4"
),(
  9476719931,"Past","14-09-2014", "Metformin 500mg tablets","19-11-2019","Day Duration 4","3", "Issue more","Issue more", "Issue more", "Issue more","4"
),(
  9476719931,"Past","14-09-2014", "Metformin 500mg tablets","19-11-2019","Day Duration 4","3","Issue more","Issue more", "Issue more", "Issue more","4"
);

INSERT INTO gpconnect.observations
  (id, html, provider, lastUpdated)
VALUES (
  9476719931,
  "<div><h2>Observations</h2><table><thead><th>Date</th><th>Entry</th><th>Value</th><th>Details</th></thead><tbody><tr><td>24 Mar 2016</td><td>Full Health of the Nation Outcome Scale score</td><td>16 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of Nat Outc Sc item 1 - aggressive/disrupt behaviour</td><td>0 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of Nat Outc Scale item 2 - non-accidental self injury</td><td>2 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of Nat Outcome Scale item 8 - other ment/behav probl</td><td>2 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of Nation Outcome Scale item 10 - activit daily liv</td><td>0 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of Nation Outcome Scale item 5 - phys illn/disabil</td><td>0 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of Nation Outcome Scale item 9 - relationship probl</td><td>0 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of the Nation Outcome scale item 11 - living condit</td><td>2 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of the Nation Outcome Scale item 3 - alcoh/drug probl</td><td>3 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of the Nation Outcome Scale item 4 - cognitive probl</td><td>2 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of the Nation Outcome Scale item 6 - hallucinat/delus</td><td>2 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of the Nation Outcome Scale item 7 - depressed mood</td><td>3 </td><td>(Added from Questionnaire)</td></tr><tr><td>24 Mar 2016</td><td>Health of the Nation Outcomes scale item 12 - occup/activit</td><td>0 </td><td>(Added from Questionnaire)</td></tr><tr><td>21 Mar 2016</td><td>O/E - height</td><td>1.67 m (5 ' 6 \)</td><td>(Added from Questionnaire)</td></tr><tr><td>21 Mar 2016</td><td>O/E - weight</td><td>80 Kg (12 st 8 lb)</td><td>(Added from Questionnaire)</td></tr><tr><td>11 Jan 2016</td><td>Pipe smoker</td><td>2 grams/day</td><td /></tr><tr><td>21 Oct 2015</td><td>New Episode Pain score</td><td>15 </td><td>pain in head?: yes<br />pain in neck: yes<br />pain in chest: yes<br />pain in legs: yes<br />pain in feet: yes</td></tr><tr><td>21 Oct 2015</td><td>New Episode Serum cholesterol level</td><td>474 mmol/L</td><td /></tr><tr><td>21 Oct 2015</td><td>Ongoing Episode Serum cholesterol level</td><td>? 5.2 mmol/L</td><td /></tr><tr><td>21 Oct 2015</td><td>Pain score</td><td>15 </td><td>pain in head?: yes<br />pain in neck: yes<br />pain in chest: yes<br />pain in legs: yes<br />pain in feet: yes</td></tr><tr><td>21 Oct 2015</td><td>Pain score</td><td>15 </td><td>pain in head?: yes<br />pain in neck: yes<br />pain in chest: yes<br />pain in legs: yes<br />pain in feet: yes</td></tr><tr><td>21 Oct 2015</td><td>Pain score</td><td>15 </td><td>pain in head?: yes<br />pain in neck: yes<br />pain in chest: yes<br />pain in legs: yes<br />pain in feet: yes</td></tr><tr><td>21 Oct 2015</td><td>Serum cholesterol level</td><td>1.2 mmol/L</td><td /></tr><tr><td>16 Oct 2015</td><td>11 betahydroxy aetiocholanolone:creatinine ratio</td><td>12 s</td><td>Q1: A1<br />Q2: N/A</td></tr><tr><td>10 Apr 2014</td><td>HAD scale: depression score</td><td>10 </td><td /></tr><tr><td>07 Apr 2014</td><td>Blood glucose level</td><td>5.8 mmol/L</td><td /></tr><tr><td>07 Apr 2014</td><td>Blood oxygen saturation</td><td>92 %</td><td /></tr><tr><td>07 Apr 2014</td><td>Heart rate</td><td>70 BPM</td><td /></tr><tr><td>07 Apr 2014</td><td>O/E - Systolic BP reading</td><td>120 mmHg</td><td /></tr><tr><td>07 Apr 2014</td><td>Respiratory rate</td><td>15 breaths/min</td><td /></tr><tr><td>07 Apr 2014</td><td>Temperature</td><td>38 C</td><td /></tr><tr><td>04 Apr 2014</td><td>O/E - Diastolic BP reading</td><td>80 mmHg</td><td /></tr><tr><td>25 Oct 2013</td><td>Waterlow pressure sore risk score</td><td>1 </td><td>Sex: N/A<br />Age: 14-49<br />Build/Weight For Height: N/A<br />Continence: N/A<br />Skin Type Visual Risk Areas - Healthy: N/A<br />Skin Type Visual Risk Areas - Tissue Paper: N/A<br />Skin Type Visual Risk Areas - Dry: N/A<br />Skin Type Visual Risk Areas - Oedematous: N/A<br />Skin Type Visual Risk Areas - Clammy, Pyrexia: N/A<br />Skin Type Visual Risk Areas - Discoloured Grade 1: N/A<br />Skin Type Visual Risk Areas - Broken/Spots Grade 2-4: N/A<br />Mobility - Fully: N/A<br />Mobility - Restless/Fidgety: N/A<br />Mobility - Apathetic: N/A<br />Mobility - Restricted: N/A<br />Mobility - Bedbound (e.g. Traction): N/A<br />Mobility - Chairbound (e.g. Wheelchair): N/A<br />Weight A - Has Patient Lost Weight Recently: N/A<br />Weight B - Weight Loss Score: N/A<br />Weight C - Patient Eating Poorly or Lack of Appetite: N/A<br />Tissue Malnutrition - Terminal Cachexia: N/A<br />Tissue Malnutrition - Multiple Organ Failure: N/A<br />Tissue Malnutrition - Single Organ Failure (Resp, Renal, Cardiac): N/A<br />Tissue Malnutrition - Peripheral Vascular Disease: N/A<br />Tissue Malnutrition - Anaemia (Hb &lt; 8g/DL): N/A<br />Tissue Malnutrition - Smoking: N/A<br />Neurological Deficit (Choose the appropriate deficit and corresponding score): N/A<br />Major Surgery or Trauma: N/A<br />Medication - Cytotoxics, Long Term/High Dose Steroids, Anti-Inflamatory (Max of 4): N/A</td></tr><tr><td>02 Oct 2013</td><td>Grade of pressure sore on back (waterlow assessment)</td><td>2 </td><td /></tr><tr><td>02 Oct 2013</td><td>Grade of pressure sore on head (waterlow assessment)</td><td>1 </td><td /></tr><tr><td>02 Oct 2013</td><td>Grade of pressure sore on left buttock (waterlow assessment)</td><td>3 </td><td /></tr><tr><td>02 Oct 2013</td><td>Grade of pressure sore on right buttock (waterlow assessment)</td><td>4 </td><td /></tr><tr><td>02 Oct 2013</td><td>Grade of pressure sore on right malleolus (waterlow assessment)</td><td>2 </td><td /></tr><tr><td>02 Oct 2013</td><td>Waterlow pressure sore risk score</td><td>17 </td><td>Sex: N/A<br />Age: 14-49<br />Build/Weight For Height: Average:  BMI = 20-24.9<br />Continence: Complete/Catheterised<br />Skin Type Visual Risk Areas - Healthy: N/A<br />Skin Type Visual Risk Areas - Tissue Paper: Yes<br />Skin Type Visual Risk Areas - Dry: N/A<br />Skin Type Visual Risk Areas - Oedematous: Yes<br />Skin Type Visual Risk Areas - Clammy, Pyrexia: N/A<br />Skin Type Visual Risk Areas - Discoloured Grade 1: Yes<br />Skin Type Visual Risk Areas - Broken/Spots Grade 2-4: N/A<br />Mobility - Fully: N/A<br />Mobility - Restless/Fidgety: N/A<br />Mobility - Apathetic: Yes<br />Mobility - Restricted: N/A<br />Mobility - Bedbound (e.g. Traction): N/A<br />Mobility - Chairbound (e.g. Wheelchair): Yes<br />Weight A - Has Patient Lost Weight Recently: N/A<br />Weight B - Weight Loss Score: N/A<br />Weight C - Patient Eating Poorly or Lack of Appetite: N/A<br />Tissue Malnutrition - Terminal Cachexia: N/A<br />Tissue Malnutrition - Multiple Organ Failure: N/A<br />Tissue Malnutrition - Single Organ Failure (Resp, Renal, Cardiac): Yes<br />Tissue Malnutrition - Peripheral Vascular Disease: N/A<br />Tissue Malnutrition - Anaemia (Hb &lt; 8g/DL): N/A<br />Tissue Malnutrition - Smoking: N/A<br />Neurological Deficit (Choose the appropriate deficit and corresponding score): N/A<br />Major Surgery or Trauma: N/A<br />Medication - Cytotoxics, Long Term/High Dose Steroids, Anti-Inflamatory (Max of 4): N/A</td></tr><tr><td>07 Jan 2013</td><td>Blood oxygen saturation</td><td>1 %</td><td /></tr><tr><td>12 Mar 2012</td><td>Basophil count</td><td>0 10^9/L [0 - 1]</td><td /></tr><tr><td>12 Mar 2012</td><td>Eosinophil count - observation</td><td>0.2 10^9/L [0.03 - 0.8]</td><td /></tr><tr><td>12 Mar 2012</td><td>Haemoglobin concentration</td><td>13.7 g/dL [11.5 - 16.5]</td><td /></tr><tr><td>12 Mar 2012</td><td>Haemolytic complement (CH50) level</td><td>13.7 U/ML [11.5 - 16.5]</td><td /></tr><tr><td>12 Mar 2012</td><td>Lymphocyte count</td><td>3.6 10^9/L [1 - 4.5]</td><td /></tr><tr><td>12 Mar 2012</td><td>Mean cell haemoglobin level</td><td>30.5 pg [27 - 32]</td><td /></tr><tr><td>12 Mar 2012</td><td>Mean cell volume</td><td>95 fL [78 - 103]</td><td /></tr><tr><td>12 Mar 2012</td><td>Monocyte count - observation</td><td>0.5 10^9/L [0.2 - 0.8]</td><td /></tr><tr><td>12 Mar 2012</td><td>Neutrophil count</td><td>4.2 10^9/L [2 - 8]</td><td /></tr><tr><td>12 Mar 2012</td><td>Platelet count - observation</td><td>276 10^9/L [150 - 450]</td><td /></tr><tr><td>12 Mar 2012</td><td>Red blood cell count</td><td>4.49 10^12/L [3.9 - 5]</td><td /></tr><tr><td>12 Mar 2012</td><td>Total white blood count</td><td>8.7 10^9/L [4 - 11]</td><td /></tr><tr><td>12 Dec 2011</td><td>Body mass index - observation</td><td>46.53 Kg/m2</td><td /></tr><tr><td>12 Dec 2011</td><td>Body mass index - observation</td><td>46.53 Kg/m2</td><td /></tr><tr><td>12 Dec 2011</td><td>O/E - height</td><td>1.2 m (47 \)</td><td /></tr><tr><td>12 Dec 2011</td><td>O/E - weight</td><td>67 Kg (10 st 8 lb)</td><td /></tr><tr><td>09 Dec 2011</td><td>O/E - height</td><td>1.6 m (5 ' 3 \)</td><td /></tr><tr><td>09 Dec 2011</td><td>O/E - weight</td><td>60 Kg (9 st 6 lb)</td><td /></tr><tr><td>21 Nov 2011</td><td>Total white blood count</td><td>0.5 10^9/L</td><td /></tr><tr><td>07 Nov 2011</td><td>11 betahydroxy androsterone concentration</td><td>1 </td><td /></tr><tr><td>07 Nov 2011</td><td>11 betahydroxy androsterone concentration</td><td>2 </td><td /></tr><tr><td>07 Nov 2011</td><td>Cigarette consumption</td><td>1 cigarettes / day</td><td /></tr><tr><td>07 Nov 2011</td><td>Cigarette consumption</td><td>2 cigarettes / day</td><td /></tr><tr><td>07 Nov 2011</td><td>Cigarette consumption</td><td>2 cigarettes / day</td><td /></tr><tr><td>07 Nov 2011</td><td>Cigarette pack-years</td><td>1 </td><td /></tr><tr><td>07 Nov 2011</td><td>Cigarette pack-years</td><td>2 </td><td /></tr><tr><td>07 Nov 2011</td><td>Cigarette pack-years</td><td>2 </td><td /></tr><tr><td>20 Oct 2011</td><td>O/E - Diastolic BP reading</td><td>90 mmHg</td><td /></tr><tr><td>20 Oct 2011</td><td>O/E - Systolic BP reading</td><td>150 mmHg</td><td /></tr><tr><td>02 Sep 2011</td><td>Waterlow pressure sore risk score</td><td>41 </td><td>Sex: Male<br />Age: 75-80<br />Build/Weight For Height: Obese: <br />BMI &gt; 30<br />Continence: Faecal Incontinence<br />Skin Type - Visual Risk Areas: Oedematous<br />Mobility: Restricted<br />A - Has Patient Lost Weight Recently: Unsure - Go to C<br />B - Weight Loss Score: N/A<br />C - Patient Eating Poorly or Lack of Appetite: No<br />Tissue Malnutrition: Terminal Cachexia<br />Neurological Deficit (Choose the appropriate deficit and corresponding score): Paraplegia (Score 6)<br />Major Surgery or Trauma: On Table &gt; 6hr#<br />Medication - Cytotoxics, Long Term/High Dose Steroids, Anti-Inflamatory (Max of 4): 4</td></tr><tr><td>25 Jul 2011</td><td>Body mass index - observation</td><td>40 Kg/m2</td><td /></tr><tr><td>25 Jul 2011</td><td>Ideal body weight</td><td>52 Kg (8 st 3 lb)</td><td /></tr><tr><td>25 Jul 2011</td><td>O/E - Diastolic BP reading</td><td>40 mmHg</td><td /></tr><tr><td>25 Jul 2011</td><td>O/E - Systolic BP reading</td><td>100 mmHg</td><td /></tr><tr><td>11 Jul 2011</td><td>Blood serotonin level</td><td>54 nmol/10*9plts</td><td /></tr><tr><td>31 May 2011</td><td>Herpes simplex virus isolation</td><td>48 </td><td /></tr><tr><td>25 May 2011</td><td>Alkaline phosphatase level</td><td>45 U/L</td><td /></tr><tr><td>25 May 2011</td><td>Neutrophil alkaline phosphatase level</td><td>78 </td><td /></tr><tr><td>25 May 2011</td><td>Pain score</td><td>6 </td><td /></tr><tr><td>25 May 2011</td><td>Red blood cell count</td><td>48 10^12/L</td><td /></tr><tr><td>16 Feb 2011</td><td>O/E - Diastolic BP reading</td><td>80 mmHg</td><td /></tr><tr><td>16 Feb 2011</td><td>O/E - Systolic BP reading</td><td>100 mmHg</td><td /></tr><tr><td>17 Dec 2010</td><td>24 hour urine normetadrenaline output</td><td>120 umol/d</td><td /></tr><tr><td>30 Nov 2010</td><td>Basophil count</td><td>0 10^9/L [0 - 0.1]</td><td /></tr><tr><td>30 Nov 2010</td><td>Eosinophil count - observation</td><td>0.1 10^9/L [0 - 0.5]</td><td /></tr><tr><td>30 Nov 2010</td><td>Haemoglobin concentration</td><td>12.8 g/dL [12 - 15]</td><td /></tr><tr><td>30 Nov 2010</td><td>Lymphocyte count</td><td>2.1 10^9/L [1.5 - 4]</td><td /></tr><tr><td>30 Nov 2010</td><td>Mean cell haemoglobin concentration</td><td>31 g/dL [32 - 36]</td><td>Below low reference limit</td></tr><tr><td>30 Nov 2010</td><td>Mean cell haemoglobin level</td><td>28.1 pg [27 - 32]</td><td /></tr></tbody></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.problems
  (nhsNumber,activeOrInactive, startDate,endDate,entry,significance,details)
VALUES (
  9476719931,
  "Active",
  '2016-07-25 12:00:00',
 '2016-07-25 12:00:00',
  "Type II diabetes mellitus",
  "Major",
  "Treated daily"
  ),(
   9476719931,
  "Inactive",
  '2016-07-25 12:00:00',
 '2016-07-25 12:00:00',
  "Type II diabetes mellitus",
  "Major",
  "Treated daily"
  ),(
   9476719931,
  "Active",
  '2000-07-25 12:00:00',
 '2016-07-25 12:00:00',
  "Low Back Pain",
  "Minor",
  "Treated daily"
  ),(
   9476719931,
  "Inactive",
  '2003-07-25 12:00:00',
 '2016-07-25 12:00:00',
  "Asthma",
  "Major",
  "Treated daily"
  ),(
   9476719931,
  "Inactive",
  '2008-07-25 12:00:00',
 '2012-07-25 12:00:00',
  "Disorder of hear",
  "Major",
  "Treated daily"
  ),(
   9476719931,
  "Inactive",
  '2000-07-25 12:00:00',
 '2005-07-25 12:00:00',
  "Low back pain",
  "Minor",
  "Treated daily"
  );

INSERT INTO gpconnect.referrals
  (nhsNumber, sectionDate, htmlPart, provider, lastUpdated)
VALUES (
   9476719931,'2016-03-03 00:00:01',
  "<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><td>03 Mar 2016</td><td>Airedale Hospital</td><td>Dr Johnson &amp; Partners</td><td>Routine</td><td>From Hospital out-patient for Diabetic Medicine<br />Receiving care<br /></td></tbody></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
),(
 9476719931,'2013-04-09 00:00:01',
 "<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><td>09 Apr 2013</td><td>Dr Johnson &amp; Partners</td><td>Leeds District Nurses</td><td>Routine</td><td>Referral to local authority weight management programme<br />Waiting For Information</td></tbody></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
),(
 9476719931,'2012-07-14 00:00:01',
  "<div><h2>Referrals</h2><table><thead><th>Date</th><th>From</th><th>To</th><th>Priority</th><th>Details</th></thead><tbody><td>14 Jul 2012</td><td>Dr Johnson &amp; Partners</td><td>Leeds General Infirmary</td><td>Urgent</td><td>Back pain</td></tbody></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
);

INSERT INTO gpconnect.patientsummary
  (id, html, provider, lastUpdated)
VALUES (
  9476719931,
  "<div><h2>Warnings</h2><table><thead><tr><th>Start Date</th><th>Entry</th><th>Significance</th><th>Details</th></tr></thead><tbody><tr><td>23 Feb 2005</td><td>Loses temper easily</td></tr></tbody></table><h2>Key Indicators</h2><table><thead><tr><th>Start Date</th><th>Medication Item</th><th>Type</th><th>Scheduled End Date</th><th>Days Duration</th><th>Details</th></tr></thead><tbody><tr><td>20 Feb 2015</td><td>Has an end of life care plan</td></tr><tr><td>13 Jan 1999</td><td>Asthmatic</td></tr></tbody></table><h2>Active Problems and Issues</h2><table><thead><tr><th>Last Issued</th><th>Medication Item</th><th>Start Date</th><th>Review Date</th><th>Number Issued</th><th>Max Issues</th><th>Details</th></tr></thead><tbody><tr><td>23 Feb 2005</td><td>Type II diabetes mellitus</td><td>Major</td><td /></tr><tr><td>10 Apr 2000</td><td>Low back pain</td><td>Minor</td><td /></tr><tr><td>13 Jan 1999</td><td>Asthma</td><td>Major</td><td /></tr></tbody></table><h2>Current Medication Issues</h2><table><thead><tr><th>Start Date</th><th>Details</th></tr></thead><tbody><tr><td>10 Apr 2016</td><td>Paracetemol 500mg tablets - 32 tablet - take 1 or 2 4 times/day</td><td>14 Apr 2016</td><td>4</td><td /></tr><tr><td>08 Apr 2016</td><td>Metformin 500mg tablets - 42 tablet - take one 3 times/day</td><td>22 Apr 2016</td><td>14</td><td>Repeat issue</td></tr></tbody></table><h2>Current Repeat Medications</h2><table><thead><tr><th>Date</th><th>Title</th><th>Details</th></tr></thead><tbody><tr><td>01 Mar 2016</td><td>Metformin 500mg tablets - 42 tablet - take one 3 times/day</td><td>Repeat</td><td>08 Apr 2016</td><td>30 Sep 2016</td><td>2</td><td>12</td><td /></tr></tbody></table><h2>Current Allergies and Adverse Reactions</h2><table><tbody><tr><th>Start date</th><th>Details</th></tr><tr><td>10 Mar 2016</td><td>PENICILLIN VK</td></tr><tr><td>29 Mar 2016</td><td>Nurofen Express 256mg caplets (Reckitt Benckiser Healthcare (UK) Ltd)</td></tr><tr><td>29 Mar 2016</td><td>Nut allergy</td></tr></tbody></table><h2>Current Recalls</h2><table><tbody><tr><th>Recall date</th><th>Details</th></tr><tr><td>29 Jan 2017</td><td>Asthma monitoring</td></tr><tr><td>10 Sep 2016</td><td>Influenza Vaccination</td></tr>	</tbody></table><h2>Encounters</h2><table><tbody><tr><th>10 Dec 2014 - 12:17</th><th>Jeffrey Johnson - Dr Johnson and Partners (J12345)</th></tr><tr><td /><td>History: Family history of diabetes mellitus type II.<br />Came to nurse last week with Polyuria Very worried that he is diabetic - confirmed on blood tests. Very amenable to change of lifestyle; no other symptoms at present.<br />Examination: O/E - Diastolic BP reading 98 mmHg. , O/E - Systolic BP reading 152 mmHg.<br />Diagnosis: Type II diabetes mellitus.<br />Procedure: Wants to avoid tabs for the moment - watch TChol as well as HbA1c. Review 1/12.<br />Metformin 500mg tablets - 168 tablets - take one three times a day - Patient has read up on metformin and is keen to start.<br /></td></tr><tr><th>01 Jan 2015 - 15:47</th><th>Jeffrey Johnson - Dr Johnson and Partners (J12345)</th></tr><tr><td /><td>Type II diabetic dietary review.<br />Haemoglobin A1c level - IFCC standardised 20.5 mmol/mol.<br />Insulin isophane biphasic porcine 30/70 100units/ml suspension for injection 10ml vials - 5 vials - use as directed.</td></tr><tr><th>04 Jul 2015 - 12:10</th><th>Thomas Turnpike - Dr Johnson and Partners (J12345)</th></tr><tr><td /><td>Contact method: Telephone<br />Care plan created: Diabetes management plan.<br />Haemoglobin A1c level - IFCC standardised 23.5 mmol/mol.</td></tr></tbody></table><h2>Recent Investigations (last 3 months)</h2><table><tbody><tr><th>12 Mar 2016</th><th>Full blood count</th></tr><tr><td /><td>Result indicator: Borderline<br />Follow-up action: Make an appointment to see doctor<br />No filing comments given.<br /><br />Date Analysed: 12/03/12<br />Time Analysed: 11:22<br />Large unstained cell: 0.20 10*9/L<br />report 7:nhs003 no errors: 0.427 L/L<br /><br /><table><tbody><tr><td>Full blood count</td><td /><td /></tr><tr><td>Haemoglobin concentration</td><td /><td>15.4 g/dL [13 - 17]</td></tr><tr><td>Total white blood count</td><td>Above range</td><td>13.1 10^9/L [4 - 11]</td></tr><tr><td>Platelet count - observation</td><td /><td>244 10^9/L [150 - 400]</td></tr><tr><td>Neutrophil count</td><td>Above range</td><td>8.5 10^9/L [2 - 7.5]</td></tr><tr><td>Lymphocyte count</td><td /><td>2.8 10^9/L [1.5 - 4]</td></tr><tr><td>Monocyte count - observation</td><td>Above range</td><td>1.1 10^9/L [0.2 - 1]</td></tr><tr><td>Eosinophil count - observation</td><td>Above range</td><td>0.6 10^9/L [0 - 0.5]</td></tr><tr><td>Basophil count</td><td /><td>0.1 10^9/L [0 - 0.1]</td></tr><tr><td>Red blood cell count</td><td>Above range</td><td>5.75 10^12/L [4.5 - 5.5]</td></tr><tr><td>Packed cell volume</td><td /><td>0.465  [0.4 - 0.5]</td></tr><tr><td>Mean cell volume</td><td /><td>80.9 fL [80 - 100]</td></tr><tr><td>Mean cell haemoglobin level</td><td>Below range</td><td>26.8 pg [27 - 32]</td></tr><tr><td>Mean cell haemoglobin concentration</td><td /><td>33.1 g/dL [32 - 36]</td></tr></tbody></table></td></tr><tr><th>23 Jan 2016</th><th>Serum TSH level</th></tr><tr><td /><td>Result indicator: Normal<br />No further action required.<br />No filing comments given.<br /><br />Clinical Information: Clinical Details: THYROXINE<br /><br /><table><tbody><tr><td>Serum TSH level</td><td /><td>1.5 miu/L [0.2 - 4]<br />TSH level is consistent with optimum replacement<br />NB above comment applies only to patients on T4 for<br />treatment of primary hypothyroidism.<br />Treatment target in CA Thyroid is TSH &lt;0.10 mU/L</td></tr></tbody></table></td></tr></tbody></table></div>",
  "TPP",
  '2016-07-25 12:00:00'
);
