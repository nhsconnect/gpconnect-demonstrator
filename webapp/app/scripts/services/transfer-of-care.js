'use strict';

angular.module('openehrPocApp')
  .factory('TransferOfCare', function ($http) {

    var patientIdOveride = 9999999000;

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/transfers-of-care/' + compositionId);
    };

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/transfers-of-care');
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/transfers-of-care', composition);
    };

    return {
      get: get,
      all: all,
      create: create
    };
  });


