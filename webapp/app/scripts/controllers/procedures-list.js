'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Procedure) {

    $scope.search = function (row) {
      return (
          angular.lowercase(row.procedureName).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.dateOfProcedure).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.timeOfProcedure).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
        );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.all($stateParams.patientId).then(function (result) {
      $scope.procedures = result.data;
      for (var i = 0; i < $scope.procedures.length; i++){
        $scope.procedures[i].dateOfProcedure = moment($scope.procedures[i].dateOfProcedure).format('DD-MMM-YYYY');
        $scope.procedures[i].timeOfProcedure = moment($scope.procedures[i].timeOfProcedure).format('HH:mm');
      }
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

      modalInstance.result.then(function (procedure) {
        procedure.dateSubmitted = new Date(procedure.dateSubmitted);
        procedure.dateOfProcedure = new Date(procedure.dateOfProcedure);
        procedure.timeOfProcedure = new Date(procedure.timeOfProcedure.valueOf() - procedure.timeOfProcedure.getTimezoneOffset() * 60000);
        var toAdd = {
                    compositionId : '',
                    procedureName : procedure.procedureName,
                    procedureNotes : procedure.procedureNotes,
                    author : procedure.author,
                    terminology : 'local',
                    code : 'at0047',
                    dateOfProcedure : procedure.dateOfProcedure,
                    timeOfProcedure : procedure.timeOfProcedure,
                    performer : procedure.performer,
                    dateSubmitted : procedure.dateSubmitted,
                    source : 'openehr'
                    };

        Procedure.create($scope.patient.id, toAdd).then(function () {
          $state.go('procedures', { patientId: $scope.patient.id });
        });
      });
    };

  });
