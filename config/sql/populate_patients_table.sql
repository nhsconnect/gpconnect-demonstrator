USE gpconnect0_7_3;

LOCK TABLES patients WRITE;
INSERT INTO patients
   (id,title,first_name,last_name,address_1,address_2,address_3,address_4,address_5,postcode,phone,date_of_birth,gender,nhs_number,pas_number,department_id,gp_id,lastUpdated,sensitive_flag)
VALUES
(1,'MR','Garth','WRIGHT','','23 MARSH LANE','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5JD','01454587554','1933-11-14','Male',9658218865,000001,1,1,'2018-03-23 12:00:00',FALSE),
(2,'MR','Mike','MEAKIN','','1 KNIGHTS COURT','','SCUNTHORPE','S HUMBERSIDE','DN16 3PL','01454587554','1927-06-19','Male',9658218873,000002,1,1,'2018-03-23 12:00:00',FALSE),
(3,'MR','Kevin','LEACH','','12 ROOKERY CROFT','EPWORTH','DONCASTER','S YORKSHIRE','DN9 1SJ','02123636563','1921-08-08','Male',9658218881,000003,1,1,'2018-03-23 12:00:00',FALSE),
(4,'MR','Arnold','OLLEY','','17 NEW STREET','ELSHAM','BRIGG','','DN20 0RW','01454587554','1939-07-21','Male',9658218903,000004,1,1,'2018-03-23 12:00:00',FALSE),
(5,'MRS','Mina','LEECH','','3 FIELDS CLOSE','EPWORTH','DONCASTER','S YORKSHIRE','DN9 1TL','01454587554','1918-09-19','Female',9658218989,000005,1,1,'2018-03-23 12:00:00',FALSE),
(6,'MS','Lauren','CORR','','11 MANLAKE AVENUE','WINTERTON','SCUNTHORPE','S HUMBERSIDE','DN15 9SD','01454587554','1944-12-28','Female',9658218997,000006,1,1,'2018-03-23 12:00:00',FALSE),
(7,'MISS','Cassie','BRAY','','1 STRATFORD DRIVE','','SCUNTHORPE','S HUMBERSIDE','DN16 1ER','01454587554','1939-06-09','Female',9658219004,000007,1,1,'2018-03-23 12:00:00',FALSE),
(8,'MS','Kim','GRIGG','','17 BOTTESFORD ROAD','','SCUNTHORPE','','DN16 3HA','01454587554','1925-10-05','Female',9658219012,000008,1,1,'2018-03-23 12:00:00',FALSE),
(9,'MS','Emilie','KEWN','','1 ANNES CRESCENT','','SCUNTHORPE','S HUMBERSIDE','DN16 2LP','01454587554','1999-10-03','Female',9658220142,000009,1,1,'2018-03-23 12:00:00',TRUE),
(10,'MRS','Amanda','CURRIE','','12 LOW STREET','HAXEY','DONCASTER','','DN9 2LA','01454587554','1964-02-13','Female',9658220150,000010,1,1,'2018-03-23 12:00:00',TRUE),
(11,'MRS','Dilys','TABRON','','10 QUEENS AVENUE','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5QN','01454587554','1969-11-27','Female',9658220223,000011,1,1,'2018-03-23 12:00:00',FALSE),
(12,'MRS','Emilia','CRAGG','','15 BECK HILL','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5HQ','01454587554','1968-02-05','Female',9658220215,000012,1,1,'2018-03-23 12:00:00',FALSE),
(13,'MS','Anne','RUMBLE','','4 GRANGE LANE SOUTH','','SCUNTHORPE','S HUMBERSIDE','DN16 3AS','01454587554','1987-05-13','Female',9658219691,000013,1,1,'2018-03-23 12:00:00',FALSE),
(15,'MRS','Loren','HUSON','','12 WARWICK DRIVE','','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5NY','01454587554','1958-12-08','Female',9658220169,000015,1,1,'2018-03-23 12:00:00',FALSE),
(16,'MRS','Zelma','DIMECK','CHAPEL HOUSE','','DEEPDALE','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 6ED','01454587554','1989-07-21','Female',9658219705,000016,1,1,'2018-03-23 12:00:00',FALSE),
(18,'MR','Dan','BISSET','','11 WESTLAND RD','WESTWOODSIDE','DONCASTER','S YORKSHIRE','DN9 2PE','01454587554','1969-11-23','Male',9658220290,000018,1,1,'2018-03-23 12:00:00',FALSE);
UNLOCK TABLES;

SET @MonthOffset = (SELECT TIMESTAMPDIFF(MONTH, '2018-10-01', now()));

-- NB Multiline comments are not parsed correctlty by the demonstrator

-- UPDATE allergies
-- SET startDate = DATE_ADD(startDate, INTERVAL @MonthOffset MONTH),
--     endDate = DATE_ADD(endDate, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE medications
-- SET lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE medication_orders
-- SET date_written = DATE_ADD(date_written, INTERVAL @MonthOffset MONTH),
--     dispense_review_date = DATE_ADD(dispense_review_date, INTERVAL @MonthOffset MONTH),
--     lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE medication_dispenses
-- SET lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE medication_administrations
-- SET administrationDate = DATE_ADD(administrationDate, INTERVAL @MonthOffset MONTH), 
--     lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE problems
-- SET startDate = DATE_ADD(startDate, INTERVAL @MonthOffset MONTH), 
--     endDate = DATE_ADD(endDate, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE referrals
-- SET sectionDate = DATE_ADD(sectionDate, INTERVAL @MonthOffset MONTH),
--     lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE encounters
-- SET sectionDate = DATE_ADD(sectionDate, INTERVAL @MonthOffset MONTH), 
--     encounterDate = DATE_ADD(encounterDate, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

-- UPDATE procedures
-- SET lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH) 
-- WHERE 1=1;

-- UPDATE observations
-- SET observationDate = DATE_ADD(observationDate, INTERVAL @MonthOffset MONTH) 
-- WHERE 1=1;

-- UPDATE immunisations
-- SET dateOfVac = DATE_ADD(dateOfVac, INTERVAL @MonthOffset MONTH) 
-- WHERE 1=1;

-- UPDATE adminitems
-- SET sectionDate = DATE_ADD(sectionDate, INTERVAL @MonthOffset MONTH) 
-- WHERE 1=1;

-- UPDATE clinicalitems
-- SET sectionDate = DATE_ADD(sectionDate, INTERVAL @MonthOffset MONTH) 
-- WHERE 1=1;

-- UPDATE investigations
-- SET sectionDate = DATE_ADD(sectionDate, INTERVAL @MonthOffset MONTH), 
--     lastUpdated = DATE_ADD(lastUpdated, INTERVAL @MonthOffset MONTH) 
-- WHERE 1=1;

-- UPDATE orders
-- SET orderDate = DATE_ADD(orderDate, INTERVAL @MonthOffset MONTH)
-- WHERE 1=1;

UPDATE medications_html
SET startDate = DATE_ADD(startDate, INTERVAL @MonthOffset MONTH), 
    scheduledEnd = DATE_ADD(scheduledEnd, INTERVAL @MonthOffset MONTH),
    lastIssued = DATE_ADD(lastIssued, INTERVAL @MonthOffset MONTH),
    reviewDate = DATE_ADD(reviewDate, INTERVAL @MonthOffset MONTH),
    discontinuationDate = DATE_ADD(discontinuationDate, INTERVAL @MonthOffset MONTH)
WHERE 1=1;
