'use strict';

angular.module('openehrPocApp')
  .factory('Eolcareplan', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/eolcareplans');
    };

    var create = function (patientId, composition) {
      console.log('post composition:');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/eolcareplans', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/eolcareplans', composition);
    };

    return {
      all: all,
      update: update,
      create:create
    };
  });