'use strict';

angular.module('rippleDemonstrator')
  .value("claims", {
    "sub": "28AD8576-1948-4C84-8B5E-55FB7EE027CE",
    "given_name": "Bob",
    "family_name": "Smith",
    "email": "bob.smith@gmail.com",
    "scope": {
      "homeView": "chart",
      "autoAdvancedSearch": false,
      "setting2": true,
      "setting3": true
    },
    "tenant_id": "Ripple",
    "tenant_name": "Ripple Demonstrator",
    "role": "idcr"
  });
