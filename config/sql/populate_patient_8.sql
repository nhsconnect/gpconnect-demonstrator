USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,desc_code,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder)
VALUES
  (9690937405 ,'2016-07-01 12:00:00',"Cured","Sneezing, red eyes","Major", "resolved","unconfirmed","environmental","8",'2016-05-01 12:00:00','2016-06-01 12:00:00',"91935009","Allergy to peanuts","152306018","703630003","Red eye","3009212011","", '1'),
  (9690937405 ,'2016-07-01 12:00:00',"Ongoing","Difficulty breathing","Major", "active","unconfirmed","medication","8",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294915005","Iodophore allergy","435235012","23924001","Tight chest","23924001","", '1');
