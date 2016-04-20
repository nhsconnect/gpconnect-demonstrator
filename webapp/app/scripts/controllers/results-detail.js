'use strict';

angular.module('gpConnect')
  .controller('ResultsDetailCtrl', function ($scope, $stateParams, $modal, $location, usSpinnerService, PatientService, Result) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Result.get($stateParams.patientId, $stateParams.resultIndex, $stateParams.source).then(function (result) {
      $scope.result = result.data;
      usSpinnerService.stop('resultsDetail-spinner');
    });

  });
