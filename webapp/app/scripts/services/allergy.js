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
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Allergies and Sensitivities"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
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
