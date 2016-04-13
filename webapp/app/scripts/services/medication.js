'use strict';

angular.module('rippleDemonstrator')
  .factory('Medication', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/medications');
    };

    var get = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/medications/' + compositionId + '?source=' + source);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/medications', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/medications', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
