'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresDetailCtrl', function ($scope, $stateParams, $modal, $state, $location, PatientService, Procedure) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.get($stateParams.patientId, $stateParams.procedureId).then(function (result) {
      $scope.procedure = result.data;
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
        procedure.dateSubmitted = new Date(procedure.dateSubmitted);
        procedure.date = new Date(procedure.date);
        var toUpdate = {
                    sourceId : procedure.sourceId,
                    name : procedure.name,
                    notes : procedure.notes,
                    author : procedure.author,
                    currentStatusTerminology : 'local',
                    currentStatusCode : 'at0047',
                    date : procedure.date,
                    time : procedure.time,
                    performer : procedure.performer,
                    dateSubmitted : procedure.dateSubmitted,
                    source : 'openehr'
                    };

        Procedure.update($scope.patient.id, toUpdate).then(function () {
          // $location.path('/patients/' + $scope.patient.id + '/procedures/' + procedure.sourceId);
          $state.go('procedures', { patientId: $scope.patient.id });
        });
      });
    };

  });
