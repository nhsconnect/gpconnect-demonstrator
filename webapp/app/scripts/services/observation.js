'use strict';

angular.module('gpConnect')
  .factory('Observation', function ($http) {

    var findAllHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/observations/htmlTables' + '?source=' + source);
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });
