'use strict';

angular.module('rippleDemonstrator')
  .controller('ContactsModalCtrl', function ($scope, $modalInstance, contact, patient, modal) {

    $scope.contact = contact;
    $scope.patient = patient;
    $scope.modal = modal;

    $scope.ok = function (contactForm, contact) {
      $scope.formSubmitted = true;

      if (contactForm.$valid) {
        $modalInstance.close(contact);
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
