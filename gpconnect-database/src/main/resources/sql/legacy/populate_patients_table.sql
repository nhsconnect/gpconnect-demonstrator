LOCK TABLES gpconnect.patients WRITE;

INSERT INTO gpconnect.patients
  (id,    title,   first_name,   last_name,    address_1,                              address_2,            address_3,            postcode,   phone,              date_of_birth, gender,   nhs_number,  pas_number,   department_id, gp_id, lastUpdated)
VALUE
  (1,     'Mr',    'Ivor',       'Cox',        '6948 Et St.',                          'Halesowen',          'Worcestershire',     'VX27 5DV', '(011981) 32362',   '1944-06-06',  'Male',   9000000009,  352541,       1,             3, '2016-07-25 12:00:00'),
  (2,     'Mr',    'Ivor',       'Cox',        'Ap #126-6226 Mi. St.',                 'Oakham',             'Rutland',            'XM9 4RF',  '(0112) 740 5408',  '1944-06-06',  'Male',   9000000025,  623454,       1,             1, '2016-07-25 12:00:00'),
  (3,     'Mrs',   'Larissa',    'Mathews',    'P.O. Box 138, 7496 Cursus Avenue',     'Selkirk',            'Selkirkshire',       'X09 3OC',  '0800 1111',        '1937-09-28',  'Female', 9000000017,  346574,       2,             2, '2016-07-25 12:00:00'),
  (4,     'Miss',    'Freya',      'Blackwell',  'P.O. Box 306, 6801 Tellus Street',     'Kirkby Lonsdale',    'Westmorland',        'P32 4GY',  '07624 647524',     '1980-02-03',  'Female', 9000000033,  332546,       1,             1, '2016-07-25 12:00:00'),
  (5,     'Mr',   'Brad',     'Case',       'Ap #229-5050 Egestas Avenue',          'St. Albans',         'Hertfordshire',      'B9 8YO',   '0964 934 2028',    '1958-05-14',  'Male',   9000000041,  345267,       3,             1, '2016-07-25 12:00:00'),
  (6,     'Mr',    'Rashad',     'Hill',       '288-6235 Odio. Street',                'Stockton-on-Tees',   'Durham',             'GF7 2OE',  '0909 764 2554',    '1952-02-19',  'Male',   9000000068,  798311,       3,             2, '2016-07-25 12:00:00'),
  (7,     'Mrs',   'Ezra',       'Gordon',     'P.O. Box 711, 8725 Purus Rd.',         'Grangemouth',        'Stirlingshire',      'B4 8MW',   '070 6691 5178',    '1958-06-24',  'Female',   9000000076,  595941,       4,             3, '2016-07-25 12:00:00'),
  (8,     'Mrs',   'Blair',      'Wells',      '783-2544 Cursus, Ave',                 'Lerwick',            'Shetland',           'H2 6HJ',   '(01141) 56013',    '1969-01-31',  'Female', 9000000084,  841652,       4,             1, '2016-07-25 12:00:00');

UNLOCK TABLES;