'use strict';

angular.module('gpConnect').factory('PatientService', ['$rootScope', '$http', 'FhirEndpointLookup', '$cacheFactory', 'fhirJWTFactory', 'ProviderRouting', 'gpcResource', function ($rootScope, $http, FhirEndpointLookup, $cacheFactory, fhirJWTFactory, ProviderRouting, gpcResource) {
    var findAllSummaries = function() {
        return $http.get(ProviderRouting.defaultPractice().apiEndpointURL + '/patients');
    };

    var getSummary = function(patientId) {
        return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord-1").then(function(response) {
            var endpointLookupResult = response;

            return $http.post(
                    endpointLookupResult.restUrlPrefix + '/Patient/$gpc.getcarerecord',
                    '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "system": "'+gpcResource.getConst("ID_NHS_NUMBER")+'", "value" : "' + patientId + '" }},{"name" : "recordSection","valueCodeableConcept" :{"coding" : [{"system":"'+gpcResource.getConst("VS_GPC_RECORD_SECTION")+'","code":"SUM","display":"Summary"}]}},{"name" : "timePeriod","valuePeriod" : { "start" : null, "end" : null }}]}',
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

    var getPatientFhirId = function(patientId, odsCode) {
        var odsCode = (typeof odsCode !== 'undefined') ? odsCode : $rootScope.patientOdsCode;

        return FhirEndpointLookup.getEndpoint(odsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient-1").then(function(endpointResponse) {
            var endpointLookupResult = endpointResponse;

            var partientLookupResponse = $http.get(
                    endpointLookupResult.restUrlPrefix + '/Patient?identifier='+gpcResource.getConst("ID_NHS_NUMBER")+'%7C' + patientId,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient-1",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId),
                            'Accept': "application/json+fhir"
                        }
                    }
            ).then(function(response) {
                if (response.data.entry != undefined) {
                    return response.data.entry[0].resource.id;
                } else {
                    return undefined;
                }
            });

            return partientLookupResponse;
        });
    };

    var getFhirPatient = function(practiceOdsCode, patientId) {
        var response;
        $rootScope.patientOdsCode = practiceOdsCode;

        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient-1").then(function(response) {
            var endpointLookupResult = response;
            var response = $http.get(
                    endpointLookupResult.restUrlPrefix + '/Patient?identifier='+gpcResource.getConst("ID_NHS_NUMBER")+'%7C' + patientId,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient-1",
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

    var registerPatient = function(practiceOdsCode, requestParameters, patientId) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:operation:gpc.registerpatient-1").then(function(endpointLookupResult) {
            return $http.post(
                    endpointLookupResult.restUrlPrefix + '/Patient/$gpc.registerpatient', requestParameters,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.registerpatient-1",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId),
                            'Accept': "application/json+fhir",
                            'Content-Type': "application/json+fhir"
                        }
                    }
            ).then(function(response) {
                return response.data;
            });
        });
    };

    return {
        findAllSummaries: findAllSummaries,
        getSummary: getSummary,
        getPatientFhirId: getPatientFhirId,
        getFhirPatient: getFhirPatient,
        registerPatient: registerPatient
    };
}]);