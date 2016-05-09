'use strict';

angular.module('gpConnect')
  .controller('KeyIndicatorsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, KeyIndicator) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    KeyIndicator.findAllHTMLTables($stateParams.patientId).then(function (result) {
      $scope.keyIndicatorTables = result.data;

      for (var i = 0; i < $scope.keyIndicatorTables.length; i++) {
        $scope.keyIndicatorTables[i].html = $sce.trustAsHtml($scope.keyIndicatorTables[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });
  });
