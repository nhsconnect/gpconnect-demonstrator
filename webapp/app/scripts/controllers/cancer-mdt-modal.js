'use strict';

angular.module('rippleDemonstrator')
  .controller('CancerMdtModalCtrl', function ($scope, $modalInstance, $stateParams, $state, cancerMdt, patient, modal) {

    $scope.cancerMdt = cancerMdt;
    $scope.protocol = 'http://';

    if (modal.title === 'Edit MDT') {
      $scope.cancerMdt.timeOfMeeting = new Date($scope.cancerMdt.timeOfMeeting);
      $scope.cancerMdt.dateOfMeeting = new Date($scope.cancerMdt.dateOfMeeting).toISOString().slice(0, 10);
      $scope.cancerMdt.dateOfRequest = new Date($scope.cancerMdt.dateOfRequest).toISOString().slice(0, 10);
    }

    $scope.changeProtocol = function (protocol) {
      switch (protocol) {
        case 'http':
          $scope.protocol = 'http://';
          break;
        case 'https':
          $scope.protocol = 'https://';
          break;
        default:
          $scope.protocol = 'http://';
      }
    };

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

    $scope.dateofMeetingDatepicker = function ($event, name) {
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
