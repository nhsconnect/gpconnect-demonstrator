'use strict';

angular.module('rippleDemonstrator')
  .value("claims", {
    "sub": "28AD8576-1948-4C84-8B5E-55FB7EE027CE",
    "given_name": "Bob",
    "family_name": "Smith",
    "email": "bob.smith@gmail.com",
    "date_of_birth": "10/05/1970",
    "scope": {
      "setting1": true,
      "setting2": true
    },
    "tenant_id": "HSCIC",
    "tenant_name": "GP Connect",
    "role": "idcr"
  });
