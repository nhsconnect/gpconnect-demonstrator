'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesModalCtrl', function ($scope, $modalInstance, allergy, patient, modal) {

    $scope.allergy = allergy;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.ok = function (allergyForm, allergy) {
      $scope.formSubmitted = true;
      if (allergyForm.$valid) {
        $modalInstance.close(allergy);
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
