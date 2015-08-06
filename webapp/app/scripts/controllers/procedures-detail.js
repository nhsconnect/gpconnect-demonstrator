'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Procedure, DateFormatter) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.all($stateParams.procedureId).then(function (result) {
      $scope.result = result.data;
      $scope.procedure = $scope.result[$stateParams.procedureIndex];
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
         procedure.dateOfProcedure = new Date(procedure.dateOfProcedure);  
         var toUpdate = {
                    compositionId : $scope.procedure.compositionId,
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
          
        Procedure.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/procedures');
        });
       });
    };

  });
