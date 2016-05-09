'use strict';

angular.module('gpConnect')
  .factory('Immunisation', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/immunisations');
    };

    var findDetails = function (patientId, immunisationId) {
      return $http.get('/api/patients/' + patientId + '/immunisations/' + immunisationId);
    };

    var findAllHTMLTables = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/immunisations/htmlTables');
    };

    var create = function (patientId, immunisation) {
      return $http.post('/api/patients/' + patientId + '/immunisations', immunisation);
    };

    var update = function (patientId, immunisation) {
      return $http.put('/api/patients/' + patientId + '/immunisations', immunisation);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      create: create,
      update: update
    };

  });
