'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, TransferOfCare) {

    $scope.query = {};
    $scope.queryBy = '$';

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    TransferOfCare.all($stateParams.patientId).then(function (result) {
      $scope.transferofCareComposition = result.data;
      for (var i = 0; i < $scope.transferofCareComposition.length; i++){
        $scope.transferofCareComposition[i].dateOfTransfer = moment($scope.transferofCareComposition[i].dateOfTransfer).format('DD-MMM-YYYY');
      }
    });

    $scope.go = function (id) {
      $location.path('/patients/'+ $scope.patient.id+ '/transfer-of-care-detail/' + id);
    };

    $scope.selected = function (transferOfCareIndex) {
      console.log($stateParams);
      return transferOfCareIndex === $stateParams.transferOfCareIndex;
    };

    $scope.create = function () {
      $state.go('transferOfCare-create', {
        patientId: $scope.patient.id
      });
    };

  });
