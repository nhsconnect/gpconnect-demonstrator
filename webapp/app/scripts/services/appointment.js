'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($http) {

    var findAllAppointments = function (patientId) {
      
      var response;
      
      return $http.get('/fhir/Patient?patientId='+patientId).then(function(response) {
         return $http.get('/fhir/Patient/' + response.data.entry[0].resource.id + '/Appointment');
      });
      
    };

    var create = function (patientId, appointment) {
      return $http.post('/api/patients/' + patientId + '/appointments', appointment);
    };

    return {
      findAllAppointments: findAllAppointments,
      create: create
    };

  });
