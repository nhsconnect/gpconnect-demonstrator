'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, TransferOfCare) {

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
      });

  });
