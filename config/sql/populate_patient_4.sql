USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,desc_code,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder)
VALUES
  (9690937308 ,'2016-07-01 12:00:00',"Cured","Allergic rhinitis","Major", "resolved","unconfirmed","environmental","4",'2016-05-01 12:00:00','2016-06-01 12:00:00',"21719001","Allergic rhinitis caused by pollen","36446019","61582004","Allergic rhinitis","102311013","", '4'),
  (9690937308 ,'2016-07-01 12:00:00',"Ongoing","Anaphylaxis","Major", "active","unconfirmed","medication","4",'2016-05-01 12:00:00','2016-06-01 12:00:00',"293585002","Salicylate allergy","433807016","39579001","Anaphylaxis (disorder)","66382015","", '4');
