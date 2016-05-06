'use strict';

angular.module('gpConnect')
  .factory('Procedure', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/procedures');
    };

    var findDetails = function (patientId, procedureId, source) {
      return $http.get('/api/patients/' + patientId + '/procedures/' + procedureId + '?source=' + source);
    };

    var create = function (patientId, procedure) {
      return $http.post('/api/patients/' + patientId + '/procedures', procedure);
    };

    var update = function (patientId, procedure) {
      return $http.put('/api/patients/' + patientId + '/procedures', procedure);
    };

    var findAllHTMLTables = function(patientId, source) {
      return $http.get('/api/patients/' + patientId + '/procedures/htmlTables' + '?source=' + source);
    };


    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      update: update,
      create: create,
      findAllHTMLTables: findAllHTMLTables
    };

  });
