USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,desc_code,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder)
VALUES
  (9690937391 ,'2016-07-01 12:00:00',"Cured","Sneezing, red eyes","Major", "resolved","unconfirmed","environmental","7",'2016-05-01 12:00:00','2016-06-01 12:00:00',"21719001","Allergic rhinitis caused by pollen","36446019","61582004","Allergic rhinitis","102311013","",'3'),
  (9690937391 ,'2016-07-01 12:00:00',"Ongoing","Difficulty breathing","Major", "active","unconfirmed","medication","7",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy","435012016","405720007","Allergic bronchitis","2157501011","", '3');
