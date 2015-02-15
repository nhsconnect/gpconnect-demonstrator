'use strict';

angular.module('openehrPocApp')
  .factory('Diagnosis', function ($http) {

    var patientIdOveride = 746;

    var byPatient = function (patientId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses');
    };

    var findByPatient = function (patientId, diagnosisId) {
      return $http.get('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses/' + diagnosisId);
    };

    var updateByPatient = function (patientId, diagnosis) {
      return $http.put('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses/' + diagnosis.id, diagnosis);
    };

    var createByPatient = function (patientId, diagnosis) {
      diagnosis.patientId = (patientIdOveride || patientId);
      return $http.post('/api/patients/' + (patientIdOveride || patientId) + '/diagnoses', diagnosis);
    };

    return {
      byPatient: byPatient,
      findByPatient: findByPatient,
      updateByPatient: updateByPatient,
      createByPatient: createByPatient
    };
  });
