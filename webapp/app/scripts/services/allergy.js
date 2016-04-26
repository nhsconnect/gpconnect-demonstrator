'use strict';

angular.module('gpConnect')
  .factory('Allergy', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/allergies');
    };

    var findDetails = function (patientId, allergyId, source) {
      return $http.get('/api/patients/' + patientId + '/allergies/' + allergyId + '?source=' + source);
    };

    var findAllHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/allergies/htmlTables' + '?source=' + source);
    };

    var create = function (patientId, allergy) {
      return $http.post('/api/patients/' + patientId + '/allergies', allergy);
    };

    var update = function (patientId, allergy) {
      return $http.put('/api/patients/' + patientId + '/allergies', allergy);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      create: create,
      update: update
    };
  });
