'use strict';

angular.module('gpConnect')
  .factory('ClinicalItem', ['$rootScope', '$http', 'FhirEndpointLookup', 'fhirJWTFactory', 'gpcResource', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, gpcResource) {
      
    var findAllHTMLTables = function (patientId,fromDate, toDate) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord-1").then(function (response) {
        var endpointLookupResult = response;
        return $http.post(endpointLookupResult.restUrlPrefix+'/Patient/$gpc.getcarerecord',
              '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "system": "'+gpcResource.getConst("ID_NHS_NUMBER")+'", "value" : "'+patientId+'" }},{"name" : "recordSection","valueCodeableConcept" :{"coding" : [{"system":"'+gpcResource.getConst("VS_GPC_RECORD_SECTION")+'","code":"CLI","display":"ClinicalItems"}]}},{"name" : "timePeriod","valuePeriod" : { "start" : "'+fromDate+'", "end" : "'+toDate+'" }}]}',
              {
                  headers: {
                      'Ssp-From': endpointLookupResult.fromASID,
                      'Ssp-To': endpointLookupResult.toASID,
                      'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord-1",
                      'Ssp-TraceID': fhirJWTFactory.guid(),
                      'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId),
                      'Accept': "application/json+fhir",
                      'Content-Type': "application/json+fhir"
                  }
              }
              );
        });
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  }]);
