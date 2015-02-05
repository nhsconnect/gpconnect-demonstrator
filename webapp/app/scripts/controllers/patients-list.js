'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsCtrl
 * @description
 * # PatientsCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('PatientsListCtrl', function ($scope, $state, $stateParams, PatientService) {
    PatientService.all().then(function (patients) {
      $scope.patients = patients;
    });

    $scope.department = $stateParams.department;
    $scope.ageRange = $stateParams.ageRange;
    $scope.order = $stateParams.order || 'lastname';
    $scope.reverse = $stateParams.reverse === 'true';

    $scope.sort = function (field) {
      var reverse = $scope.reverse;

      if ($scope.order === field) {
        reverse = !reverse;
      }

      $state.transitionTo($state.current, { order: field, reverse: reverse });
    };

    $scope.sortClass = function (field) {
      if ($scope.order === field) {
        return $scope.reverse ? 'sort-desc' : 'sort-asc';
      }
    };

    $scope.go = function (patient) {
      $state.go('diagnoses-list', { patientId: patient.id });
    };
  });
