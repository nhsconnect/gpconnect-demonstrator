'use strict';

angular.module('gpConnect')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, $sce, $modal, $state, usSpinnerService, PatientService, Allergy) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.cause).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.reaction).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Allergy.findAllHTMLTables($stateParams.patientId).then(function (result) {
      $scope.allergyTables = result.data;

      for (var i = 0; i < $scope.allergyTables.length; i++) {
        $scope.allergyTables[i].html = $sce.trustAsHtml($scope.allergyTables[i].html);
      }

      usSpinnerService.stop('allergySummary-spinner');
    });

    $scope.go = function (id, source) {
      $state.go('allergies-detail', {
        patientId: $scope.patient.id,
        allergyIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        source: source
      });
    };

    $scope.selected = function ($index) {
      return $index === $stateParams.allergyIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/allergies/allergies-modal.html',
        size: 'lg',
        controller: 'AllergiesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Allergy'
            };
          },
          allergy: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (allergy) {
        var toAdd = {
          sourceId: '',
          cause: allergy.cause,
          causeCode: allergy.causeCode,
          causeTerminology: allergy.causeTerminology,
          reaction: allergy.reaction,
          source: allergy.source
        };

        Allergy.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('allergies', {
              patientId: $scope.patient.id,
              filter: $scope.query,
              page: $scope.currentPage
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
