'use strict';

angular.module('openehrPocApp')
  .controller('ContactsDetailCtrl', function ($scope, $stateParams, $modal, $location, $state, PatientService, Contact) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.get($stateParams.patientId, $stateParams.contactIndex).then(function (result) {
      $scope.contact = result.data;
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/contacts/contacts-modal.html',
        size: 'lg',
        controller: 'ContactsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Contact'
            };
          },
          contact: function () {
            return angular.copy($scope.contact);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (contact) {
        Contact.update($scope.patient.id, contact).then(function () {
          $state.go('contacts', { patientId: $scope.patient.id });
        });
      });
    };

  });
