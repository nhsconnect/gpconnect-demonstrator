'use strict';

angular.module('gpConnect')
  .controller('ObservationsListCtrl', function ($scope, $location, $stateParams, $sce, $modal, usSpinnerService, $state, PatientService, Observation) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.testName).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.sampleTaken).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateCreated).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Observation.findAllHTMLTables($stateParams.patientId).then(function (result) {
      if (result.data.length == 0) {
        var text = '[{"sourceId":"1","source":"Legacy","provider":"No Data","html":"No observation data available for this patient."}]';
        $scope.observationTables = JSON.parse(text);
      } else {
        $scope.observationTables = result.data;
      }

      for (var i = 0; i < $scope.observationTables.length; i++) {
        $scope.observationTables[i].html = $sce.trustAsHtml($scope.observationTables[i].html);
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (sourceId, source) {
      $state.go('observations-detail', {
        patientId: $scope.patient.id,
        resultIndex: sourceId,
        source: source,
        filter: $scope.query,
        page: $scope.currentPage
      });
    };

    $scope.selected = function (resultIndex) {
      return resultIndex === $stateParams.resultIndex;
    };

  });
