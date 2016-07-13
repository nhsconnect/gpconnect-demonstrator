'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($http, EnvConfig) {

    var findAllAppointments = function (patientId) {
        return $http.get(EnvConfig.restUrlPrefix+'/fhir/Patient/' + patientId + '/Appointment',
        {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:patient_appointments"
          }
        });
    };
    
    var getScheduleOperation = function (organizationODSCode, siteODSCode, startDateTime, endDateTime) {
      return $http.post(EnvConfig.restUrlPrefix+'/fhir/Organization/$getschedule',
      '{ "resourceType" : "Parameters", "parameter" : [ { "name" : "odsOrganisationCode", "valueIdentifier" : { "value" : "'+organizationODSCode+'" } }, { "name" : "odsSiteCode", "valueIdentifier" : { "value" : "'+siteODSCode+'" } }, { "name" : "timePeriod", "valuePeriod" : { "start" : "'+startDateTime+'", "end" : "'+endDateTime+'" } } ] }',
      {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule"
          }
        });
    };
    
    var findResourceByReference = function (resourceReference) {
        if(resourceReference == "Location"){
            return $http.get(EnvConfig.restUrlPrefix+'/fhir/'+resourceReference,
            {
                headers: {
                    'Ssp-From': EnvConfig.fromASID,
                    'Ssp-To': EnvConfig.toASID,
                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location"
                }
            });
        } else if(resourceReference == "Practitioner"){
            return $http.get(EnvConfig.restUrlPrefix+'/fhir/'+resourceReference,
            {
                headers: {
                    'Ssp-From': EnvConfig.fromASID,
                    'Ssp-To': EnvConfig.toASID,
                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:practitioner"
                }
            });
        }
    };
    
    var create = function (appointment) {
      return $http.post(EnvConfig.restUrlPrefix+'/fhir/Appointment',
        appointment,
        {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:appointment"
          }
        });
    };
    
    var save = function (appointmentId, appointment) {
      return $http.put(EnvConfig.restUrlPrefix+'/fhir/Appointment/'+appointmentId, 
        appointment,
        {
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:appointment"
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
