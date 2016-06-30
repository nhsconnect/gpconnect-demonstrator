INSERT INTO gpconnect.appointment_schedules
  (id, practitionerId, identifier, typeCode, typeDescription, locationId, startDateTime, endDateTime, scheduleComment)
VALUES (1, 2, 'Schedule001', '394802001', 'General medicine', 1, '2016-03-22 10:00:00', '2016-12-22 17:59:59', 'Schedule 1 for general appointments'),
(2, 1, 'Schedule002', '394814009', 'General practice', 1, '2016-06-24 09:00:00', '2017-06-22 09:00:00', 'Schedule 2 for general appointments'),
(3, 3, 'Schedule003', '394593009', 'Medical oncology', 2, '2013-12-22 11:46:22', '2040-12-22 11:46:22', 'Schedule 3 for general appointments');

INSERT INTO gpconnect.appointment_slots
  (id, typeCode, typeDisplay, scheduleReference, freeBusyType, startDateTime, endDateTime)
VALUES (1, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 09:00:00', '2016-06-29 09:59:59'),
(2, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 10:00:00', '2016-06-29 10:59:59'),
(3, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(4, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 13:00:00', '2016-06-29 13:59:59'),
(5, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 14:00:00', '2016-06-29 14:59:59'),
(6, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 15:00:00', '2016-06-29 15:59:59'),
(7, 408443003, 'General medical practice', 2, 'FREE', '2016-06-29 16:00:00', '2016-06-29 16:59:59'),
(8, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 10:00:00', '2016-06-29 10:29:59'),
(9, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 10:30:00', '2016-06-29 10:59:59'),
(10, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:29:59'),
(11, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 11:30:00', '2016-06-29 11:59:59'),
(12, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:29:59'),
(13, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 12:30:00', '2016-06-29 12:59:59'),
(14, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 13:00:00', '2016-06-29 13:29:59'),
(15, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 13:30:00', '2016-06-29 13:59:59');

INSERT INTO gpconnect.locations
	(id, name, site_ods_code, site_ods_code_name)
VALUES 
	(1, 'The Hepworth Surgery', 'A12345', 'THE HEPWORTH SURGERY'),
	(2, 'The Moore Surgery', 'A23456', 'THE MOORE SURGERY'),
	(3, 'The Hockey Surgery', 'A34567', 'THE HOCKNEY SURGERY'),
	(4, 'The Lowry Surgery', 'A45678', 'THE LOWRY SURGERY');