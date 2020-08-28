USE gpconnect1_5;
INSERT INTO allergyintolerance
  (nhsNumber,endDate,endReason,note,reactionDescription, clinicalStatus,verificationStatus,category,patientRef,onSetDateTime,assertedDate,concept_code,concept_display,manCoding,manDisplay,manDescCoding,manDescDisplay, recorder, warningCode)
VALUES
  (9690938118 ,'2016-07-01 12:00:00',"","Vomiting and diarrhoea","Major", "resolved","unconfirmed","medication","16",'2016-05-01 12:00:00','2016-06-01 12:00:00',"294716003","Biphasic insulin allergy (disorder)","49237006","Allergic diarrhea (disorder)","","", '5','confidential-items'),
/* #263 add extra intolerances with different warning types */
  (9690938118 ,'2016-07-02 12:00:00',"","Vomiting and diarrhoea","Major", "resolved","unconfirmed","medication","16",'2016-05-02 12:00:00','2016-06-02 12:00:00',"294716003","Biphasic insulin allergy (disorder)","49237006","Allergic diarrhea (disorder)","","", '5','data-in-transit');
