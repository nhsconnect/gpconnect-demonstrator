USE gpconnect1_2_8;
INSERT INTO translations
  (id, system, code, display)
VALUES
  (2, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '06157009', 'Sertraline 100mg tablets'),
  (3, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '16967001', 'Metformin 1g modified release tablets'),
  (4, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '10688002', 'Atorvastatin 20mg tablets'),
  (5, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '03716001', 'Gliclazide 80mg tablets'),
  (6, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '15577001', 'Lantus 100units/ml solution for injection 3ml pre-filled SoloStar pen (Sanofi)'),
  (7, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '05918001', 'Cetirizine 10mg tablets'),
  (8, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '02304002', 'Sumatriptan 50mg tablets'),
  (9, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '02876002', 'Propranolol 40mg tablets'),
  (10, 'https://fhir.hl7.org.uk/Id/multilex-drug-codes', '09493001', 'Acamprosate 333mg gastro-resistant tablets');

INSERT INTO medications
  (id,concept_code,concept_display,desc_code,desc_display,code_translation_ref,text,batchNumber,expiryDate,lastUpdated)
VALUES
  (1,196421000000109,'Transfer-degraded medication entry','','','','Co-proxamol',null,null,'2000-01-01'),
  (2,196421000000109,'Transfer-degraded medication entry','','','','Actomol','54352365','1978-08-10','2018-01-01'),
  (3,196421000000109,'Transfer-degraded medication entry','','','', 'Melleril',null,null,'2018-01-01'),
  (4,7890511000001107,'Lyrica 75mg capsules (Pfizer Ltd)','','','',null,'1322143','2020-07-13','2018-01-01'),
  (5,322623000,'Tramadol 50mg capsules','','','',null,'4253455','2021-08-10','2018-01-01'),
  (6,459211000001102,'Neurontin 100mg capsules (Pfizer Ltd)','','','',null,'548548','2021-08-10','2018-01-01'),
  (7,17651711000001105,'Gabapentin 6% gel','','','',null,'46787648','2025-01-01','2018-01-01'),
  (8,16219811000001107,'OxyContin 10mg modified-release tablets','','','',null,'4675677','2019-12-04','2018-01-01'),
  (9,330202001,'Ibuprofen 5% gel','','','',null,'784678','2024-06-05','2018-01-01'),
  (10,322504003,'Codeine 60mg tablets','','','',null,null,null,'2018-01-01'),
  (11,7774008,'Gamolenic acid 37mg / Vitamin E 10unit capsules','','','',null,null,null,'2018-01-01'),
  (12,19524811000001103,'Povidone-iodine 5% cream','','','',null,null,null,'2018-01-01'),
  (13,414049008,'Doxylamine 25 mg oral tablet','','','',null,null,null,'2018-01-01'),
  (14,329807003,'Naproxen 500 mg oral tablet','','','',null,null,null,'2018-01-01'),
  (15,3333911000001105, 'Pollenase Antihistamine 4mg tablets (E M Pharma) 30 tablet','','','',null,null,null,'2018-01-01'),
  (16,19976311000001109,'Morphine 0.1% gel','','','',null,null,null,'2018-01-01'),
  (17,9382711000001109,'Pine tar 2.3% bath oil','','','',null,null,null,'2018-01-01'),
  (18,419956001,'Human insulin 100units/mL injection solution 10mL vial','','','',null,null,null,'2018-01-01'),
  (19,377203000,'Aspirin 325 mg and carisoprodol 200 mg oral tablet','','','',null,null,null,'2018-01-01'),
  (20,9751511000001104,'Sertraline 100mg tablets','','','2',null,null,null,'2018-01-01'),
  (21,411533003,'Metformin 2G Modified-Release tablets','','','3',null,null,null,'2018-01-01'),
  (22,320030001,'Atorvastaton 20mg tablets','','','4',null,null,null,'2018-01-01'),
  (23,325242002,'Gliclazide 80mg tables','','','5',null,null,null,'2018-01-01'),
  (24,11933011000001106,'Lantus 100units/ml solution for injections 3ml pre-filled solostar pen','','','6',null,null,null,'2018-01-01'),
  (25,3200818006,'Cetirizine 10mg tablets','','','7',null,null,null,'2018-01-01'),
  (26,322815004,'Sumatriptan 50mg tablets','','','8',null,null,null,'2018-01-01'),
  (27,318353009,'Propranolol 40mg tablets','','','9',null,null,null,'2018-01-01'),
  (28,323342002,'Acamprosate 333mg gastro-resistant tablets','','','10',null,null,null,'2018-01-01');
