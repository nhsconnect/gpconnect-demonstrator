'use strict';

angular.module('openehrPocApp')
  .factory('Order', function ($http) {

    var patientIdOveride = 9999999000;

    var all = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/laborders');
    };

    var create = function (patientId, composition) {
      console.log('post composition:');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/laborders', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/laborders', composition);
    };

    var suggestion = function () {
      return $http.get('/api/terminology/local/list/order');
    };

    return {
      all: all,
      update: update,
      create:create,
      suggestion:suggestion
    };
  });
