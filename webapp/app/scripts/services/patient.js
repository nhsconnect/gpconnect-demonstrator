'use strict';

angular.module('gpConnect')
        .factory('PatientService', function ($rootScope, $http, FhirEndpointLookup, $cacheFactory, fhirJWTFactory, ProviderRouting) {

            var findAllSummaries = function () {
                return $http.get(ProviderRouting.defaultPractice().apiEndpointURL + '/patients');
            };

            var getSummary = function (patientId) {
                return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.post(endpointLookupResult.restUrlPrefix + '/Patient/$gpc.getcarerecord',
                            '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "' + patientId + '" }},{"name" : "recordSection","valueString" : "SUM"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}',
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

            var getPatientFhirId = function (patientId) {
                var patientFhirIdCache = $cacheFactory.get('patientFhirIdCache');
                if (patientFhirIdCache == undefined) {
                    patientFhirIdCache = $cacheFactory('patientFhirIdCache');
                }
                var patientFhirId = patientFhirIdCache.get(patientId);
                if (patientFhirId == undefined) {
                    return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient").then(function (endpointResponse) {
                        var endpointLookupResult = endpointResponse;
                        var partientLookupResponse = $http.get(endpointLookupResult.restUrlPrefix + '/Patient?identifier=http://fhir.nhs.net/Id/nhs-number|' + patientId,
                                {
                                    headers: {
                                        'Ssp-From': endpointLookupResult.fromASID,
                                        'Ssp-To': endpointLookupResult.toASID,
                                        'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient",
                                        'Ssp-TraceID': fhirJWTFactory.guid(),
                                        'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                                    }
                                }
                        ).then(function (response) {
                            return response.data.entry[0].resource.id;
                        });

                        patientFhirIdCache.put(patientId, partientLookupResponse);
                        return partientLookupResponse;
                    });
                } else {
                    return patientFhirId;
                }
            };

            var getFhirPatient = function (practiceOdsCode, patientId) {
                var response;
                var patientFhirCache = $cacheFactory.get('patientFhirCache');
                if (patientFhirCache == undefined) {
                    patientFhirCache = $cacheFactory('patientFhirCache');
                }
                var patientFhir = patientFhirCache.get(patientId);
                if (patientFhir == undefined) {

                    $rootScope.patientOdsCode = practiceOdsCode;

                    return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient").then(function (response) {
                        var endpointLookupResult = response;
                        var response = $http.get(endpointLookupResult.restUrlPrefix + '/Patient?identifier=http://fhir.nhs.net/Id/nhs-number|' + patientId,
                                {
                                    headers: {
                                        'Ssp-From': endpointLookupResult.fromASID,
                                        'Ssp-To': endpointLookupResult.toASID,
                                        'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient",
                                        'Ssp-TraceID': fhirJWTFactory.guid(),
                                        'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                                    }
                                }
                        ).then(function (response) {
                            return response.data.entry[0].resource;
                        });
                        patientFhirCache.put(patientId, response);
                        return response;
                    });
                } else {
                    return patientFhir;
                }
            };

            return {
                findAllSummaries: findAllSummaries,
                getSummary: getSummary,
                getPatientFhirId: getPatientFhirId,
                getFhirPatient: getFhirPatient
            };

        });
