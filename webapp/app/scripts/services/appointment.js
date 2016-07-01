'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($http) {

    var findAllAppointments = function (patientId) {
      var response;
      return $http.get('/fhir/Patient?patientId='+patientId).then(function(response) {
         return $http.get('/fhir/Patient/' + response.data.entry[0].resource.id + '/Appointment');
      });
    };
    
    var getScheduleOperation = function (organizationODSCode, siteODSCode, startDateTime, endDateTime) {
      return $http.post('/fhir/$getschedule', '{ "resourceType": "Parameters", "parameter": [{ "name": "odsOrganisationCode", "valueIdentifier": { "value": "'+organizationODSCode+'" } }, { "name": "odsSiteCode", "valueIdentifier": { "value": "'+siteODSCode+'" } }, { "name": "timePeriod", "valuePeriod": { "start": "'+startDateTime+'", "end": "'+endDateTime+'" } }] }');
    };
    
    var findResourceByReference = function (resourceReference) {
      return $http.get('/fhir/'+resourceReference);
    };
    
    var create = function (patientId, appointment) {
      return $http.post('/api/patients/' + patientId + '/appointments', appointment);
    };

    return {
      findAllAppointments: findAllAppointments,
      getScheduleOperation: getScheduleOperation,
      findResourceByReference: findResourceByReference,
      create: create
    };

  });
