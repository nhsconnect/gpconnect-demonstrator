'use strict';

angular.module('rippleDemonstrator')
  .controller('DiagnosesDetailCtrl', function ($scope, $stateParams, SearchInput, $location, $modal, Helper, $state, usSpinnerService, PatientService, Diagnosis) {

    SearchInput.update();
    $scope.UnlockedSources = [
      'handi.ehrscape.com'
    ];

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.get($stateParams.patientId, $stateParams.diagnosisIndex, $stateParams.source).then(function (result) {
      $scope.diagnosis = result.data;
      usSpinnerService.stop('diagnosisDetail-spinner');
    });

    $scope.formDisabled = true;

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/diagnoses/diagnoses-modal.html',
        size: 'lg',
        controller: 'DiagnosesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Problem / Diagnosis'
            };
          },
          diagnosis: function () {
            return angular.copy($scope.diagnosis);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        var toUpdate = {
          code: diagnosis.code,
          dateOfOnset: diagnosis.dateOfOnset,
          description: diagnosis.description,
          problem: diagnosis.problem,
          source: diagnosis.source,
          sourceId: diagnosis.sourceId,
          terminology: diagnosis.terminology
        };

        Diagnosis.update($scope.patient.id, toUpdate).then(function () {
          setTimeout(function () {
            $state.go('diagnoses-detail', {
              patientId: $scope.patient.id,
              diagnosisIndex: diagnosis.source === 'openehr' ? Helper.updateId(diagnosis.sourceId) : diagnosis.sourceId,
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            });
          }, 2000);
        });
      });
    };

    $scope.isLocked = function (diagnosis) {
      if (!(diagnosis && diagnosis.id)) {
        return true;
      }

      var diagnosisIdSegments = diagnosis.id.toString().split('::');
      if (diagnosisIdSegments.length > 1) {
        return ($scope.UnlockedSources.indexOf(diagnosisIdSegments[1]) < 0);
      }

      return true;
    };

    $scope.convertToLabel = function (text) {
      var result = text.replace(/([A-Z])/g, ' $1');
      return result.charAt(0).toUpperCase() + result.slice(1);
    };

  });
