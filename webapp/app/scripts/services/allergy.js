'use strict';

angular.module('rippleDemonstrator')
  .factory('Allergy', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/allergies');
    };

    var get = function (patientId, compositionId, source) {
      return $http.get('/api/patients/' + patientId + '/allergies/' + compositionId + '?source=' + source);
    };

    var create = function (patientId, composition) {
      console.log('put allergy comp:');
      console.log(composition);
      return $http.post('/api/patients/' + patientId + '/allergies', composition);
    };

    var update = function (patientId, composition) {
      console.log('put allergy comp:');
      console.log(composition);
      return $http.put('/api/patients/' + patientId + '/allergies', composition);
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create
    };

  });
