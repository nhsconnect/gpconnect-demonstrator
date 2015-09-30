'use strict';

angular.module('rippleDemonstrator')
  .factory('TransferOfCare', function ($http) {

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/transfers-of-care/' + compositionId);
    };

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/transfers-of-care');
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/transfers-of-care', composition);
    };

    return {
      get: get,
      all: all,
      create: create
    };

  });
