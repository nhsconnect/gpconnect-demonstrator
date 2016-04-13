'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams, $state, SearchInput, $rootScope, $location, usSpinnerService, PatientService) {

    SearchInput.update();
    $scope.patients = $stateParams.patientsList;

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;


      $scope.allergiesCount = patient.allergies.length;
      $scope.allergies = patient.allergies.slice(0, 5);

      $scope.diagnosesCount = patient.problems.length;
      $scope.diagnoses = patient.problems.slice(0, 5);

      $scope.medicationsCount = patient.medications.length;
      $scope.medications = patient.medications.slice(0, 5);

      $scope.contactsCount = patient.contacts.length;
      $scope.contacts = patient.contacts.slice(0, 5);

      $scope.transferofCaresCount = patient.transfers.length;
      $scope.transferofCareComposition = patient;

      var descendingTransferofCareComposition = [];
      for (var x = $scope.transferofCareComposition.transfers.length - 1; x >= 0; x--) {
        descendingTransferofCareComposition.push($scope.transferofCareComposition.transfers[x]);
      }

      $scope.transferofCareComposition.transfers = descendingTransferofCareComposition;
      $scope.transferofCareComposition = $scope.transferofCareComposition.transfers.slice(0, 5);

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.goToSection = function (section) {
      var requestHeader = {
        patientId: $stateParams.patientId,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
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
      case 'Contacts':
        toState = 'contacts';
        break;
      case 'Transfer':
        toState = 'transferOfCare';
        break;
      }
      $state.go(toState, requestHeader);
    };

  });
