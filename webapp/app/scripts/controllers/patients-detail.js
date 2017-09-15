'use strict';

angular.module('gpConnect')
  .controller('PatientsDetailCtrl', function ($scope, $stateParams, $state, PatientService, ProviderRouting) {

    PatientService.getFhirPatient(ProviderRouting.defaultPractice().odsCode, $stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
      $.each(patient.identifier, function (key, identifier) {
        if(identifier.system == "https://fhir.nhs.uk/Id/nhs-number"){
            $scope.patientNhsNumber = identifier.value;
        }
      });
      $scope.patientLocalIdentifier = patient.id;
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
      case 'observations':
          toState = 'observations';
          break;
      case 'investigations':
          toState = 'investigations';
          break;
      case 'immunisations':
          toState = 'immunisations';
          break;
      case 'adminitems':
          toState = 'adminitems';
          break;
      case 'clinicalitems':
          toState = 'clinicalitems';
          break;
      case 'orders-sent':
          toState = 'orders-sent';
          break;
      case 'orders-received':
          toState = 'orders-received';
          break;
      }

      $state.go(toState, requestHeader);
    };

  });
