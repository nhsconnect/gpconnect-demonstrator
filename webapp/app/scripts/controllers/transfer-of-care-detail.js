'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, TransferOfCare) {

    PatientService.get($stateParams.patientId).then(function (patient) {
        $scope.patient = patient;
      });

    TransferOfCare.getComposition($stateParams.patientId).then(function (result) {
        $scope.transferofCareComposition = result.data;
        $scope.transferOfCare = $scope.transferofCareComposition.transfers[$stateParams.transferOfCareIndex];
        $scope.transferOfCareSelectionNumber = $stateParams.transferOfCareIndex + 1;
        $scope.allergies = $scope.transferOfCare.allergies;
        $scope.contacts = $scope.transferOfCare.contacts;
        $scope.problems = $scope.transferOfCare.problems;
        $scope.medications = $scope.transferOfCare.medication;
        $scope.transferDetail = $scope.transferOfCare.transferDetail;
      });

  });
