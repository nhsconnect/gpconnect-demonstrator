'use strict';

angular.module('gpConnect').factory('Appointment', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, Organization) {
	
    var findAllAppointments = function (patientNHSNumber, patientId, odsCode, startDateTime, endDateTime) {
        var odsCode = (typeof odsCode !== 'undefined') ? odsCode : $rootScope.patientOdsCode;
                
        return FhirEndpointLookup.getEndpoint(odsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments-1").then(function(response) {
            var endpointLookupResult = response;
            
            var startDate = new Date(startDateTime);
            var endDate = new Date(endDateTime);
            
            var requiredParams = 'start=ge' + startDate.toISOString().split('T')[0] + '&start=le' + endDate.toISOString().split('T')[0];
            
            return $http.get(
                    endpointLookupResult.restUrlPrefix + '/Patient/' + patientId + '/Appointment?' + requiredParams,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments-1",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientNHSNumber),
                            'Accept': "application/fhir+json"
                        }
                    }
            );
        });
    };

    var searchForFreeSlots = function (practiceOdsCode, startDateTime, endDateTime) {
           
            return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:slot-1").then(function(response) {
                var endpointLookupResult = response;
                
                var requiredParams = 'start=ge' +startDateTime+ '&end=le' +endDateTime+ '&fb-type=free&_include=Slot:schedule';
                var optionalParams = '_include:recurse=Schedule:actor:Practitioner&_include:recurse=Schedule:actor:Location';
                
                return $http.get(
                        endpointLookupResult.restUrlPrefix + '/Slot?' + requiredParams + '&' + optionalParams,
                        {
                            headers: {
                                'Ssp-From': endpointLookupResult.fromASID,
                                'Ssp-To': endpointLookupResult.toASID,
                                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:slot-1",
                                'Ssp-TraceID': fhirJWTFactory.guid(),
                                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", practiceOdsCode),
                                'Accept': "application/fhir+json",
                                'Content-Type': "application/fhir+json"
                            }
                        }
                );
            });
            

    };

    var findResourceByReference = function (practiceOdsCode, patientId, resourceReference) {
        if (resourceReference.indexOf("Location") > -1) {
            return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:location-1").then(function(response) {
                var endpointLookupResult = response;
                
                return $http.get(
                        endpointLookupResult.restUrlPrefix + '/' + resourceReference,
                        {
                            headers: {
                                'Ssp-From': endpointLookupResult.fromASID,
                                'Ssp-To': endpointLookupResult.toASID,
                                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location-1",
                                'Ssp-TraceID': fhirJWTFactory.guid(),
                                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", practiceOdsCode),
                                'Accept': "application/fhir+json"
                            }
                        }
                );
            });
        } else if (resourceReference.indexOf("Practitioner") > -1) {
            return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner-1").then(function(response) {
                var endpointLookupResult = response;
                
                return $http.get(
                        endpointLookupResult.restUrlPrefix + '/' + resourceReference,
                        {
                            headers: {
                                'Ssp-From': endpointLookupResult.fromASID,
                                'Ssp-To': endpointLookupResult.toASID,
                                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner-1",
                                'Ssp-TraceID': fhirJWTFactory.guid(),
                                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", practiceOdsCode),
                                'Accept': "application/fhir+json"
                            }
                        }
                );
            });
        }
    };

    var create = function (practiceOdsCode, patientId, appointment) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment-1").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.post(
                    endpointLookupResult.restUrlPrefix + '/Appointment',
                    appointment,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment-1",
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId),
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Prefer': "return=representation",
                            'Accept': "application/fhir+json",
                            'Content-Type': "application/fhir+json"
                        }
                    }
            );
        });
    };

    var save = function (practiceOdsCode, patientNHSNumber, appointmentId, appointment) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment-1").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.put(
                    endpointLookupResult.restUrlPrefix + '/Appointment/' + appointmentId,
                    appointment,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:update:appointment-1",
                            'Prefer': "return=representation",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientNHSNumber),
                            'Accept': "application/fhir+json",
                            'Content-Type': "application/fhir+json"
                        }
                    }
            );
        });
    };
	
	var cancel = function (practiceOdsCode, patientNHSNumber, appointmentId, appointment) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment-1").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.put(
                    endpointLookupResult.restUrlPrefix + '/Appointment/' + appointmentId,
                    appointment,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:cancel:appointment-1",
                            'Prefer': "return=representation",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientNHSNumber),
                            'Accept': "application/fhir+json",
                            'Content-Type': "application/fhir+json"
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
        save: save,
		cancel: cancel
    };
});