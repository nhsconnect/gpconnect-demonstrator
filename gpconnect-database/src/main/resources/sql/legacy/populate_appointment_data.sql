INSERT INTO gpconnect.appointment_schedules
  (id, practitionerId, identifier, typeCode, typeDescription, locationId, startDateTime, endDateTime, scheduleComment)
VALUES (1, 2, 'Schedule001', '394802001', 'General medicine', 1, '2016-03-22 10:00:00', '2016-12-22 17:59:59', 'Schedule 1 for general appointments'),
(2, 1, 'Schedule002', '394814009', 'General practice', 1, '2016-06-24 09:00:00', '2017-06-22 09:00:00', 'Schedule 2 for general appointments'),
(3, 3, 'Schedule003', '394593009', 'Medical oncology', 2, '2013-12-22 11:46:22', '2040-12-22 11:46:22', 'Schedule 3 for general appointments');