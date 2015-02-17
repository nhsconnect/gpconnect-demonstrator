'use strict';

angular.module('openehrPocApp')
  .controller('ContactsListCtrl', function ($scope, $location, $stateParams, $modal, PatientService, Contact) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.byPatient($stateParams.patientId).then(function (result) {
      $scope.contacts = result.data;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (contact) {
      return contact.id === $stateParams.contactId;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/contacts/contacts-modal.html',
        size: 'lg',
        controller: 'ContactsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Contact'
            };
          },
          contact: function () {
            return { date: new Date() };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (contact) {
        Contact.createByPatient($scope.patient.id, contact).then(function (result) {
          $scope.patient.contacts.push(result.data);
        });
      });
    };

  });
