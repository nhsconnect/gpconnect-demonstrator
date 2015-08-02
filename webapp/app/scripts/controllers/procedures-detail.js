'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Procedure) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.all($stateParams.procedureId).then(function (result) {
      $scope.result = result.data[0];
      $scope.procedure = $scope.result.procedures[$stateParams.procedureIndex];
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/procedures/procedures-modal.html',
        size: 'lg',
        controller: 'ProceduresModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Procedure'
            };
          },
          procedure: function () {
            return angular.copy($scope.procedure);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (procedure) {
        $scope.result.procedures[$stateParams.procedureIndex] = procedure;

        Contact.create($scope.patient.id, $scope.result).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/procedures');
        });
      });
    };

  });
