'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, TransferOfCare) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    TransferOfCare.getComposition($stateParams.patientId).then(function (result) {
      $scope.transferofCareComposition = result.data;

      console.log($scope.transferofCareComposition);
    });


    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (transferOfCareIndex) {
      return transferOfCareIndex === $stateParams.transferOfCareIndex;
    };

    $scope.create = function () {
      $state.go('transferOfCare-create', {
        patientId: $scope.patient.id
      });
    };

  });
