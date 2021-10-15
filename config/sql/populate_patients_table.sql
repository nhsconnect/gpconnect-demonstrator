USE gpconnect1_2_8;
LOCK TABLES patients WRITE;
INSERT INTO patients
	   (id,title,first_name,last_name,address_1,address_2,address_3,address_4,address_5,postcode,phone,date_of_birth,gender,nhs_number,pas_number,department_id,gp_id,lastUpdated,sensitive_flag,multiple_birth,deceased,marital_status,managing_organization,registration_start, registration_status)
VALUES
(1,'MR','Garth','WRIGHT','','23 MARSH LANE','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5JD','01454587554','1933-11-14','Male',9658218865,000001,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(2,'MR','Mike','MEAKIN','','1 KNIGHTS COURT','','SCUNTHORPE','S HUMBERSIDE','DN16 3PL','01454587554','1927-06-19','Male',9658218873,000002,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(3,'MR','Kevin','LEACH','','12 ROOKERY CROFT','EPWORTH','DONCASTER','S YORKSHIRE','DN9 1SJ','02123636563','1921-08-08','Male',9658218881,000003,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(4,'MR','Arnold','OLLEY','','17 NEW STREET','ELSHAM','BRIGG','','DN20 0RW','01454587554','1939-07-21','Male',9658218903,000004,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(5,'MRS','Mina','LEECH','','3 FIELDS CLOSE','EPWORTH','DONCASTER','S YORKSHIRE','DN9 1TL','01454587554','1918-09-19','Female',9658218989,000005,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(6,'MS','Lauren','CORR','','11 MANLAKE AVENUE','WINTERTON','SCUNTHORPE','S HUMBERSIDE','DN15 9SD','01454587554','1944-12-28','Female',9658218997,000006,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(7,'MISS','Cassie','BRAY','','1 STRATFORD DRIVE','','SCUNTHORPE','S HUMBERSIDE','DN16 1ER','01454587554','1939-06-09','Female',9658219004,000007,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(8,'MS','Kim','GRIGG','','17 BOTTESFORD ROAD','','SCUNTHORPE','','DN16 3HA','01454587554','1925-10-05','Female',9658219012,000008,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(9,'MS','Emilie','KEWN','','1 ANNES CRESCENT','','SCUNTHORPE','S HUMBERSIDE','DN16 2LP','01454587554','1999-10-03','Female',9658220142,000009,1,1,'2018-03-23 12:00:00',TRUE,FALSE,NULL,'S','7','1962-07-13','A'),
(10,'MRS','Amanda','CURRIE','','12 LOW STREET','HAXEY','DONCASTER','','DN9 2LA','01454587554','1964-02-13','Female',9658220150,000010,1,1,'2018-03-23 12:00:00',TRUE,FALSE,NULL,'S','7','1962-07-13','A'),
(11,'MRS','Dilys','TABRON','','10 QUEENS AVENUE','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5QN','01454587554','1969-11-27','Female',9658220223,000011,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(12,'MRS','Emilia','CRAGG','','15 BECK HILL','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5HQ','01454587554','1968-02-05','Female',9658220215,000012,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(13,'MS','Anne','RUMBLE','','4 GRANGE LANE SOUTH','','SCUNTHORPE','S HUMBERSIDE','DN16 3AS','01454587554','1987-05-13','Female',9658219691,000013,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(15,'MRS','Loren','HUSON','','12 WARWICK DRIVE','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5NY','01454587554','1958-12-08','Female',9658220169,000015,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(16,'MRS','Zelma','DIMECK','CHAPEL HOUSE','','DEEPDALE','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 6ED','01454587554','1989-07-21','Female',9658219705,000016,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(17,'MRS','Dee','REAY','','64 EXETER ROAD','','SCUNTHORPE','S HUMBERSIDE','DN15 7AY','01454587554','1995-07-04','Female',9658219713,000017,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(18,'MR','Dan','BISSET','','11 WESTLAND ROAD','WESTWOODSIDE','DONCASTER','S YORKSHIRE','DN9 2PE','01454587554','1969-11-23','Male',9658220290,000018,1,1,'2018-03-23 12:00:00',FALSE,FALSE,'2005-01-13','S','7','1962-07-13','A'),
(20,'MS','Muriel','MAY','CRUACHAN','COMMONSIDE','CROWLE','SCUNTHORPE','S HUMBERSIDE','DN17 4EY','01454587554','1980-06-30','Female',9658219721,000020,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','A'),
(21,'MS','Lynda','ORME','HOE HILL HOUSE','','HOE HILL','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5RD','01454587554','1979-10-06','Female',9658219748,000021,1,1,'2018-03-23 12:00:00',FALSE,FALSE,NULL,'S','7','1962-07-13','N');
/* re added by perl script since not derived from PDS */
INSERT INTO patients
	   (id,title,first_name,last_name,address_1,address_2,address_3,postcode,phone,date_of_birth,gender,nhs_number,pas_number,department_id,gp_id,lastUpdated,sensitive_flag,multiple_birth,deceased,marital_status,managing_organization,registration_start)
VALUES
(14,'MS','Georgina','HOPLEY','103 ENDERBY ROAD','','SCUNTHORPE','DN17 2JL','01454587554','1927-05-15','Female',9476718943,000014,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1','1935-11-05'),
(19,'PNA','PNA','PNA','PNA','PNA','','PNA','PNA','2017-05-20','Male',9866105660,000019,1,3,'2017-05-23 12:00:00',FALSE,FALSE,NULL,'M','1','2017-05-13');
UNLOCK TABLES;

/* mobile required for a specific supplier */
INSERT INTO patient_telecoms
 (patientId,system,usetype,value)
VALUES
 (2,'PHONE','MOBILE','+447401254880');
