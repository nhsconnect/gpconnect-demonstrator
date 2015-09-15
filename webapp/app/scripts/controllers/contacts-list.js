'use strict';

angular.module('openehrPocApp')
  .controller('ContactsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Contact) {

    $scope.query = {};
    $scope.queryBy = '$';

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.all($stateParams.patientId).then(function (result) {
      $scope.contacts = result.data;
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
        contact.sourceId = '';

        Contact.create($scope.patient.id, contact).then(function () {
          $state.go('contacts', { patientId: $scope.patient.id });
        });
      });
    };

  });
