'use strict';

angular.module('rippleDemonstrator')
  .factory('Eolcareplan', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/eolcareplans');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/eolcareplans/' + compositionId);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/eolcareplans', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/eolcareplans', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
