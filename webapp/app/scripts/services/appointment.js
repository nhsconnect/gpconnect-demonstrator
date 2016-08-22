'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory) {

    var findAllAppointments = function (patientNHSNumber, patientId) {
        return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments").then(function (response) {
            var endpointLookupResult = response;
            return $http.get(endpointLookupResult.restUrlPrefix+'/Patient/' + patientId + '/Appointment',
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
    
    var getScheduleOperation = function (organizationODSCode, siteODSCode, startDateTime, endDateTime) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule").then(function (response) {
        var endpointLookupResult = response;
        return $http.post(endpointLookupResult.restUrlPrefix+'/Organization/$gpc.getschedule',
        '{ "resourceType" : "Parameters", "parameter" : [ { "name" : "odsOrganisationCode", "valueIdentifier" : { "value" : "'+organizationODSCode+'" } }, { "name" : "odsSiteCode", "valueIdentifier" : { "value" : "'+siteODSCode+'" } }, { "name" : "timePeriod", "valuePeriod" : { "start" : "'+startDateTime+'", "end" : "'+endDateTime+'" } } ] }',
        {
            headers: {
                'Ssp-From': endpointLookupResult.fromASID,
                'Ssp-To': endpointLookupResult.toASID,
                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule",
                'Ssp-TraceID': fhirJWTFactory.guid(),
                'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", organizationODSCode)
            }
          });
      });
    };
    
    var findResourceByReference = function (patientId, resourceReference) {
        if(resourceReference.indexOf("Location") > -1){
            return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:read:location").then(function (response) {
                var endpointLookupResult = response;
                return $http.get(endpointLookupResult.restUrlPrefix+'/'+resourceReference,
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
        } else if(resourceReference.indexOf("Practitioner") > -1){
            return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner").then(function (response) {
                var endpointLookupResult = response;
                return $http.get(endpointLookupResult.restUrlPrefix+'/'+resourceReference,
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
    
    var create = function (patientId, appointment) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:create:appointment").then(function (response) {
        var endpointLookupResult = response;
        return $http.post(endpointLookupResult.restUrlPrefix+'/Appointment',
          appointment,
          {
            headers: {
                'Ssp-From': endpointLookupResult.fromASID,
                'Ssp-To': endpointLookupResult.toASID,
                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment",
                'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId),
                'Ssp-TraceID': fhirJWTFactory.guid(),
                'Prefer' : "return=representation"
            }
          });
      });
    };
    
    var save = function (patientNHSNumber, appointmentId, appointment) {
      return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:read:appointment").then(function (response) {
        var endpointLookupResult = response;
        return $http.put(endpointLookupResult.restUrlPrefix+'/Appointment/'+appointmentId, 
          appointment,
          {
            headers: {
                'Ssp-From': endpointLookupResult.fromASID,
                'Ssp-To': endpointLookupResult.toASID,
                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment",
                'Prefer' : "return=representation",
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
