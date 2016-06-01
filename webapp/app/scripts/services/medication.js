'use strict';

angular.module('gpConnect')
  .factory('Medication', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/medications');
    };

    var findDetails = function (patientId, medicationId, source) {
      return $http.get('/api/patients/' + patientId + '/medications/' + medicationId + '?source=' + source);
    };

    var findAllHTMLTables = function(patientId, source) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Medications"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    var create = function (patientId, medication) {
      return $http.post('/api/patients/' + patientId + '/medications', medication);
    };

    var update = function (patientId, medication) {
      return $http.put('/api/patients/' + patientId + '/medications', medication);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      create: create,
      update: update
    };

  });
