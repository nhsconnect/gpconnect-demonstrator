'use strict';

angular.module('gpConnect')
  .factory('PatientService', function ($http) {

    var findAllSummaries = function () {
      return $http.get('/api/patients');
    };

    var findDetails = function (patientId) {
      var config = { cache: true };

      return $http.get('/api/patients/' + patientId, config);
    };

    var getSummary = function (patientId) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Patient Details"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      getSummary: getSummary
    };

  });
