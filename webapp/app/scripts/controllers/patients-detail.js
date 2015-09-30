'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsDetailCtrl', function ($scope, $stateParams, PatientService) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

  });
