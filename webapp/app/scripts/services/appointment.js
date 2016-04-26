'use strict';

angular.module('gpConnect')
  .factory('Appointment', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/appointments');
    };

    var findDetails = function (patientId, appointmentId) {
      return $http.get('/api/patients/' + patientId + '/appointments/' + appointmentId);
    };

    var create = function (patientId, appointment) {
      return $http.post('/api/patients/' + patientId + '/appointments', appointment);
    };

    var update = function (patientId, appointment) {
      return $http.put('/api/patients/' + patientId + '/appointments', appointment);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      update: update,
      create: create
    };

  });
