'use strict';

angular.module('openehrPocApp')
  .controller('DiagnosesListCtrl', function ($scope, $state, $stateParams, $location, $modal, PatientService, Diagnosis) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.diagnoses = $scope.result.problems;
      console.log('original diagnosis');
      console.log($scope.result);
      console.log('original diagnosis array');
      console.log($scope.result.problems);
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (diagnosisIndex) {
      return diagnosisIndex === $stateParams.diagnosisIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/diagnoses/diagnoses-modal.html',
        size: 'lg',
        controller: 'DiagnosesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Diagnosis'
            };
          },
          diagnosis: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        $scope.result.problems.push(diagnosis);

        Diagnosis.create($scope.patient.id, $scope.result).then(function () {
          $state.go('diagnoses-list', { patientId: $scope.patient.id });
        });
      });
    };
  });
