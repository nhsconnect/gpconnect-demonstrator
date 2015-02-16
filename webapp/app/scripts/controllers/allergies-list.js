'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, $modal, PatientService, Allergy) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.byPatient($stateParams.patientId).then(function (result) {
      $scope.allergies = result.data;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (allergy) {
      return allergy.id === $stateParams.allergyId;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/allergies/allergies-modal.html',
        size: 'lg',
        controller: 'AllergiesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Allergy'
            };
          },
          allergy: function () {
            return { date: new Date() };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (allergy) {
        Allergy.createByPatient($scope.patient.id, allergy).then(function (result) {
          $scope.patient.allergies.push(result.data);
        });
      });
    };

  });
