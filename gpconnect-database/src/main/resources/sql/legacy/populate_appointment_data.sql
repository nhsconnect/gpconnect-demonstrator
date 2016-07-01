INSERT INTO gpconnect.appointment_schedules
  (id, practitionerId, identifier, typeCode, typeDescription, locationId, startDateTime, endDateTime, scheduleComment)
VALUES (1, 2, 'Schedule001', '394802001', 'General medicine', 1, '2016-03-22 10:00:00', '2016-12-22 17:59:59', 'Schedule 1 for general appointments'),
(2, 1, 'Schedule002', '394814009', 'General practice', 1, '2016-06-24 09:00:00', '2017-06-22 09:00:00', 'Schedule 2 for general appointments'),
(3, 3, 'Schedule003', '394593009', 'Medical oncology', 2, '2013-12-22 11:46:22', '2040-12-22 11:46:22', 'Schedule 3 for general appointments'),

(4, 1, 'Schedule004', '394814009', 'General practice', 1, '2013-12-22 11:46:22', '2040-12-22 11:46:22', 'Schedule 4 for general appointments with practitioner Goff, Carolyn D. @ The Hepworth Surgery Main Building'),
(5, 2, 'Schedule005', '394814009', 'General practice', 1, '2013-12-22 10:46:22', '2040-12-22 10:46:22', 'Schedule 5 for general appointments with practitioner Cash, Claire F. @ The Hepworth Surgery Main Building'),
(6, 3, 'Schedule006', '394814009', 'General practice', 2, '2013-12-22 09:46:22', '2040-12-22 09:46:22', 'Schedule 6 for general appointments with practitioner Spencer, Hyacinth A. @ The Moore Surgery Main Building'),
(7, 4, 'Schedule007', '394814009', 'General practice', 3, '2013-12-22 09:46:22', '2040-12-22 09:46:22', 'Schedule 7 for general appointments with practitioner Bailey, Demetrius B. @ The Hockey Surgery Main Building'),
(8, 5, 'Schedule008', '394814009', 'General practice', 3, '2013-12-22 10:46:22', '2040-12-22 10:46:22', 'Schedule 8 for general appointments with practitioner Guthrie, Indigo D. @ The Hockey Surgery Main Building'),
(9, 6, 'Schedule009', '394814009', 'General practice', 4, '2013-12-22 09:46:22', '2040-12-22 09:46:22', 'Schedule 9 for general appointments with practitioner Hudson, Inez G. @ The Hockey Surgery Annex'),
(10, 7, 'Schedule010', '394814009', 'General practice', 4, '2013-12-22 10:46:22', '2040-12-22 10:46:22', 'Schedule 10 for general appointments with practitioner Joseph, May N. @ The Hockey Surgery Annex');

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
(15, 394592004, 'Clinical oncology', 1, 'FREE', '2016-06-29 13:30:00', '2016-06-29 13:59:59'),
(16, 408443003, 'General medical practice', 4, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:59:59'),
(17, 408443003, 'General medical practice', 4, 'FREE', '2016-06-29 13:00:00', '2016-06-29 13:59:59'),
(18, 408443003, 'General medical practice', 4, 'FREE', '2016-06-29 14:00:00', '2016-06-29 14:59:59'),
(19, 408443003, 'General medical practice', 5, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(20, 408443003, 'General medical practice', 5, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:59:59'),
(21, 408443003, 'General medical practice', 5, 'FREE', '2016-06-29 13:00:00', '2016-06-29 13:59:59'),
(22, 408443003, 'General medical practice', 6, 'FREE', '2016-06-29 09:00:00', '2016-06-29 09:59:59'),
(23, 408443003, 'General medical practice', 6, 'FREE', '2016-06-29 10:00:00', '2016-06-29 10:59:59'),
(24, 408443003, 'General medical practice', 6, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(25, 408443003, 'General medical practice', 7, 'FREE', '2016-06-29 10:00:00', '2016-06-29 10:59:59'),
(26, 408443003, 'General medical practice', 7, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(27, 408443003, 'General medical practice', 7, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:59:59'),
(28, 408443003, 'General medical practice', 8, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(29, 408443003, 'General medical practice', 8, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:59:59'),
(30, 408443003, 'General medical practice', 8, 'FREE', '2016-06-29 13:00:00', '2016-06-29 13:59:59'),
(31, 408443003, 'General medical practice', 9, 'FREE', '2016-06-29 10:00:00', '2016-06-29 10:59:59'),
(32, 408443003, 'General medical practice', 9, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(33, 408443003, 'General medical practice', 9, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:59:59'),
(34, 408443003, 'General medical practice', 10, 'FREE', '2016-06-29 11:00:00', '2016-06-29 11:59:59'),
(35, 408443003, 'General medical practice', 10, 'FREE', '2016-06-29 12:00:00', '2016-06-29 12:59:59'),
(36, 408443003, 'General medical practice', 10, 'FREE', '2016-06-29 13:00:00', '2016-06-29 13:59:59');

INSERT INTO gpconnect.locations
	(id, name, site_ods_code, site_ods_code_name)
VALUES 
	(1, 'Main Building', 'Z33435', 'MAIN BUILDING'),
	(2, 'Main Building', 'Z33436', 'MAIN BUILDING'),
	(3, 'Main Building', 'Z33437', 'MAIN BUILDING'),
	(4, 'Annex',         'Z33437', 'ANNEX');