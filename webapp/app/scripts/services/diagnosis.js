'use strict';

angular.module('openehrPocApp')
  .factory('Diagnosis', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses');
    };

    var create = function (patientId, composition) {
      console.log('new compositon is:');
      console.log(composition);
      console.log('new diagnosis array');
      console.log(composition.problems);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses', composition);
    };

    var update = function (patientId, composition) {
      console.log('new compositon is:');
      console.log(composition);
      console.log('new diagnosis array');
      console.log(composition.problems);
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses', composition);
    };

    return {
      all: all,
      update: update,
      create:create
    };
  });

