'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansModalCtrl', function ($scope, $filter, $modalInstance, eolcareplan, patient, modal, PatientService) {

    $scope.currentUser = PatientService.getCurrentUser();
    $scope.eolcareplan = eolcareplan;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.docTypes =  [
        { type:'Document Type' },
        { type:'Document Type 2' }
    ];

    if (modal.title === 'Create End of Life Care Document'){
      var today = new Date().toISOString().slice(0, 10);
      $scope.eolcareplan.careDocument = {
        dateCreated : today
      };
    }else {
      $scope.eolcareplan.careDocument.dateCreated = new Date($scope.eolcareplan.careDocument.dateCreated).toISOString().slice(0, 10);
      $scope.eolcareplan.cprDecision.dateOfDecision = new Date($scope.eolcareplan.cprDecision.dateOfDecision).toISOString().slice(0, 10);
      $scope.eolcareplan.treatmentDecision.dateOfDecision = new Date($scope.eolcareplan.treatmentDecision.dateOfDecision).toISOString().slice(0, 10);
    }

    $scope.ok = function (eolcareplanForm, eolcareplan) {
      $scope.formSubmitted = true;
      if (eolcareplanForm.$valid) {
        $modalInstance.close(eolcareplan);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.radioModel = 'Tab1';

    $scope.typesAvaliable = ['Document', 'Document T1', 'Document T2', 'Document T3'];
    $scope.typesChosen = $scope.typesAvaliable[0];

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.openDecisionDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.dateofcprdecisionDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.validate = function (form, name, index) {
      var errorToCheckFor = name + index;

      for (var error in form.$error.required){
        var errorName = form.$error.required[error].$name;

        if (errorName === errorToCheckFor){
          return true;
        }
      }
    };

    $scope.validateDirty = function (form, name, index) {
      var errorToCheckFor = name + index;

      if (form[errorToCheckFor].$dirty && form[errorToCheckFor].$invalid){
        return true;
      } else {
        return false;
      }
    };

    $scope.validateClean = function (form, name, index) {
      var errorToCheckFor = name + index;

      if (form[errorToCheckFor].$dirty && form[errorToCheckFor].$valid){
        return true;
      } else {
        return false;
      }
    };

  });
