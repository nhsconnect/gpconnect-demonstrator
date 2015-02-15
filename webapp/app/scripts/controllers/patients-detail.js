'use strict';

angular.module('openehrPocApp')
  .controller('PatientsDetailCtrl', function ($scope, $stateParams, PatientService) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

  });
