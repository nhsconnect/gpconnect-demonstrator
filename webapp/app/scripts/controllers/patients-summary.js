'use strict';

angular.module('openehrPocApp')
  .controller('PatientsSummaryCtrl', function ($scope, $stateParams,$location,PatientService,Diagnosis,Allergy,Medication,Contact,TransferOfCare) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.diagnosesCount =  $scope.result.problems.length;
      $scope.diagnoses = $scope.result.problems.slice(0,5);
    });

    Allergy.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.allergiesCount =  $scope.result.allergies.length;
      $scope.allergies = $scope.result.allergies.slice(0,5);

    });

    Medication.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.medicationsCount =  $scope.result.medications.length;
      $scope.medications = $scope.result.medications.slice(0,5);
    });

    Contact.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.contactsCount = $scope.result.contacts.length;
      $scope.contacts = $scope.result.contacts.slice(0,5);
    });

    TransferOfCare.getComposition($stateParams.patientId).then(function (result) {
      $scope.transferofCaresCount = result.data.transfers.length;
      $scope.transferofCareComposition = result.data;

      var descendingTransferofCareComposition = [];
     for(var x = $scope.transferofCareComposition.transfers.length-1; x>=0; x--){
       descendingTransferofCareComposition.push($scope.transferofCareComposition.transfers[x]);
     }

      $scope.transferofCareComposition.transfers = descendingTransferofCareComposition;

      console.log ($scope.transferofCareComposition);
      $scope.transferofCareComposition = $scope.transferofCareComposition.transfers.slice(0,5);
      console.log ($scope.transferofCareComposition);
    });

    $scope.go = function (path) {
      $location.path(path);
    };

  });
