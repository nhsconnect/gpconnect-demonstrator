'use strict';

angular.module('rippleDemonstrator')
  .value("claims", {
    "sub": "28AD8576-1948-4C84-8B5E-55FB7EE027CE",
    "given_name": "Bob",
    "family_name": "Smith",
    "email": "bob.smith@gmail.com",
    "scope": {
      "homeView": "search",
      "autoAdvancedSearch": true,
      "setting2": true,
      "setting3": true
    },
    "tenant_id": "STFT",
    "tenant_name": "South Tyneside NHS Trust",
    "role": "idcr"
});
