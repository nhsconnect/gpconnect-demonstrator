'use strict';

angular.module('openehrPocApp')
  .controller('DiagnosesModalCtrl', function ($scope, $modalInstance, diagnosis, patient, modal) {

    $scope.diagnosis = diagnosis;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Edit Problem / Diagnosis'){
      $scope.diagnosis.dateOfOnset = new Date($scope.diagnosis.dateOfOnset).toISOString().slice(0, 10);
    }

    $scope.ok = function (diagnosisForm, diagnosis) {
      $scope.formSubmitted = true;
      if (diagnosisForm.$valid) {
        $modalInstance.close(diagnosis);
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
