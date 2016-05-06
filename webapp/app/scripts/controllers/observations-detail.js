'use strict';

angular.module('gpConnect')
  .controller('ObservationsDetailCtrl', function ($scope, $stateParams, $modal, $location, usSpinnerService, PatientService, Observation) {

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Result.findDetails($stateParams.patientId, $stateParams.resultIndex, $stateParams.source).then(function (result) {
      $scope.result = result.data;
      usSpinnerService.stop('observationsDetail-spinner');
    });

  });
