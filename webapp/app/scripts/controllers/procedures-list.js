'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Procedure) {

    $scope.search = function (row) {
      return (
          angular.lowercase(row.name).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.date).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.time).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
        );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Procedure.all($stateParams.patientId).then(function (result) {
      $scope.procedures = result.data;
      for (var i = 0; i < $scope.procedures.length; i++){
        $scope.procedures[i].date = moment($scope.procedures[i].date).format('DD-MMM-YYYY');
        $scope.procedures[i].time = moment($scope.procedures[i].time).format('HH:mm');    
      }
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (procedureId) {
      return procedureId === $stateParams.procedureId;
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
         procedure.date = new Date(procedure.date);  
         procedure.time = new Date(procedure.time.valueOf() - procedure.time.getTimezoneOffset() * 60000);   
         var toAdd = {
                    sourceId : '',
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

        Procedure.create($scope.patient.id, toAdd).then(function () {
          $state.go('procedures', { patientId: $scope.patient.id });
        });
      });
    };

  });
