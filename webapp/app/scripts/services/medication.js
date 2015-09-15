'use strict';

angular.module('openehrPocApp')
  .factory('Medication', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/medications');
    };
    
     var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/medications/' + compositionId);
    };

    var create = function (patientId, composition) {
      console.log('post medication comp:');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/medications', composition);
    };

    var update = function (patientId, composition) {
      console.log('put medication comp:');
      console.log(composition);
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/medications', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };
  });

