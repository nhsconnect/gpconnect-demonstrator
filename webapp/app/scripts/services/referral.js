'use strict';

angular.module('openehrPocApp')
  .factory('Referral', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/referrals');
    };

    var create = function (patientId, composition) {
      console.log('post referral comp:');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/referrals', composition);
    };

    var update = function (patientId, composition) {
      console.log('put referral comp:');
      console.log(composition);
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/referrals', composition);
    };

    return {
      all: all,
      update: update,
      create: create
    };
  });

