'use strict';

angular.module('gpConnect')
        .factory('Appointment', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, Organization) {

            var findAllAppointments = function (patientNHSNumber, patientId, odsCode) {
    			var odsCode = (typeof odsCode !== 'undefined') ?  odsCode : $rootScope.patientOdsCode;
                
                return FhirEndpointLookup.getEndpoint(odsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.get(endpointLookupResult.restUrlPrefix + '/Patient/' + patientId + '/Appointment',
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments",
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientNHSNumber)
                                }
                            });
                });
            };

            var getScheduleOperation = function (organizationOdsCode, startDateTime, endDateTime, patientId) {
                // Lookup Endpoint Organization localId "organizationId"
                return Organization.searchForOrganisation(organizationOdsCode, patientId, organizationOdsCode).then(function (organizationLookupResponse) {
                    if (organizationLookupResponse.data.entry != undefined) {
                        var organizationId = organizationLookupResponse.data.entry[0].resource.id;
                        return FhirEndpointLookup.getEndpoint(organizationOdsCode, "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule").then(function (response) {
                            var endpointLookupResult = response;
                            return $http.post(endpointLookupResult.restUrlPrefix + '/Organization/' + organizationId + '/$gpc.getschedule',
                                    '{ "resourceType" : "Parameters", "parameter" : [ { "name" : "timePeriod", "valuePeriod" : { "start" : "' + startDateTime + '", "end" : "' + endDateTime + '" } } ] }',
                                    {
                                        headers: {
                                            'Ssp-From': endpointLookupResult.fromASID,
                                            'Ssp-To': endpointLookupResult.toASID,
                                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule",
                                            'Ssp-TraceID': fhirJWTFactory.guid(),
                                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", organizationOdsCode)
                                        }
                                    });
                        });
                    }
                });

            };

            var findResourceByReference = function (practiceOdsCode, patientId, resourceReference) {
                if (resourceReference.indexOf("Location") > -1) {
                    return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:location").then(function (response) {
                        var endpointLookupResult = response;
                        return $http.get(endpointLookupResult.restUrlPrefix + '/' + resourceReference,
                                {
                                    headers: {
                                        'Ssp-From': endpointLookupResult.fromASID,
                                        'Ssp-To': endpointLookupResult.toASID,
                                        'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location",
                                        'Ssp-TraceID': fhirJWTFactory.guid(),
                                        'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                                    }
                                });
                    });
                } else if (resourceReference.indexOf("Practitioner") > -1) {
                    return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner").then(function (response) {
                        var endpointLookupResult = response;
                        return $http.get(endpointLookupResult.restUrlPrefix + '/' + resourceReference,
                                {
                                    headers: {
                                        'Ssp-From': endpointLookupResult.fromASID,
                                        'Ssp-To': endpointLookupResult.toASID,
                                        'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner",
                                        'Ssp-TraceID': fhirJWTFactory.guid(),
                                        'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                                    }
                                });
                    });
                }
            };

            var create = function (practiceOdsCode, patientId, appointment) {
                return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.post(endpointLookupResult.restUrlPrefix + '/Appointment',
                            appointment,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment",
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId),
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Prefer': "return=representation"
                                }
                            });
                });
            };

            var save = function (practiceOdsCode, patientNHSNumber, appointmentId, appointment) {
                return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.put(endpointLookupResult.restUrlPrefix + '/Appointment/' + appointmentId,
                            appointment,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment",
                                    'Prefer': "return=representation",
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientNHSNumber)
                                }
                            });
                });
            };

            return {
                findAllAppointments: findAllAppointments,
                getScheduleOperation: getScheduleOperation,
                findResourceByReference: findResourceByReference,
                create: create,
                save: save
            };

        });
