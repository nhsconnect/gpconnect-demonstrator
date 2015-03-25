'use strict';

angular.module('openehrPocApp')
  .factory('TransferOfCare', function ($http) {

    var patientIdOveride = 9999999000;

    var get = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/transfer-of-care/summary');
    };

    var getComposition = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/transfer-of-care/');
    };

    var update = function (patientId, composition) {
      console.log('transferOfCareComposition: ');
      console.log(composition);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/transfer-of-care', composition);
    };

    return {
      get: get,
      getComposition: getComposition,
      update: update
    };
  });


