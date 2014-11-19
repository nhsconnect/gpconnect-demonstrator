/* jshint bitwise: false */

'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.factory:Diagnosis
 * @description
 * # Diagnosis
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .factory('Diagnosis', function ($http, $q) {

    var byPatient = function (patientId) {
      var defer = $q.defer();

      $http.get('/openEHR/api/patients/' + patientId + '/diagnoses').success(function (result) {
        defer.resolve(result);
      });

      return defer.promise;
    };

    var findByPatient = function (patientId, diagnosisId) {
      var defer = $q.defer();

      $http.get('/openEHR/api/patients/' + patientId + '/diagnoses/' + diagnosisId).success(function (result) {
        defer.resolve(result);
      });

      return defer.promise;
    };

    var updateByPatient = function (patientId, diagnosis) {
      return $http.put('/openEHR/api/patients/' + patientId + '/diagnoses/' + diagnosis.id, diagnosis);
    };

    var createByPatient = function (patientId, diagnosis) {
      diagnosis.patientId = patientId;
      return $http.post('/openEHR/api/patients/' + patientId + '/diagnoses', diagnosis);
    };

    return {
      byPatient: byPatient,
      findByPatient: findByPatient,
      updateByPatient: updateByPatient,
      createByPatient: createByPatient
    };
  });
