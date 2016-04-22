'use strict';

angular.module('gpConnect')
  .controller('ProblemModalCtrl', function ($scope, $modalInstance, UserService, problem, patient, modal) {

    $scope.currentUser = UserService.getCurrentUser();
    $scope.problem = problem;
    $scope.patient = patient;
    $scope.modal = modal;
    $scope.protocol = 'http://';

    if (modal.title === 'Edit Problem') {
      $scope.problem.dateSubmitted = new Date().toISOString().slice(0, 10);
      $scope.problem.dateOfOnset = new Date($scope.problem.dateOfOnset).toISOString().slice(0, 10);
    }else {
      $scope.problem.dateSubmitted = new Date().toISOString().slice(0, 10);
      $scope.problem.code = '12393890';
    }

    $scope.changeProtocol = function (protocol) {
      switch (protocol) {
        case 'http':
          $scope.protocol = 'http://';
          break;
        case 'https':
          $scope.protocol = 'https://';
          break;
        default:
          $scope.protocol = 'http://';
      }
    };

    $scope.ok = function (problemForm, problem) {
      $scope.formSubmitted = true;

      if (problemForm.$valid) {
        $modalInstance.close(problem);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

  });
