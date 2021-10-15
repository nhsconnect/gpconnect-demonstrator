USE gpconnect1_2_8;
INSERT INTO practitioners
  (id,userid,p_role_ids,p_name_family,p_name_given,p_name_prefix,p_gender,p_organization_id,p_role_code,p_role_display,p_com_code,p_com_display,lastUpdated)
VALUES
  (1,'G13579135','','Gilbert','Nichole','Miss','FEMALE',7,'R0050','Consultant','de|en','German|English','2016-07-25 12:00:00'),
  (2,'G22345655','PT1122|PT1234','Slater','Kibo','Mr','MALE',1,'R0050','Consultant','de|en','German|English','2016-07-25 12:00:00'),
  (3,'G11111116','PT1234','Parsons','Melissa','Mrs','FEMALE',2,'R0042','paediatrician','de','German','2016-07-25 12:00:00'),
  (4,'G22222226','PT3333','Parsons','Melissa','Mrs','FEMALE',2,'R0042','paediatrician','en','English','2016-07-25 12:00:00'),
  (5,'G22222226','PT2222|PT4444','Parsons','Melissa','Mrs','FEMALE',2,'R0042','paediatrician','en','English','2016-07-25 12:00:00');
