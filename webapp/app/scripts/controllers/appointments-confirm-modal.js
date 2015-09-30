'use strict';

angular.module('rippleDemonstrator')
  .controller('AppointmentsConfirmModalCtrl', function ($scope, $modalInstance, time) {

    $scope.modal = $modalInstance;
    $scope.time = new Date(time);

    $scope.ok = function () {
      $modalInstance.close();
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

  });
