USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder,warningCode)
VALUES
  (9690938096 ,'2016-07-01 12:00:00',"Cured","Swollen lips, tongue, eyes","Major", "resolved","unconfirmed","medication","13",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294505008","Amoxicillin allergy (disorder)","703630003","Red eye (finding)","","", '1','data-in-transit'),
  (9690938096 ,'2016-07-01 12:00:00',"Ongoing","Wheezing, chest tightness, shortness of breath","Major", "active","unconfirmed","medication","13",'2016-05-01 12:00:00','2016-06-01 12:00:00',"293585002","Salicylate allergy (disorder)","23924001","Tight chest (finding)","","", '1','data-in-transit');
