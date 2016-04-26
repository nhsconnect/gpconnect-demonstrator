'use strict';

angular.module('gpConnect')
  .factory('PatientService', function ($http) {

    var findAllSummaries = function () {
      return $http.get('/api/patients');
    };

    var findDetails = function (patientId) {
      var config = { cache: true };

      return $http.get('/api/patients/' + patientId, config);
    };

    var getSummary = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/patientsummary/htmlTables');
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      getSummary: getSummary
    };

  });
