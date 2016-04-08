'use strict';

angular.module('rippleDemonstrator')
  .controller("AdvancedSearchController", function($scope, $modalInstance, modal, searchParams) {

    $scope.modal = modal;
    $scope.searchParams = searchParams;
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

    $scope.isNhsNumberRequired = function (nhsNumberFormField) {
      var nhsNumber = nhsNumberFormField.$viewValue;

      if (nhsNumber === undefined) {
        return true;
      }

      nhsNumber = nhsNumber.replace(/\s+/g, '');

      return isNaN(nhsNumber) || (nhsNumberFormField.$invalid && nhsNumber.length === 0);
    };

    $scope.isNhsNumberTooShort = function (value) {
      if (value === undefined) {
        return false;
      }

      var nhsNumber = value.replace(/\s+/g, '');

      return !isNaN(nhsNumber) && nhsNumber.length > 0 && nhsNumber.length < 10;
    };

    $scope.isNhsNumberTooLong = function (value) {
      if (value === undefined) {
        return false;
      }

      var nhsNumber = value.replace(/\s+/g, '');

      return !isNaN(nhsNumber) && nhsNumber.length > 10;
    };
  });
