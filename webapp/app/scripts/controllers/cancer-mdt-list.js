'use strict';

angular.module('rippleDemonstrator')
  .controller('CancerMdtListCtrl', function ($scope, $location, $stateParams, SearchInput, $modal, $state, usSpinnerService, PatientService, CancerMdt) {

    $scope.currentPage = 1;
    SearchInput.update();

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.dateOfRequest).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.serviceTeam).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateOfMeeting).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    CancerMdt.all($stateParams.patientId).then(function (result) {
      $scope.cancerMdtComposition = result.data;

      for (var i = 0; i < $scope.cancerMdtComposition.length; i++) {
        $scope.cancerMdtComposition[i].dateOfRequest = moment($scope.cancerMdtComposition[i].dateOfRequest).format('DD-MMM-YYYY');
        $scope.cancerMdtComposition[i].dateOfMeeting = moment($scope.cancerMdtComposition[i].dateOfMeeting).format('DD-MMM-YYYY');
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('cancerMdt-detail', {
        patientId: $scope.patient.id,
        cancerMdtIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
      });
    };

    $scope.selected = function (cancerMdtIndex) {
      return cancerMdtIndex === $stateParams.cancerMdtIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/cancer-mdt/cancer-mdt-modal.html',
        size: 'lg',
        controller: 'CancerMdtModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create MDT'
            };
          },
          cancerMdt: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (cancerMdt) {
        cancerMdt.dateOfMeeting = new Date(cancerMdt.dateOfMeeting);
        cancerMdt.dateOfMeeting.setMinutes(cancerMdt.dateOfMeeting.getMinutes() - cancerMdt.dateOfMeeting.getTimezoneOffset());

        cancerMdt.dateOfRequest = new Date(cancerMdt.dateOfRequest);
        cancerMdt.dateOfRequest.setMinutes(cancerMdt.dateOfRequest.getMinutes() - cancerMdt.dateOfRequest.getTimezoneOffset());

        if (cancerMdt.timeOfMeeting !== null) {
          cancerMdt.timeOfMeeting = new Date(cancerMdt.timeOfMeeting);
          cancerMdt.timeOfMeeting.setMinutes(cancerMdt.timeOfMeeting.getMinutes() - cancerMdt.timeOfMeeting.getTimezoneOffset());
        }

        cancerMdt.compositionId = '';
        cancerMdt.source = 'openehr';

        CancerMdt.create($scope.patient.id, cancerMdt).then(function () {
          setTimeout(function () {
            $state.go('cancerMdt', {
              patientId: $scope.patient.id,
              filter: $scope.query,
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
