LOCK TABLES gpconnect.patients WRITE;

INSERT INTO gpconnect.patients
  (id,title,first_name,last_name,address_1,address_2,address_3,postcode,phone,date_of_birth,gender,nhs_number,pas_number,department_id,gp_id,lastUpdated,sensitive_flag,multiple_birth,deceased,marital_status,managing_organization)
VALUES
  (1,'MS','Sophie','Mcvey',' 10 ST. CATHERINES CRESCENT','SCUNTHORPE','S HUMBERSIDE','DN16 3LQ','01454587554','1952-06-13','Female',9476719915,000001,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'M','1'),
  (2,'MRS','Minnie','DAWES','24 GRAMMAR SCHOOL ROAD','BRIGG','','DN20 8AF','01454587554','1952-05-31','Female',9476719931,000002,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'M','1'),
  (3,'MR','Julian','PHELAN','FARM HOUSE','BONNYHALE ROAD','S HUMBERSIDE','DN17 4JQ','02123636563','1992-07-02','Male',9476719974,000003,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (4,'MRS','Dolly','BANTON','11 QUEENSWAY','SCUNTHORPE','S HUMBERSIDE','DN16 2BZ','0121454552','1955-09-18','Female',9476719958,000004,1,2,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (5,'MISS','Ruby','MACKIN','3 WILDERSPIN HEIGHTS','BARTON-UPON-HUMBER','S HUMBERSIDE','DN18 5SN','013256541523','1953-01-01','Female',9476719966,000005,2,1,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'M','1'),
  (6,'MS',' Sybill','PROUT','30 HALTON CLOSE','KIRTON LINDSEY','GAINSBOROUGH','DN21 4PX','013256541523','1953-11-02','Female',9476719923,000006,2,1,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (7,'MR','Dean','BALL','28 TENSING ROAD','SCUNTHORPE','S HUMBERSIDE','DN16 3DS','013256541523','1935-08-20','Male',9476718870,000007,2,1,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (8,'MR','Daren','ROLF','LYNTON','EAST HALTON','S HUMBERSIDE','DN40 3NN','013256541523','1929-01-10','Male',9476718889,000008,2,1,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (9,'MR','Carl','CASSIN','LONGCROFT','WALCOT ROAD','ALKBOROUGH','DN15 9JS','01454587554','1951-04-16','Male',9476718897,000009,1,3,'2016-07-25 12:00:00',TRUE,FALSE,NULL,'S','1'),
  (10,'MR','Evan','TOWERS','2 FRONT STREET','BRIGG','S HUMBERSIDE','DN20 0RD','01454587554','1934-09-18','Male',9476718900,000010,1,3,'2016-07-25 12:00:00',TRUE,FALSE,NULL,'S','1'),
  (11,'MR','Donald','GROVES','41 THE MEADOWS','MESSINGHAM','SCUNTHORPE','DN17 3UD','01454587554','1917-03-17','Male',9476718919,000011,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (12,'MR','Mervyn','EAVE','11 WESLEY STREET','KIRTON LINDSEY','GAINSBOROUGH','DN21 4PE','01454587554','1932-06-27','Male',9476718927,000012,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (13,'MR','Jeremy','SHAIN','24 HIGH STREET','HAXEY','DONCASTER','DN9 2HH','01454587554','1928-02-06','Male',9476718935,000013,1,3,'2016-07-25 12:00:00',FALSE,FALSE,NULL,'S','1'),
  (15,'MR','Greg','HOPLEY','103 ENDERBY ROAD','','SCUNTHORPE','DN17 2JL','01454587554','1929-03-15','Female',9476718951,000015,1,3,'2016-07-25 12:00:00',TRUE,FALSE,NULL,'S','1'),
  (16,'MR','Donald','DOHERTY','12 WINDINGS STREET','ROTHERHAM','','S25 4RG','0116235689','2017-05-20','Male',9000000181,000016,1,3,'2017-05-23 12:00:00',TRUE,FALSE,NULL,'S','1'),
  (17,'MRS','Claire','JOHNSON','6a BURTON GARDENS','YORK','','Y012 6HN','01904569872','1979-11-12','Female',9000000173,000017,1,3,'2017-07-25 12:00:00',TRUE,TRUE,'2016-07-13 18:23:36','M','1'),
  (18,'MR','Donald','DOHERTY','12 WINDINGS STREET','ROTHERHAM','','S25 4RG','0116235689','2017-05-20','Male',9000000181,000018,1,3,'2017-05-23 12:00:00',TRUE,FALSE,NULL,'S','1'),
  (19,'PNA','PNA','PNA','PNA','PNA','','PNA','PNA','2017-05-20','Male',9000000184,000018,1,3,'2017-05-23 12:00:00',TRUE,FALSE,NULL,'M','1');
UNLOCK TABLES;