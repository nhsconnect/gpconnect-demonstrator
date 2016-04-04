'use strict';

angular.module('rippleDemonstrator')
  .controller("AdvancedSearchController", function($scope, $modalInstance, modal) {

    $scope.modal = modal;
    $scope.searchParams = {};
    $scope.formSubmitted = false;
    $scope.detailsFocused = false;

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.ok = function(searchForm) {
      $scope.formSubmitted = true;

      if ((!$scope.detailsFocused && searchForm.nhsNumber.$valid) ||
           ($scope.detailsFocused && searchForm.lastName.$valid && searchForm.firstName.$valid && searchForm.dateOfBirth.$valid)) {

        $modalInstance.close($scope.searchParams);
      }
    };

    $scope.openDatePicker = function($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };
  });
