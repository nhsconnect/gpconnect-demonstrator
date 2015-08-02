'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Procedure) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data[0];
      $scope.procedures = $scope.result.procedures;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (procedureIndex) {
      return procedureIndex === $stateParams.procedureIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/procedures/procedures-modal.html',
        size: 'lg',
        controller: 'ProceduresModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Procedure'
            };
          },
          procedure: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (procedures) {
        $scope.result.procedures.push(procedure);

        Contact.create($scope.patient.id, $scope.result).then(function () {
          $state.go('procedures', { patientId: $scope.patient.id });
        });
      });
    };

  });
