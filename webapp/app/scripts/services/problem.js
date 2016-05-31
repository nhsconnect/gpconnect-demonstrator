'use strict';

angular.module('gpConnect')
  .factory('Problem', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/problem');
    };

    var findDetails = function (patientId, problemId, source) {
      return $http.get('/api/patients/' + patientId + '/problem/' + problemId + '?source=' + source);
    };

    var findAllHTMLTables = function(patientId, source) {
        return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Problems"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    var create = function (patientId, problem) {
      return $http.post('/api/patients/' + patientId + '/problem', problem);
    };

    var update = function (patientId, problem) {
      return $http.put('/api/patients/' + patientId + '/problem', problem);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      update: update,
      create: create
    };

  });

