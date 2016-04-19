'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsDetailCtrl', function ($scope, $stateParams, $state, PatientService) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    $scope.goTo = function (section) {
      var requestHeader = {
        patientId: $stateParams.patientId
      };

      var toState = '';

      switch (section) {
      case 'summary':
        toState = 'patients-summary';
        break;
      case 'diagnosis':
        toState = 'diagnoses-list';
        break;
      case 'allergies':
        toState = 'allergies';
        break;
      case 'medications':
        toState = 'medications';
        break;
      case 'results':
        toState = 'results';
        break;
      case 'procedures':
        toState = 'procedures';
        break;
      case 'referrals':
        toState = 'referrals';
        break;
      case 'appointments':
        toState = 'appointments';
        break;
      case 'transfers':
        toState = 'transferOfCare';
        break;
      case 'careplans':
        toState = 'eolcareplans';
        break;
      case 'mdt':
        toState = 'cancerMdt';
        break;
      }
      $state.go(toState, requestHeader);
    };

  });
