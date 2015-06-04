'use strict';

angular.module('openehrPocApp')
  .controller('MedicationsListCtrl', function ($scope, $location, $stateParams, $modal, $state,PatientService, Medication) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Medication.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.medications = $scope.result.medications;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (medicationIndex) {
      return medicationIndex === $stateParams.medicationIndex;
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
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (medication) {
        $scope.result.medications.push(medication);

        Medication.update($scope.patient.id, $scope.result).then(function (result) {
          // $scope.patient.medications.push(result.data);
          $state.go('medications', { patientId: $scope.patient.id });
        });
      });
    };

  });
