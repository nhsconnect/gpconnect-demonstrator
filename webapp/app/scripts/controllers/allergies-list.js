'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Allergy) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.allergies = $scope.result.allergies;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function ($index) {
      return $index === $stateParams.allergyIndex;
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
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (allergy) {
        $scope.result.allergies.push(allergy);

        Allergy.update($scope.patient.id, $scope.result).then(function () {
          $state.go('allergies', { patientId: $scope.patient.id });
        });
      });
    };

  });


