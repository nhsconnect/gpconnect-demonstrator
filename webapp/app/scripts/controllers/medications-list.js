'use strict';

angular.module('openehrPocApp')
  .controller('MedicationsListCtrl', function ($scope, $location, $stateParams, $modal, PatientService, Medication) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Medication.byPatient($stateParams.patientId).then(function (result) {
      $scope.medications = result.data;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (medication) {
      return medication.id === $stateParams.medicationId;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/medications/medications-modal.html',
        size: 'lg',
        controller: 'MedicationsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Medication'
            };
          },
          medication: function () {
            return { date: new Date() };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (medication) {
        Medication.createByPatient($scope.patient.id, medication).then(function (result) {
          $scope.patient.medications.push(result.data);
        });
      });
    };

  });
