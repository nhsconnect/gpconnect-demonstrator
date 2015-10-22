'use strict';

angular.module('rippleDemonstrator')
  .controller('ResultsListCtrl', function ($scope, $location, $stateParams, $modal, usSpinnerService, $state, PatientService, Result) {

    $scope.search = function (row) {
      return (
        angular.lowercase(row.testName).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.sampleTaken).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateReported).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Result.all($stateParams.patientId).then(function (result) {
      $scope.results = result.data;

      for (var i = 0; i < $scope.results.length; i++) {
        $scope.results[i].sampleTaken = moment($scope.results[i].sampleTaken).format('DD-MMM-YYYY');
        $scope.results[i].dateCreated = moment($scope.results[i].dateCreated).format('DD-MMM-YYYY');
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
       $state.go('results-detail', { patientId: $scope.patient.id, resultIndex: id, filter: $scope.query });
    };

    $scope.selected = function (resultIndex) {
      return resultIndex === $stateParams.resultIndex;
    };

  });
