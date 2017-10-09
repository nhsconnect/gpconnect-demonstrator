'use strict';

angular.module('gpConnect').factory('Appointment', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, Organization) {
    var findAllAppointments = function (patientNHSNumber, patientId, odsCode) {
        var odsCode = (typeof odsCode !== 'undefined') ? odsCode : $rootScope.patientOdsCode;
                
        return FhirEndpointLookup.getEndpoint(odsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.get(
                    endpointLookupResult.restUrlPrefix + '/Patient/' + patientId + '/Appointment',
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientNHSNumber),
                            'Accept': "application/json+fhir"
                        }
                    }
            );
        });
    };

    var searchForFreeSlots = function (practiceOdsCode, startDateTime, endDateTime) {
           

           
            return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:slot").then(function(response) {
                var endpointLookupResult = response;
                
                var requiredParams = 'start=ge' +startDateTime+ '&end=le' +endDateTime+ '&fb-type=free&_include=Slot:schedule';
                var optionalParams = '_include:recurse=Schedule:Practitioner&_include:recurse=Schedule:actor:Location';
                
                return $http.get(
                        endpointLookupResult.restUrlPrefix + '/Slot?' + requiredParams + '&' + optionalParams,
                        {
                            headers: {
                                'Ssp-From': endpointLookupResult.fromASID,
                                'Ssp-To': endpointLookupResult.toASID,
                                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:slot",
                                'Ssp-TraceID': fhirJWTFactory.guid(),
                                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", practiceOdsCode),
                                'Accept': "application/json+fhir",
                                'Content-Type': "application/json+fhir"
                            }
                        }
                );
            });
            

    };

    var findResourceByReference = function (practiceOdsCode, patientId, resourceReference) {
        if (resourceReference.indexOf("Location") > -1) {
            return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:location").then(function(response) {
                var endpointLookupResult = response;
                
                return $http.get(
                        endpointLookupResult.restUrlPrefix + '/' + resourceReference,
                        {
                            headers: {
                                'Ssp-From': endpointLookupResult.fromASID,
                                'Ssp-To': endpointLookupResult.toASID,
                                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location",
                                'Ssp-TraceID': fhirJWTFactory.guid(),
                                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", practiceOdsCode),
                                'Accept': "application/json+fhir"
                            }
                        }
                );
            });
        } else if (resourceReference.indexOf("Practitioner") > -1) {
            return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner").then(function(response) {
                var endpointLookupResult = response;
                
                return $http.get(
                        endpointLookupResult.restUrlPrefix + '/' + resourceReference,
                        {
                            headers: {
                                'Ssp-From': endpointLookupResult.fromASID,
                                'Ssp-To': endpointLookupResult.toASID,
                                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner",
                                'Ssp-TraceID': fhirJWTFactory.guid(),
                                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", practiceOdsCode),
                                'Accept': "application/json+fhir"
                            }
                        }
                );
            });
        }
    };

    var create = function (practiceOdsCode, patientId, appointment) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.post(
                    endpointLookupResult.restUrlPrefix + '/Appointment',
                    appointment,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment",
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId),
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Prefer': "return=representation",
                            'Accept': "application/json+fhir",
                            'Content-Type': "application/json+fhir"
                        }
                    }
            );
        });
    };

    var save = function (practiceOdsCode, patientNHSNumber, appointmentId, appointment) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.put(
                    endpointLookupResult.restUrlPrefix + '/Appointment/' + appointmentId,
                    appointment,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:update:appointment",
                            'Prefer': "return=representation",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientNHSNumber),
                            'Accept': "application/json+fhir",
                            'Content-Type': "application/json+fhir"
                        }
                    }
            );
        });
    };

    return {
        findAllAppointments: findAllAppointments,
        searchForFreeSlots: searchForFreeSlots,
        findResourceByReference: findResourceByReference,
        create: create,
        save: save
    };
});