'use strict';

angular.module('gpConnect')
  .controller('ClinicalItemsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, ClinicalItem) {

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

    ClinicalItem.findAllHTMLTables($stateParams.patientId).then(function (result) {
      if (result.data.length == 0) {
        var text = '[{"sourceId":"1","source":"Legacy","provider":"No Data","html":"No clinical item data available for this patient."}]';
        $scope.clinicalItemTables = JSON.parse(text);
      } else {
        $scope.clinicalItemTables = result.data;
      }

      for (var i = 0; i < $scope.clinicalItemTables.length; i++) {
        $scope.clinicalItemTables[i].html = $sce.trustAsHtml($scope.clinicalItemTables[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });
  });
