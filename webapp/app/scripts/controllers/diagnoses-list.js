'use strict';

angular.module('rippleDemonstrator')
  .controller('DiagnosesListCtrl', function ($scope, $state, $stateParams, SearchInput, $location, $modal, usSpinnerService, PatientService, Diagnosis) {

    SearchInput.update();
    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.problem).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.dateOfOnset).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.all($stateParams.patientId).then(function (result) {
      $scope.diagnoses = result.data;

      for (var i = 0; i < $scope.diagnoses.length; i++) {
        $scope.diagnoses[i].dateOfOnset = moment($scope.diagnoses[i].dateOfOnset).format('DD-MMM-YYYY');
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id, diagnosisSource) {
      $state.go('diagnoses-detail', {
        patientId: $scope.patient.id,
        diagnosisIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType,
        source: diagnosisSource
      });
    };

    $scope.selected = function (diagnosisIndex) {
      return diagnosisIndex === $stateParams.diagnosisIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/diagnoses/diagnoses-modal.html',
        size: 'lg',
        controller: 'DiagnosesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Problem / Diagnosis'
            };
          },
          diagnosis: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        diagnosis.dateOfOnset = new Date(diagnosis.dateOfOnset);

        var toAdd = {
          code: diagnosis.code,
          dateOfOnset: diagnosis.dateOfOnset,
          description: diagnosis.description,
          problem: diagnosis.problem,
          source: diagnosis.source,
          sourceId: '',
          terminology: diagnosis.terminology
        };

        Diagnosis.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('diagnoses-list', {
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
