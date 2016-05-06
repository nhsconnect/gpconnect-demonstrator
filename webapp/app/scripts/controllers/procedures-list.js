'use strict';

angular.module('gpConnect')
  .controller('ProceduresListCtrl', function ($scope, $location, $stateParams, $sce, $modal, $state, usSpinnerService, PatientService, Procedure) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.name).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.date).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.time).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Procedure.findAllHTMLTables($stateParams.patientId).then(function (result) {
      $scope.procedureTables = result.data;

      for (var i = 0; i < $scope.procedureTables.length; i++) {
        $scope.procedureTables[i].html = $sce.trustAsHtml($scope.procedureTables[i].html);
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id, procedureSource) {
      $state.go('procedures-detail', {
        patientId: $scope.patient.id,
        procedureId: id,
        filter: $scope.query,
        page: $scope.currentPage,
        source: procedureSource
      });
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
            return {};
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
        procedure.time = new Date(procedure.time.valueOf() - procedure.time.getTimezoneOffset() * 60000);

        var toAdd = {
          procedureName: procedure.procedureName,
          procedureTerminology: procedure.procedureTerminology,
          procedureCode: procedure.procedureCode,
          notes: procedure.notes,
          author: procedure.author,
          date: procedure.date,
          time: procedure.time,
          performer: procedure.performer,
          dateSubmitted: procedure.dateSubmitted
        };

        Procedure.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('procedures', {
              patientId: $scope.patient.id,
              filter: $scope.query,
              page: $scope.currentPage
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
