'use strict';

angular.module('gpConnect')
  .value("claims", {
    "sub": "28AD8576-1948-4C84-8B5E-55FB7EE027CE",
    "given_name": "Bob",
    "family_name": "Smith",
    "email": "gpconnect@example.com",
    "date_of_birth": "19/04/2016",
    "scope": {
      "setting1": true,
      "setting2": true
    },
    "tenant_id": "HSCIC",
    "tenant_name": "GP Connect",
    "role": "gpconnect"
  });
