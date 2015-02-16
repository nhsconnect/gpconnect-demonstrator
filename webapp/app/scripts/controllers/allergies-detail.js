'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Allergy) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.get($stateParams.allergyId).then(function (allergy) {
      $scope.allergy = allergy;
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/allergies/allergies-modal.html',
        size: 'lg',
        controller: 'AllergiesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Allergy'
            };
          },
          allergy: function () {
            return angular.copy($scope.allergy);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (allergy) {
        Allergy.updateByPatient($scope.patient.id, diagnosis).then(function (result) {
          $scope.diagnosis = result.data;
          $location.path('/patients/' + $scope.patient.id + '/allergies/' + $scope.allergy.id);
        });
      });
    };

  });
