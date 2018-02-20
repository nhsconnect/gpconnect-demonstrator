'use strict';

angular.module('gpConnect').factory('PatientService', function ($rootScope, $http, FhirEndpointLookup, $cacheFactory, fhirJWTFactory, ProviderRouting) {
    var findAllSummaries = function() {
        return $http.get(ProviderRouting.defaultPractice().apiEndpointURL + '/patients');
    };

    var getSummary = function(patientId) {
        return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord").then(function(response) {
            var endpointLookupResult = response;

            return $http.post(
                    endpointLookupResult.restUrlPrefix + '/Patient/$gpc.getcarerecord',
                    '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "system": "http://fhir.nhs.net/Id/nhs-number", "value" : "' + patientId + '" }},{"name" : "recordSection","valueCodeableConcept" :{"coding" : [{"system":"http://fhir.nhs.net/ValueSet/gpconnect-record-section-1","code":"SUM","display":"Summary"}]}},{"name" : "timePeriod","valuePeriod" : { "start" : null, "end" : null }}]}',
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

    var getFhirPatient = function(practiceOdsCode, patientId) {
        var response;
        $rootScope.patientOdsCode = practiceOdsCode;

        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient").then(function(response) {
            var endpointLookupResult = response;
            var response = $http.get(
                    endpointLookupResult.restUrlPrefix + '/Patient?identifier=http://fhir.nhs.net/Id/nhs-number%7C' + patientId,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId),
                            'Accept': "application/json+fhir"
                        }
                    }
            ).then(function(response) {
                if (response.data != undefined && response.data.entry != undefined) {
                    return response.data.entry[0].resource;
                } else {
                    return undefined;
                }
            });

            return response;
        });
    };

    return {
        findAllSummaries: findAllSummaries,
        getSummary: getSummary,
        getFhirPatient: getFhirPatient
    };
});