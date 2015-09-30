'use strict';

angular.module('rippleDemonstrator')
  .factory('Appointment', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/appointments');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/appointments/' + compositionId);
    };

    var create = function (patientId, composition) {
      console.log('post appointment comp:');
      console.log(composition);
      return $http.post('/api/patients/' + patientId + '/appointments', composition);
    };

    var update = function (patientId, composition) {
      console.log('put appointment comp:');
      console.log(composition);
      return $http.put('/api/patients/' + patientId + '/appointments', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
