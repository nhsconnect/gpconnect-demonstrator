'use strict';

angular.module('rippleDemonstrator')
  .controller('MedicationsListCtrl', function ($scope, $location, $stateParams, $modal, $state, usSpinnerService, PatientService, Medication) {

    $scope.query = {};
    $scope.queryBy = '$';

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function(newPage) {
      $scope.currentPage = newPage;
    }

    if($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query.$ = $stateParams.filter;
    }

    Medication.all($stateParams.patientId).then(function (result) {
      $scope.medications = result.data;
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('medications-detail', {
        patientId: $scope.patient.id,
        medicationIndex: id,
        filter: $scope.query.$,
        page: $scope.currentPage
      });
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
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (medication) {
        medication.startDate = new Date(medication.startDate);
        medication.startTime = new Date(medication.startTime.valueOf() - medication.startTime.getTimezoneOffset() * 60000);

        var toAdd = {
          sourceId: '',
          doseAmount: medication.doseAmount,
          doseDirections: medication.doseDirections,
          doseTiming: medication.doseTiming,
          medicationCode: medication.medicationCode,
          medicationTerminology: medication.medicationTerminology,
          name: medication.name,
          route: medication.route,
          startDate: medication.startDate,
          startTime: medication.startTime,
          source: 'openehr'
        };

        Medication.create($scope.patient.id, toAdd).then(function () {
          $state.go('medications', {
            patientId: $scope.patient.id
          });
        });
      });
    };

  });
