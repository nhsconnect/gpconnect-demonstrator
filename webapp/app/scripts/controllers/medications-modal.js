'use strict';

angular.module('openehrPocApp')
  .controller('MedicationsModalCtrl', function ($scope, $modalInstance, medication, patient, modal) {

    $scope.medication = medication;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Edit Medication'){
      $scope.medication.startTime = new Date($scope.medication.startTime);
      $scope.medication.startDate = new Date($scope.medication.startDate).toISOString().slice(0, 10);
    }

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
      $event.preventDefault();
      $event.stopPropagation();
      $scope[name] = true;
    };

  });
