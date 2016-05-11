'use strict';

angular.module('gpConnect')
  .controller('AdminItemsListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, AdminItem) {

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

    AdminItem.findAllHTMLTables($stateParams.patientId).then(function (result) {

      if (result.data.length == 0) {
        var text = '[{"sourceId":"1","source":"Legacy","provider":"No Data","html":"No administrative items data available for this patient."}]';
        $scope.adminItemTables = JSON.parse(text);
      } else {
        $scope.adminItemTables = result.data;
      }

      for (var i = 0; i < $scope.adminItemTables.length; i++) {
        $scope.adminItemTables[i].html = $sce.trustAsHtml($scope.adminItemTables[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });
  });
