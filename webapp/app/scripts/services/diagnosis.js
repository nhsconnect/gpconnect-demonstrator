'use strict';

angular.module('openehrPocApp')
  .factory('Diagnosis', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses');
    };   
    
    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses/' + compositionId);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses', composition);
    };

    return {
      all: all,
      get: get,    
      update: update,
      create:create
    };
  });

