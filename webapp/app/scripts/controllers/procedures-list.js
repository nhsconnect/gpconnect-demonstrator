'use strict';

angular.module('rippleDemonstrator')
  .controller('ProceduresListCtrl', function ($scope, $location, $stateParams, SearchInput, $modal, $state, usSpinnerService, PatientService, Procedure) {

    $scope.currentPage = 1;

    SearchInput.update();

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

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Procedure.all($stateParams.patientId).then(function (result) {
      $scope.procedures = result.data;

      for (var i = 0; i < $scope.procedures.length; i++) {
        $scope.procedures[i].date = moment($scope.procedures[i].date).format('DD-MMM-YYYY');
        $scope.procedures[i].time = moment($scope.procedures[i].time).format('HH:mm');
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id, procedureSource) {
      $state.go('procedures-detail', {
        patientId: $scope.patient.id,
        procedureId: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType,
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
          sourceId: '',
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
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
