'use strict';

angular.module('gpConnect')
  .factory('Immunisation', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory) {

    var findAllHTMLTables = function (patientId) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord").then(function (response) {
        var endpointLookupResult = response;
        return $http.post(endpointLookupResult.restUrlPrefix+'/Patient/$gpc.getcarerecord',
          '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "IMM"},{"name" : "timePeriod","valuePeriod" : { "start" : null, "end" : null }}]}',
          {
              headers: {
                  'Ssp-From': endpointLookupResult.fromASID,
                  'Ssp-To': endpointLookupResult.toASID,
                  'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord",
                  'Ssp-TraceID': fhirJWTFactory.guid(),
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
