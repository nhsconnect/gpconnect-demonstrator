'use strict';

angular.module('rippleDemonstrator')
  .factory('Diagnosis', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/diagnoses');
    };

    var get = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/diagnoses/' + compositionId + '?source=' + source);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/diagnoses', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/diagnoses', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });

