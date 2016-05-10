'use strict';

angular.module('gpConnect')
  .factory('Investigation', function ($http) {

    var findAllHTMLTables = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/investigations/htmlTables');
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });
