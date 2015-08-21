'use strict';

angular.module('openehrPocApp')
  .controller('ResultsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Result) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Result.all($stateParams.resultIndex).then(function (result) {
      $scope.result = result.data[$stateParams.resultIndex];
    });

  });
