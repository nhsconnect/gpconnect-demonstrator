'use strict';

angular.module('gpConnect')
  .factory('Result', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/labresults');
    };

    var findDetails = function (patientId, labResultId, source) {
      return $http.get('/api/patients/' + patientId + '/labresults/' + labResultId + '?source=' + source);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails
    };

  });
