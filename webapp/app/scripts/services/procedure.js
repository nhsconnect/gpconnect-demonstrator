'use strict';

angular.module('rippleDemonstrator')
  .factory('Procedure', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/procedures');
    };

    var get = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/procedures/' + compositionId + '?source=' + source);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/procedures', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/procedures', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
