'use strict';

angular.module('rippleDemonstrator')
  .controller('AllergiesListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Allergy) {

    $scope.search = function (row) {
      return (
        angular.lowercase(row.cause).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.reaction).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Allergy.all($stateParams.patientId).then(function (result) {
      $scope.allergies = result.data;
    });

    $scope.go = function (id) {
      $location.path('/patients/' + $scope.patient.id + '/allergies/' + id);
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
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (allergy) {
        var toAdd = {
          sourceId: '',
          cause: allergy.cause,
          causeCode: allergy.causeCode,
          causeTerminology: allergy.causeTerminology,
          reaction: allergy.reaction,
          source: 'openehr'
        };

        Allergy.create($scope.patient.id, toAdd).then(function () {
          $state.go('allergies', {
            patientId: $scope.patient.id
          });
        });
      });
    };

  });
