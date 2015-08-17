'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Procedure) {

    $scope.query = {};
    $scope.queryBy = '$';
    
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.all($stateParams.patientId).then(function (result) {
      $scope.procedures = result.data;
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
                    }

        Procedure.create($scope.patient.id, toAdd).then(function () {
          $state.go('procedures', { patientId: $scope.patient.id });
        });
        
      });
    };

  });
