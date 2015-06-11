'use strict';

angular.module('openehrPocApp')
  .controller('MedicationsModalCtrl', function ($scope, $modalInstance, medication, patient, modal) {

    $scope.medication = medication;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.ok = function (medicationForm, medication) {
      $scope.formSubmitted = true;
      if (medicationForm.$valid) {
        $modalInstance.close(medication);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      console.log($event);
      console.log(name);
      $event.preventDefault();
      $event.stopPropagation();
      $scope[name] = true;
      console.log($scope[name]);

    };

  });
