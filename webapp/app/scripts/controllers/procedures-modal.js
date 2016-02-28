'use strict';

angular.module('rippleDemonstrator')
  .controller('ProceduresModalCtrl', function ($scope, $filter, $modalInstance, UserService, patient, modal, procedure) {

    $scope.currentUser = UserService.getCurrentUser();
    $scope.procedure = procedure;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Create Procedure') {
      $scope.procedure.dateSubmitted = new Date().toISOString().slice(0, 10);
    }
    else {
      $scope.procedure.time = new Date($scope.procedure.time);
      $scope.procedure.dateSubmitted = new Date($scope.procedure.dateSubmitted).toISOString().slice(0, 10);
      $scope.procedure.date = new Date($scope.procedure.date).toISOString().slice(0, 10);
    }

    $scope.procedure.currentStatusCode = '1234567';
    $scope.procedure.currentStatusTerminology = 'ICD-10';

    $scope.dateofProcedureDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.dateofSubmittedDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.ok = function (procedureForm, procedure) {
      $scope.formSubmitted = true;

      if (procedureForm.$valid) {
        $modalInstance.close(procedure);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
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
