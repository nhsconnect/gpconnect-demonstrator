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

    $scope.isNhsNumberRequired = function (advancedSearchForm) {
      var nhsNumber = advancedSearchForm.nhsNumber.$viewValue;

      if (nhsNumber === undefined && $scope.areDetailsFieldsClean(advancedSearchForm)) {
        return true;
      }

      nhsNumber = nhsNumber.replace(/\s+/g, '');

      var nhsNumberInvalid = isNaN(nhsNumber) || (advancedSearchForm.nhsNumber.$invalid && nhsNumber.length === 0);

      return nhsNumberInvalid && $scope.areDetailsFieldsClean(advancedSearchForm);
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

    $scope.isNhsNumberFieldInvalid = function (nhsNumberField) {
      return nhsNumberField.$invalid || nhsNumberField.$pristine;
    };

    $scope.areDetailsFieldsClean = function(advancedSearchForm) {
      var lastName = advancedSearchForm.lastName;
      var firstName = advancedSearchForm.firstName;
      var dateOfBirth = advancedSearchForm.dateOfBirth;

      var lastNameValid = lastName.$invalid || lastName.$pristine || lastName.$viewValue === '';
      var firstNameValid = firstName.$invalid || firstName.$pristine || firstName.$viewValue === '';
      var dateOfBirthValid = dateOfBirth.$invalid || dateOfBirth.$pristine || dateOfBirth.$viewValue === '';

      return lastNameValid && firstNameValid && dateOfBirthValid;
    };

    $scope.areDetailsFieldsInvalid = function (advancedSearchForm) {
      return advancedSearchForm.lastName.$invalid || advancedSearchForm.firstName.$invalid || advancedSearchForm.dateOfBirth.$invalid;
    }
  });
