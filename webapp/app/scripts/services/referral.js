'use strict';

angular.module('rippleDemonstrator')
  .factory('Referral', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/referrals');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/referrals/' + compositionId);
    };

    var create = function (patientId, composition) {
      console.log('post referral comp:');
      console.log(composition);
      return $http.post('/api/patients/' + patientId + '/referrals', composition);
    };

    var update = function (patientId, composition) {
      console.log('put referral comp:');
      console.log(composition);
      return $http.put('/api/patients/' + patientId + '/referrals', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
