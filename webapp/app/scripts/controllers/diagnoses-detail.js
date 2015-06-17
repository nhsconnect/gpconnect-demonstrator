'use strict';

angular.module('openehrPocApp')
  .controller('DiagnosesDetailCtrl', function ($scope, $stateParams, $location, $modal, PatientService, Diagnosis) {

    $scope.UnlockedSources = [
      'handi.ehrscape.com'
    ];

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.diagnosis = $scope.result.problems[$stateParams.diagnosisIndex];
    });

    $scope.formDisabled = true;

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/diagnoses/diagnoses-modal.html',
        size: 'lg',
        controller: 'DiagnosesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Problem'
            };
          },
          diagnosis: function () {
            return angular.copy($scope.diagnosis);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        $scope.result.problems[$stateParams.diagnosisIndex] = diagnosis;

        Diagnosis.create($scope.patient.id, $scope.result).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/diagnoses');
        });
      });
    };

    $scope.isLocked = function (diagnosis) {
      if (!(diagnosis && diagnosis.id)) {
        return true;
      }
      var diagnosisIdSegments = diagnosis.id.toString().split('::');
      if (diagnosisIdSegments.length > 1) {
        return ($scope.UnlockedSources.indexOf(diagnosisIdSegments[1]) < 0);
      }
      return true;
    };

    $scope.convertToLabel = function (text) {
      var result = text.replace(/([A-Z])/g, ' $1');
      return result.charAt(0).toUpperCase() + result.slice(1);
    };
  });
