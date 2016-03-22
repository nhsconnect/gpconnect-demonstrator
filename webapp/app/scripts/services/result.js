'use strict';

angular.module('rippleDemonstrator')
  .factory('Result', function ($http) {

    var get = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/labresults/' + compositionId + '?source=' + source);
    };

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/labresults');
    };

    return {
      all: all,
      get: get
    };

  });
