'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, $modal, PatientService, Allergy) {

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

      if( $index === $stateParams.allergyIndex){
        alert($index);
      }

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

        Allergy.update($scope.patient.id, $scope.result).then(function (result) {
          $scope.patient.allergies.push(result.data);
        });
      });
    };

  });
