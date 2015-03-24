'use strict';

angular.module('openehrPocApp')
  .factory('TransferOfCare', function ($http) {

    var patientIdOveride = 9999999000;

    var get = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/transfer-of-care/summary');
    };

    var update = function (patientId, composition) {
      var data = { 'patientId' : patientId, 'transferOfCareComposition' : composition };
      //return $http.post('/api/patients/' + patientId + '/transfer-of-care', data);
      console.log('transferOfCareComposition: ');
      console.log(composition);
       return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/transfer-of-care', data);
    };

    return {
      get: get,
      update: update
    };
  });


