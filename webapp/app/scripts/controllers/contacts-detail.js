'use strict';

angular.module('openehrPocApp')
  .controller('ContactsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Contact) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.all($stateParams.contactId).then(function (result) {
      $scope.result = result.data;
      $scope.contact = $scope.result.contacts[$stateParams.contactIndex];
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
        $scope.result.contacts[$stateParams.contactIndex] = contact;

        Contact.update($scope.patient.id, $scope.result).then(function (result) {
          $scope.contact = result.data;
          $location.path('/patients/' + $scope.patient.id + '/contacts/' + $stateParams.contactIndex);
        });
      });
    };

  });
