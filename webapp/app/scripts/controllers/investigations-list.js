'use strict';

angular.module('gpConnect')
  .controller('InvestigationsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, Investigation) {

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

    Investigation.findAllHTMLTables($stateParams.patientId).then(function (result) {
      if (result.data.length == 0) {
        var text = '[{"sourceId":"1","source":"Legacy","provider":"No Data","html":"No investigation data available for this patient."}]';
        $scope.investigationTables = JSON.parse(text);
      } else {
        $scope.investigationTables = result.data;
      }

      for (var i = 0; i < $scope.investigationTables.length; i++) {
        $scope.investigationTables[i].html = $sce.trustAsHtml($scope.investigationTables[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });
  });
