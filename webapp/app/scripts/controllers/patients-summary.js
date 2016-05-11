'use strict';

angular.module('gpConnect')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams, $state, $rootScope, $location, $sce, usSpinnerService, PatientService) {

    $scope.patients = $stateParams.patientsList;

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    PatientService.getSummary($stateParams.patientId).then(function (patientSummaries) {
      if (patientSummaries.data.length == 0) {
        var text = '[{"sourceId":"1","source":"Legacy","provider":"No Data","html":"No patient summary available for this patient."}]';
        $scope.patientSummaries = JSON.parse(text);
      } else {
        $scope.patientSummaries = patientSummaries.data;
      }

      for (var i = 0; i < $scope.patientSummaries.length; i++) {
        $scope.patientSummaries[i].html = $sce.trustAsHtml($scope.patientSummaries[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.goToSection = function (section) {
      var requestHeader = {
        patientId: $stateParams.patientId
      };
    };

  });
