'use strict';

angular.module('rippleDemonstrator')
  .controller('EolcareplansListCtrl', function ($scope, $location, $stateParams, SearchInput, $modal, usSpinnerService, $state, PatientService, Eolcareplan) {

    $scope.currentPage = 1;
    SearchInput.update();

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.name).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.type).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.date).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Eolcareplan.all($stateParams.patientId).then(function (result) {
      $scope.eolcareplans = result.data;

      if (result.data.length > 0) {
        for (var i = 0; i < $scope.eolcareplans.length; i++) {
          $scope.eolcareplans[i].date = moment($scope.eolcareplans[i].date).format('DD-MMM-YYYY');
        }
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('eolcareplans-detail', {
        patientId: $scope.patient.id,
        eolcareplansIndex: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
      });
    };

    $scope.selected = function (eolcareplansIndex) {
      return eolcareplansIndex === $stateParams.eolcareplansIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/care-plans/eolcareplans-modal.html',
        size: 'lg',
        controller: 'EolcareplansModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create End of Life Care Document'
            };
          },
          eolcareplan: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (eolcareplan) {
        eolcareplan.careDocument.type = eolcareplan.careDocument.type.type;
        eolcareplan.careDocument.dateCreated = new Date(eolcareplan.careDocument.dateCreated);
        eolcareplan.cprDecision.dateOfDecision = new Date(eolcareplan.cprDecision.dateOfDecision);
        eolcareplan.treatmentDecision.dateOfDecision = new Date(eolcareplan.treatmentDecision.dateOfDecision);

        var toAdd = {
          sourceId: '',
          careDocument: eolcareplan.careDocument,
          cprDecision: eolcareplan.cprDecision,
          prioritiesOfCare: eolcareplan.prioritiesOfCare,
          source: 'openehr',
          treatmentDecision: eolcareplan.treatmentDecision
        };

        Eolcareplan.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('eolcareplans', {
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
