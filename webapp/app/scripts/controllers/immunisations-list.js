'use strict';

angular.module('gpConnect')
  .controller('ImmunisationsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, Immunisation) {

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

    Immunisation.findAllHTMLTables($stateParams.patientId).then(function (result) {
      $scope.immunisationTables = result.data;

      for (var i = 0; i < $scope.immunisationTables.length; i++) {
        $scope.immunisationTables[i].html = $sce.trustAsHtml($scope.immunisationTables[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });
  });
