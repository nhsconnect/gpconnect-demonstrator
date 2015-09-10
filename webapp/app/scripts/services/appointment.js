'use strict';

angular.module('openehrPocApp')
  .factory('Appointment', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/appointments');
    };

    var create = function (patientId, composition) {
      console.log('post appointment comp:');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/appointments', composition);
    };

    var update = function (patientId, composition) {
      console.log('put appointment comp:');
      console.log(composition);
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/appointments', composition);
    };

    return {
      all: all,
      update: update,
      create: create
    };
  });
