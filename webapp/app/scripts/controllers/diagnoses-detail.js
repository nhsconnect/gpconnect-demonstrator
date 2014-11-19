'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsDetailCtrl
 * @description
 * # PatientsDetailCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('DiagnosesDetailCtrl', function ($scope, $stateParams, $location, $modal, Patient, Diagnosis, growlNotifications) {

    $scope.UnlockedSources = [
      'handi.ehrscape.com'
    ];

    $scope.patient = Patient.get($stateParams.patientId);

    Diagnosis.findByPatient($scope.patient.id, $stateParams.diagnosisId).then(function (result) {
      $scope.diagnosis = result;
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
              title: 'Edit Diagnosis'
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
        Diagnosis.updateByPatient($scope.patient.id, diagnosis).then(function (result) {
          growlNotifications.add('<strong>'+ $scope.patient.fullname() + ':</strong> Diagnosis updated', 'success', 10000);
          $scope.diagnosis = result.data;
        });
      });
    };

    $scope.isLocked = function (diagnosis) {
      if (! (diagnosis && diagnosis.id)) {
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
