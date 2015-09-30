'use strict';

angular.module('rippleDemonstrator')
  .factory('CancerMdt', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/mdtreports/');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/mdtreports/' + compositionId);
    };

    var create = function (patientId, composition) {
      console.log('create cancerMdtComposition: ');
      console.log(composition);
      return $http.post('/api/patients/' + patientId + '/mdtreports', composition);
    };

    var update = function (patientId, composition) {
      console.log('update cancerMdtComposition: ');
      console.log(composition);
      return $http.put('/api/patients/' + patientId + '/mdtreports', composition);
    };

    return {
      all: all,
      get: get,
      create: create,
      update: update
    };

  });
