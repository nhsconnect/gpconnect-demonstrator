'use strict';

angular.module('gpConnect').factory('Immunisation', ['$rootScope', '$http', 'FhirEndpointLookup', 'fhirJWTFactory', 'gpcResource', function($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, gpcResource) {
    var findAllHTMLTables = function (patientId) {
        return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.post(
                    endpointLookupResult.restUrlPrefix+'/Patient/$gpc.getcarerecord',
                    '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "system": "'+gpcResource.getConst("ID_NHS_NUMBER")+'", "value" : "'+patientId+'" }},{"name" : "recordSection","valueCodeableConcept" :{"coding" :[{"system":"'+gpcResource.getConst("VS_GPC_RECORD_SECTION")+'","code":"IMM","display":"Immunisation"}]}},{"name" : "timePeriod","valuePeriod" : { "start" : null, "end" : null }}]}',
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord",
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