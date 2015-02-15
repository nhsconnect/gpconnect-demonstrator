'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, PatientService, Allergy) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.byPatient($stateParams.patientId).then(function (result) {
      $scope.allergies = result.data;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (allergy) {
      return allergy.id === $stateParams.allergyId;
    };

  });
