'use strict';

angular.module('rippleDemonstrator')
  .controller('ContactsListCtrl', function ($scope, $rootScope, $location, $stateParams, SearchInput, usSpinnerService, $modal, $state, PatientService, Contact) {

    SearchInput.update();
    $scope.query = {};
    $scope.queryBy = '$';
    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    if ($stateParams.filter) {
      $scope.query.$ = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Contact.all($stateParams.patientId).then(function (result) {
      $scope.contacts = result.data;
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('contacts-detail', {
        patientId: $scope.patient.id,
        contactIndex: id,
        filter: $scope.query.$,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
      });
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
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (contact) {
        contact.sourceId = '';

        Contact.create($scope.patient.id, contact).then(function () {
          setTimeout(function () {
            $state.go('contacts', {
              patientId: $scope.patient.id,
              filter: $scope.query.$,
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
