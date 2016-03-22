'use strict';

angular.module('rippleDemonstrator')
  .controller('ResultsDetailCtrl', function ($scope, $stateParams, SearchInput, $modal, $location, usSpinnerService, PatientService, Result) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Result.get($stateParams.patientId, $stateParams.resultIndex, $stateParams.source).then(function (result) {
      $scope.result = result.data;
      usSpinnerService.stop('resultsDetail-spinner');
    });

  });
