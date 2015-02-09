'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:DiagnosesListCtrl
 * @description
 * # DiagnosesListCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('DiagnosesListCtrl', function ($scope, $stateParams, $location, $modal, PatientService, Diagnosis) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;

      Diagnosis.byPatient($scope.patient.id).then(function (result) {
        $scope.patient.diagnoses = result;
      });
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (diagnosis) {
      return diagnosis.id === $stateParams.diagnosisId;
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
            return { dateOfOnset: new Date() };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        Diagnosis.createByPatient($scope.patient.id, diagnosis).then(function (result) {
          $scope.patient.diagnoses.push(result.data);
          console.log(result);
        });
      });
    };
  });
