'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($http) {

    var findAllAppointments = function (patientId) {
        return $http.get('/fhir/Patient/' + patientId + '/Appointment');
    };
    
    var getScheduleOperation = function (organizationODSCode, siteODSCode, startDateTime, endDateTime) {
      return $http.post('/fhir/Organization/$getschedule', '{ "resourceType" : "Parameters", "parameter" : [ { "name" : "odsOrganisationCode", "valueIdentifier" : { "value" : "'+organizationODSCode+'" } }, { "name" : "odsSiteCode", "valueIdentifier" : { "value" : "'+siteODSCode+'" } }, { "name" : "timePeriod", "valuePeriod" : { "start" : "'+startDateTime+'", "end" : "'+endDateTime+'" } } ] }');
    };
    
    var findResourceByReference = function (resourceReference) {
      return $http.get('/fhir/'+resourceReference);
    };
    
    var create = function (appointment) {
      return $http.post('/fhir/Appointment', appointment);
    };

    return {
      findAllAppointments: findAllAppointments,
      getScheduleOperation: getScheduleOperation,
      findResourceByReference: findResourceByReference,
      create: create
    };

  });
