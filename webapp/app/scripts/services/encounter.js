'use strict';

angular.module('gpConnect')
  .factory('Encounter', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory) {

    var findAllHTMLTables = function (patientId, fromDate, toDate) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord").then(function (response) {
        var endpointLookupResult = response;
        return $http.post(endpointLookupResult.restUrlPrefix+'/Patient/$gpc.getcarerecord',
          '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueCodeableConcept" :{"coding" : [{"system":"http://fhir.nhs.net/ValueSet/gpconnect-record-section-1","code":"ENC","display":"Encounters"}]}},{"name" : "timePeriod","valuePeriod" : { "start" : "'+fromDate+'", "end" : "'+toDate+'" }}]}',
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
