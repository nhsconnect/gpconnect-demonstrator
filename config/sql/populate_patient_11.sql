USE gpconnect1;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder)
VALUES
  (9658220223 ,'2016-07-01 12:00:00',"Cured","Swollen lips, tongue, eyes","Major", "resolved","unconfirmed","environmental","11",'2016-05-01 12:00:00','2016-06-01 12:00:00',"91935009","Allergy to peanuts (disorder)","68670009","Contact dermatitis of eyelid (disorder)","","", '4'),
  (9658220223 ,'2016-07-01 12:00:00',"Ongoing","Wheezing, chest tightness, shortness of breath","Major", "active","unconfirmed","medication","11",'2016-05-01 12:00:00','2016-06-01 12:00:00',"293585002","Salicylate allergy (disorder)","23924001","Tight chest (finding)","","", '4');
