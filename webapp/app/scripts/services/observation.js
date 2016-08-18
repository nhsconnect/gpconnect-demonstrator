'use strict';

angular.module('gpConnect')
  .factory('Observation', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory) {

    var findAllHTMLTables = function(patientId, source) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord").then(function (response) {
        var endpointLookupResult = response;
        return $http.post(endpointLookupResult.restUrlPrefix+'/Patient/$gpc.getcarerecord',
          '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "OBS"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}',
          {
              headers: {
                  'Ssp-From': endpointLookupResult.fromASID,
                  'Ssp-To': endpointLookupResult.toASID,
                  'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord",
                  'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
              }
          }
          );
      });
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });
