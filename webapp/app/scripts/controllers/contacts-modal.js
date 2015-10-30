'use strict';

angular.module('rippleDemonstrator')
  .controller('ContactsModalCtrl', function ($scope, $modalInstance, contact, PatientService, patient, modal) {

    $scope.currentUser = PatientService.getCurrentUser();
    $scope.contact = contact;
    $scope.patient = patient;
    $scope.modal = modal;

    if (modal.title === 'Create Contact') {
      $scope.contact.dateSubmitted = new Date().toISOString().slice(0, 10);
      $scope.contact.relationshipCode = 'at0039';
      $scope.contact.relationshipTerminology = 'local';
    }
    else {
      $scope.contact.dateSubmitted = new Date().toISOString().slice(0, 10);
      // $scope.contact.dateSubmitted = new Date($scope.contact.dateSubmitted).toISOString().slice(0, 10);
    }

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
