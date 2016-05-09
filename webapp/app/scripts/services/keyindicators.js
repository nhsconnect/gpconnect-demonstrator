'use strict';

angular.module('gpConnect')
  .factory('KeyIndicator', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/keyindicators');
    };

    var findDetails = function (patientId, keyIndicatorId) {
      return $http.get('/api/patients/' + patientId + '/keyindicators/' + keyIndicatorId);
    };

    var findAllHTMLTables = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/keyindicators/htmlTables');
    };

    var create = function (patientId, keyIndicator) {
      return $http.post('/api/patients/' + patientId + '/keyindicators', keyIndicaotr);
    };

    var update = function (patientId, keyIndicator) {
      return $http.put('/api/patients/' + patientId + '/keyindicators', keyIndicator);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      create: create,
      update: update
    };

  });
