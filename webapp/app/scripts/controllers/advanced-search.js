'use strict';

angular.module('rippleDemonstrator')
  .controller("AdvancedSearchController", function($scope, $modalInstance, modal) {

    $scope.modal = modal;

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };
  });
