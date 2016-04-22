'use strict';

angular.module('gpConnect')
  .factory('Problem', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/problem');
    };

    var findDetails = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/problem/' + compositionId + '?source=' + source);
    };

    var findAllHTMLTables = function(patientId, source) {
        return $http.get('/api/patients/' + patientId + '/problem/htmlTables' + '?source=' + source);
    };
    
    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/problem', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/problem', composition);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      update: update,
      create: create
    };

  });

