'use strict';

angular.module('gpConnect')
  .controller('ProblemDetailCtrl', function ($scope, $stateParams, $location, $modal, $state, usSpinnerService, PatientService, Problem) {

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    Problem.findDetails($stateParams.patientId, $stateParams.problemIndex, $stateParams.source).then(function (result) {
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
              problemIndex: problem.sourceId,
              page: $scope.currentPage
            });
          }, 2000);
        });
      });
    };

    $scope.convertToLabel = function (text) {
      var result = text.replace(/([A-Z])/g, ' $1');
      return result.charAt(0).toUpperCase() + result.slice(1);
    };

  });
