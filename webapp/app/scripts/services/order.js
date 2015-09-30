'use strict';

angular.module('rippleDemonstrator')
  .factory('Order', function ($http) {

    var all = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/laborders');
    };

    var get = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/laborders/' + compositionId);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/laborders', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/laborders', composition);
    };

    var suggestion = function () {
      return $http.get('/api/terminology/list/order');
    };

    return {
      all: all,
      get: get,
      update: update,
      create: create,
      suggestion: suggestion
    };

  });
