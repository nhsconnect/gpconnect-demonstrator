'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtModalCtrl', function ($scope, $modalInstance, $stateParams, $state, cancerMdt, patient, modal) {

    $scope.cancerMdt = cancerMdt;
    console.log($scope.cancerMdt);
    // Temporary add participants
    var participants = ['Joe Hughes', 'Lucy Jones', 'Bob Lowe'];
    $scope.cancerMdt.participants = participants;
    $scope.patient = patient;
    $scope.modal = modal;
    $scope.cancerMdtSelectionNumber = $stateParams.cancerMdtIndex + 1;
    $scope.ok = function (cancerMdtForm, cancerMdt) {
      $scope.formSubmitted = true;
      if (cancerMdtForm.$valid) {
        $modalInstance.close(cancerMdt);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

  });
