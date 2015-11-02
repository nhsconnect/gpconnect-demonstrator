'use strict';

angular.module('rippleDemonstrator')
  .controller('ProceduresDetailCtrl', function ($scope, $stateParams, SearchInput, $modal, $state, $location, Helper, usSpinnerService, PatientService, Procedure) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
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
          name: procedure.name,
          notes: procedure.notes,
          author: procedure.author,
          currentStatusTerminology: 'local',
          currentStatusCode: 'at0047',
          date: procedure.date,
          time: procedure.time,
          performer: procedure.performer,
          dateSubmitted: procedure.dateSubmitted,
          source: 'openehr'
        };

        Procedure.update($scope.patient.id, toUpdate).then(function () {
          setTimeout(function () {
            $state.go('procedures-detail', {
              patientId: $scope.patient.id,
              procedureId: Helper.updateId(procedure.sourceId),
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            });
          }, 2000);
        });
      });
    };

  });
