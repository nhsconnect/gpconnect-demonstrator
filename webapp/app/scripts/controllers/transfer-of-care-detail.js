'use strict';

angular.module('rippleDemonstrator')
  .controller('TransferOfCareDetailCtrl', function ($scope, $stateParams, SearchInput, $modal, $location, usSpinnerService, PatientService, TransferOfCare) {

    SearchInput.update();
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    TransferOfCare.get($stateParams.patientId, $stateParams.transferOfCareIndex).then(function (result) {
      $scope.transferOfCare = result.data;
      $scope.allergies = $scope.transferOfCare.allergies;
      $scope.contacts = $scope.transferOfCare.contacts;
      $scope.problems = $scope.transferOfCare.problems;
      $scope.medications = $scope.transferOfCare.medications;
      $scope.dateOfTransfer = $scope.transferOfCare.dateOfTransfer;
      usSpinnerService.stop('transferDetail-spinner');
    });

  });
