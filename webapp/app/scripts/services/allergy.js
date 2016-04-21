'use strict';

angular.module('gpConnect')
  .factory('Allergy', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/allergies');
    };

    var findDetails = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/allergies/' + compositionId + '?source=' + source);
    };

    var findAllHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/allergies/htmlTables' + '?source=' + source);
    };

    var createAllergy = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/allergies', composition);
    };

    var updateAllergy = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/allergies', composition);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      createAllergy: createAllergy,
      updateAllergy: updateAllergy
    };
  });
