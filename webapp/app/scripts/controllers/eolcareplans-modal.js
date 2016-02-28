'use strict';

angular.module('rippleDemonstrator')
  .controller('EolcareplansModalCtrl', function ($scope, $filter, $modalInstance, UserService, eolcareplan, patient, modal) {

    $scope.currentUser = UserService.getCurrentUser();
    $scope.eolcareplan = eolcareplan;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.docTypes = [
      { type: 'Document Type' },
      { type: 'Document Type 2' }
    ];

    if (modal.title === 'Create End of Life Care Document') {
      $scope.eolcareplan.careDocument = {
        dateCreated: new Date().toISOString().slice(0, 10)
      };
    }
    else {
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

      for (var error in form.$error.required) {
        var errorName = form.$error.required[error].$name;

        if (errorName === errorToCheckFor) {
          return true;
        }
      }
    };

    $scope.validateDirty = function (form, name, index) {
      var errorToCheckFor = name + index;
      return form[errorToCheckFor].$dirty && form[errorToCheckFor].$invalid;
    };

    $scope.validateClean = function (form, name, index) {
      var errorToCheckFor = name + index;
      return form[errorToCheckFor].$dirty && form[errorToCheckFor].$valid;
    };

  });
