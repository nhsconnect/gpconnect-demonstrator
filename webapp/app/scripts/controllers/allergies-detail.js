'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Allergy) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.allergy = $scope.result.allergies[$stateParams.allergyIndex];
      $scope.allergySelectionNumber = $stateParams.allergyIndex + 1;
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
        $scope.result.allergies[$stateParams.allergyIndex] = allergy;

        Allergy.update($scope.patient.id, $scope.result).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/allergies');
        });
      });
    };

  });
