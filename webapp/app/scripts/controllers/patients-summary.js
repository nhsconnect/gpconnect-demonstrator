'use strict';

angular.module('gpConnect')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams, $state, $rootScope, $location, usSpinnerService, PatientService) {

    $scope.patients = $stateParams.patientsList;

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;


      $scope.allergiesCount = patient.allergies.length;
      $scope.allergies = patient.allergies.slice(0, 5);

      $scope.diagnosesCount = patient.problems.length;
      $scope.diagnoses = patient.problems.slice(0, 5);

      $scope.medicationsCount = patient.medications.length;
      $scope.medications = patient.medications.slice(0, 5);

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.goToSection = function (section) {
      var requestHeader = {
        patientId: $stateParams.patientId
      };

      var toState = '';
      switch (section) {
      case 'Problems':
        toState = 'diagnoses-list';
        break;
      case 'Allergies':
        toState = 'allergies';
        break;
      case 'Medications':
        toState = 'medications';
        break;
      }
      $state.go(toState, requestHeader);
    };

  });
