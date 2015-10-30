'use strict';

angular.module('rippleDemonstrator')
  .controller('ContactsDetailCtrl', function ($scope, $stateParams, $modal, $location, SearchInput, $state, Helper, PatientService, usSpinnerService, Contact) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.get($stateParams.patientId, $stateParams.contactIndex).then(function (result) {
      $scope.contact = result.data;
      usSpinnerService.stop('contactDetail-spinner');
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
          setTimeout(function () {
            $state.go('contacts-detail', {
              patientId: $scope.patient.id,
              contactIndex: Helper.updateId(contact.sourceId),
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            });
          }, 2000);
        });
      });
    };

  });
