'use strict';

angular.module('gpConnect')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams, $state, $rootScope, $location, usSpinnerService, PatientService) {

    $scope.patients = $stateParams.patientsList;

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
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
