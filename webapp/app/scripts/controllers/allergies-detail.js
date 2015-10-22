'use strict';

angular.module('rippleDemonstrator')
  .controller('AllergiesDetailCtrl', function ($scope, $stateParams, $modal, $state, $location, Helper, usSpinnerService, PatientService, Allergy) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.get($stateParams.patientId, $stateParams.allergyIndex).then(function (result) {
      $scope.allergy = result.data;
      usSpinnerService.stop('allergiesDetail-spinner');
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
        var toUpdate = {
          sourceId: allergy.sourceId,
          cause: allergy.cause,
          causeCode: allergy.causeCode,
          causeTerminology: allergy.causeTerminology,
          reaction: allergy.reaction,
          source: 'openehr'
        };

        Allergy.update($scope.patient.id, toUpdate).then(function () {
          $state.go('allergies-detail', { patientId: $scope.patient.id, allergyIndex: Helper.updateId(allergy.sourceId) });
        });
      });
    };

  });
