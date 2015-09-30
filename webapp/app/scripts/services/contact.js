'use strict';

angular.module('rippleDemonstrator')
  .factory('Contact', function ($http) {

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/contacts/' + compositionId);
    };

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/contacts');
    };

    var create = function (patientId, composition) {
      console.log('post composition:');
      console.log(composition);
      return $http.post('/api/patients/' + patientId + '/contacts', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/contacts', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
