'use strict';

angular.module('gpConnect')
  .factory('PatientService', function ($http, EnvConfig) {

    var findAllSummaries = function () {
      return $http.get('/api/patients');
    };

    var findDetails = function (patientId) {
      var config = { cache: true };

      return $http.get('/api/patients/' + patientId, config);
    };

    var getSummary = function (patientId) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Summary"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };
    
    var getPatientFhirId = function (patientId) {
      var response;
      return $http.get('/fhir/Patient?patientId='+patientId).then(function(response) {
         return response.data.entry[0].resource.id;
      });
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      getSummary: getSummary,
      getPatientFhirId: getPatientFhirId
    };

  });
