'use strict';

angular.module('gpConnect')
  .factory('Observation', function ($http) {

    var findAllHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/observations/htmlTables' + '?source=' + source);
    };

    var findAllInvestigationHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/investigations/htmlTables');
    };

    return {
      findAllHTMLTables: findAllHTMLTables,
      findAllInvestigationHTMLTables: findAllInvestigationHTMLTables
    };

  });
