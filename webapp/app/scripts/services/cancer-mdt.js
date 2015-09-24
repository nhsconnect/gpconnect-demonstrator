'use strict';

angular.module('openehrPocApp')
  .factory('CancerMdt', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/mdtreports/');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/mdtreports/' + compositionId);
    };

    var create = function (patientId, composition) {
      console.log('create cancerMdtComposition: ');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/mdtreports', composition);
    };

    var update = function (patientId, composition) {
      console.log('update cancerMdtComposition: ');
      console.log(composition);
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/mdtreports', composition);
    };

    return {
      all: all,
      get: get,
      create: create,
      update: update
    };
  });


