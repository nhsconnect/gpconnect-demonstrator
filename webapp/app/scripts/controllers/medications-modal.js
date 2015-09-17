'use strict';

angular.module('openehrPocApp')
  .controller('MedicationsModalCtrl', function ($scope, $modalInstance, medication, patient, modal) {

    $scope.medication = medication;
    $scope.patient = patient;
    $scope.modal = modal;
    
     if (modal.title === 'Edit Medication'){
      //$scope.procedure.time = new Date($scope.procedure.time);
      $scope.medication.startDateTime = new Date($scope.medication.startDateTime).toISOString().slice(0, 10);
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
