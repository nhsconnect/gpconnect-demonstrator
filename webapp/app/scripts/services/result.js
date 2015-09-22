'use strict';

angular.module('openehrPocApp')
  .factory('Result', function ($http) {

    var patientIdOveride = 9999999000;

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/labresults/' + compositionId);
    };

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/labresults');
    };

    return {
      all: all,
      get: get
    };
  });
