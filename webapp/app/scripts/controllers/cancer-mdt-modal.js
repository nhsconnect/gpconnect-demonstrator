'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtModalCtrl', function ($scope, $modalInstance, cancerMdt, patient, modal) {

    $scope.cancerMdt = cancerMdt;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.ok = function (cancerMdtForm, cancerMdt) {
      $scope.formSubmitted = true;
      if (cancerMdtForm.$valid) {
        $modalInstance.close(cancerMdt);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

  });
