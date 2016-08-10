'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($http, EnvConfig, fhirJWTFactory) {

    var findAllAppointments = function (patientNHSNumber, patientId) {
        return $http.get(EnvConfig.restUrlPrefix+'/Patient/' + patientId + '/Appointment',
        {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments",
              'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientNHSNumber)
          }
        });
    };
    
    var getScheduleOperation = function (organizationODSCode, siteODSCode, startDateTime, endDateTime) {
      return $http.post(EnvConfig.restUrlPrefix+'/Organization/$gpc.getschedule',
      '{ "resourceType" : "Parameters", "parameter" : [ { "name" : "odsOrganisationCode", "valueIdentifier" : { "value" : "'+organizationODSCode+'" } }, { "name" : "odsSiteCode", "valueIdentifier" : { "value" : "'+siteODSCode+'" } }, { "name" : "timePeriod", "valuePeriod" : { "start" : "'+startDateTime+'", "end" : "'+endDateTime+'" } } ] }',
      {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule",
              'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", organizationODSCode)
          }
        });
    };
    
    var findResourceByReference = function (patientId, resourceReference) {
        if(resourceReference.indexOf("Location") > -1){
            return $http.get(EnvConfig.restUrlPrefix+'/'+resourceReference,
            {
                headers: {
                    'Ssp-From': EnvConfig.fromASID,
                    'Ssp-To': EnvConfig.toASID,
                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location",
                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                }
            });
        } else if(resourceReference.indexOf("Practitioner") > -1){
            return $http.get(EnvConfig.restUrlPrefix+'/'+resourceReference,
            {
                headers: {
                    'Ssp-From': EnvConfig.fromASID,
                    'Ssp-To': EnvConfig.toASID,
                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner",
                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                }
            });
        }
    };
    
    var create = function (patientId, appointment) {
      return $http.post(EnvConfig.restUrlPrefix+'/Appointment',
        appointment,
        {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment",
              'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId),
              'Prefer' : "return=representation"
          }
        });
    };
    
    var save = function (patientNHSNumber, appointmentId, appointment) {
      return $http.put(EnvConfig.restUrlPrefix+'/Appointment/'+appointmentId, 
        appointment,
        {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment",
              'Prefer' : "return=representation",
              'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientNHSNumber)
          }
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
