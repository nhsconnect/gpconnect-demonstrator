'use strict';

angular.module('gpConnect')
  .factory('ClinicalItem', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/clinicalitems');
    };

    var findDetails = function (patientId, clinicalItemId) {
      return $http.get('/api/patients/' + patientId + '/clinicalitems/' + clinicalItemId);
    };

    var findAllHTMLTables = function (patientId) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Clinical Items"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    var create = function (patientId, clinicalItem) {
      return $http.post('/api/patients/' + patientId + '/clinicalitems', clinicalItem);
    };

    var update = function (patientId, clinicalItem) {
      return $http.put('/api/patients/' + patientId + '/clinicalitems', clinicalItem);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      create: create,
      update: update
    };

  });
