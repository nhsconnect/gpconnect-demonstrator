'use strict';

angular.module('gpConnect')
  .controller('PatientsDetailCtrl', function ($scope, $stateParams, $state, PatientService) {

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
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
      case 'problem':
        toState = 'problem-list';
        break;
      case 'allergies':
        toState = 'allergies';
        break;
      case 'medications':
        toState = 'medications';
        break;
      case 'referrals':
        toState = 'referrals';
        break;
      case 'appointments':
        toState = 'appointments';
        break;
      case 'encounters':
        toState = 'encounters';
        break;
      case 'procedures':
        toState = 'procedures';
        break;
      case 'observations':
          toState = 'observations';
          break;
      }
      $state.go(toState, requestHeader);
    };

  });
