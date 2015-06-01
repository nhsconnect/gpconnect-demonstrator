'use strict';

angular.module('openehrPocApp')
  .controller('PatientsListCtrl', function ($scope, $state, $stateParams, PatientService) {
    PatientService.all().then(function (patients) {
      $scope.patients = patients;
    });

    $scope.order = $stateParams.order || 'lastname';
    $scope.reverse = $stateParams.reverse === 'true';
    $scope.filters = { department: $stateParams.department, ageRange: $stateParams.ageRange };

    $scope.sort = function (field) {
      var reverse = $scope.reverse;

      if ($scope.order === field) {
        reverse = !reverse;
      }

      $state.transitionTo($state.current, _.extend($stateParams, { order: field, reverse: reverse }));
    };

    $scope.sortClass = function (field) {
      if ($scope.order === field) {
        return $scope.reverse ? 'sort-desc' : 'sort-asc';
      }
    };

    $scope.go = function (patient) {
      //$state.go('diagnoses-list', { patientId: patient.id });
      $state.go('patients-summary', { patientId: patient.id });
    };

    $scope.patientFilter = function (patient) {
      if ($scope.filters.department) {
        return (patient.department === $scope.filters.department);
      }

      if ($scope.filters.ageRange) {
        return (patient.ageRange === $scope.filters.ageRange);
      }

      return true;
    };
  });
