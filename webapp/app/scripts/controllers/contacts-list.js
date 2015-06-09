'use strict';

angular.module('openehrPocApp')
  .controller('ContactsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Contact) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.contacts = $scope.result.contacts;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (contactIndex) {
      return contactIndex === $stateParams.contactIndex;
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
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (contact) {
        $scope.result.contacts.push(contact);

        Contact.update($scope.patient.id, $scope.result).then(function () {
          $state.go('contacts', { patientId: $scope.patient.id });
        });
      });
    };

  });
