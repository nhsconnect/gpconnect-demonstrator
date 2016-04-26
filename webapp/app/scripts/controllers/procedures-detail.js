'use strict';

angular.module('gpConnect')
  .controller('ProceduresDetailCtrl', function ($scope, $stateParams, $modal, $state, $location, usSpinnerService, PatientService, Procedure) {

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Procedure.get($stateParams.patientId, $stateParams.procedureId, $stateParams.source).then(function (result) {
      $scope.procedure = result.data;
      usSpinnerService.stop('proceduresDetail-spinner');
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
        procedure.date.setMinutes(procedure.date.getMinutes() - procedure.date.getTimezoneOffset());

        var toUpdate = {
          sourceId: procedure.sourceId,
          procedureName: procedure.procedureName,
          procedureCode: procedure.procedureCode,
          procedureTerminology: procedure.procedureTerminology,
          notes: procedure.notes,
          author: procedure.author,
          date: procedure.date,
          time: procedure.time,
          performer: procedure.performer,
          dateSubmitted: procedure.dateSubmitted,
          source: procedure.source
        };

        Procedure.update($scope.patient.id, toUpdate).then(function () {
          setTimeout(function () {
            $state.go('procedures-detail', {
              patientId: $scope.patient.id,
              procedureId: procedure.sourceId,
              page: $scope.currentPage,
              source: $stateParams.source
            });
          }, 2000);
        });
      });
    };

  });
