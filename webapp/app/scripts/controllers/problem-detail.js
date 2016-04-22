'use strict';

angular.module('gpConnect')
  .controller('ProblemDetailCtrl', function ($scope, $stateParams, $location, $modal, Helper, $state, usSpinnerService, PatientService, Problem) {

    $scope.UnlockedSources = [
      'handi.ehrscape.com'
    ];

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Problem.get($stateParams.patientId, $stateParams.problemIndex, $stateParams.source).then(function (result) {
      $scope.problem = result.data;
      usSpinnerService.stop('problemDetail-spinner');
    });

    $scope.formDisabled = true;

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/problem/problem-modal.html',
        size: 'lg',
        controller: 'ProblemModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Problem'
            };
          },
          problem: function () {
            return angular.copy($scope.problem);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (problem) {
        var toUpdate = {
          code: problem.code,
          dateOfOnset: problem.dateOfOnset,
          description: problem.description,
          problem: problem.problem,
          source: problem.source,
          sourceId: problem.sourceId,
          terminology: problem.terminology
        };

        Problem.update($scope.patient.id, toUpdate).then(function () {
          setTimeout(function () {
            $state.go('problem-detail', {
              patientId: $scope.patient.id,
              problemIndex: problem.source === 'openehr' ? Helper.updateId(problem.sourceId) : problem.sourceId,
              page: $scope.currentPage
            });
          }, 2000);
        });
      });
    };

    $scope.isLocked = function (problem) {
      if (!(problem && problem.id)) {
        return true;
      }

      var problemIdSegments = problem.id.toString().split('::');
      if (problemIdSegments.length > 1) {
        return ($scope.UnlockedSources.indexOf(problemIdSegments[1]) < 0);
      }

      return true;
    };

    $scope.convertToLabel = function (text) {
      var result = text.replace(/([A-Z])/g, ' $1');
      return result.charAt(0).toUpperCase() + result.slice(1);
    };

  });
