USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,desc_code,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder)
VALUES
  (9690937383 ,'2016-07-01 12:00:00',"Cured","Sneezing, red eyes","Major", "resolved","unconfirmed","environmental","6",'2016-05-01 12:00:00','2016-06-01 12:00:00',"21719001","Allergic rhinitis caused by pollen","36446019","61582004","Allergic rhinitis","102311013","", '2'),
  (9690937383 ,'2016-07-01 12:00:00',"Ongoing","Difficulty breathing","Major", "active","unconfirmed","medication","6",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy","23924001","435012016","Tight chest","23924001","", '2');
