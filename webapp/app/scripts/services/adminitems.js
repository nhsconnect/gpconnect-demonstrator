'use strict';

angular.module('gpConnect')
  .factory('AdminItem', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/adminitems');
    };

    var findDetails = function (patientId, adminItemId) {
      return $http.get('/api/patients/' + patientId + '/adminitems/' + adminItemId);
    };

    var findAllHTMLTables = function (patientId) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Administrative Items"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    var create = function (patientId, adminItem) {
      return $http.post('/api/patients/' + patientId + '/adminitems', adminItem);
    };

    var update = function (patientId, adminItem) {
      return $http.put('/api/patients/' + patientId + '/adminitems', adminItem);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      create: create,
      update: update
    };

  });
