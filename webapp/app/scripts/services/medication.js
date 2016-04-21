'use strict';

angular.module('gpConnect')
  .factory('Medication', function ($http) {

    var findAllMedications = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/medications');
    };

    var findMedication = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/medications/' + compositionId + '?source=' + source);
    };

    var findAllHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/medications/htmlTables' + '?source=' + source);
    };

    var createMedication = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/medications', composition);
    };

    var updateMedication = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/medications', composition);
    };

    return {
      findAllMedications: findAllMedications,
      findMedication: findMedication,
      findAllHTMLTables: findAllHTMLTables,
      createMedication: createMedication,
      updateMedication: updateMedication
    };
  });
