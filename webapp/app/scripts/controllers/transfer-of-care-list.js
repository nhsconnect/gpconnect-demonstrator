'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareListCtrl', function ($scope, $location, $stateParams, $modal, PatientService, TransferOfCare) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    $scope.transferofCareCollection = { transferOfCares :[]};

    TransferOfCare.get($stateParams.patientId).then(function (result) {
      $scope.transferOfCare = result.data;

      //Ask Ian for collection of transferOfCares
      $scope.transferofCareCollection.transferOfCares.push($scope.transferOfCare);
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (transferOfCareIndex) {
      return transferOfCareIndex === $stateParams.transferOfCareIndex;
    };

  });
