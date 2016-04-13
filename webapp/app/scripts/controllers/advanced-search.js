'use strict';

angular.module('rippleDemonstrator')
  .controller("AdvancedSearchController", function ($scope, $modalInstance, $state, modal, searchParams, AdvancedSearch) {

    $scope.modal = modal;
    $scope.searchParams = searchParams;
    $scope.formSubmitted = false;
    $scope.detailsFocused = false;
    $scope.modalReopened = false;

    if ($scope.searchParams.nhsNumber) {
      $scope.nhsNumberFocus = true;
    }
    else if ($scope.searchParams.surname) {
      $scope.surnameFocus = true;
    }
    else {
      $scope.nhsNumberFocus = true;
    }

    if ($scope.searchParams.dateOfBirth) {
      $scope.searchParams.dateOfBirth = new Date($scope.searchParams.dateOfBirth).toISOString().slice(0, 10);
      $scope.detailsFocused = true;
      $scope.modalReopened = true;
    }

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.ok = function (searchForm) {
      if (searchForm.$valid) {
        AdvancedSearch.searchByDetails($scope.searchParams).then(function (result) {
          $scope.patients = result.data;
          changeState();
        });
      }
    };

    var changeState = function () {
      $scope.formSubmitted = true;
      $modalInstance.close();

      if ($scope.patients.length == 1) {
        $state.go('patients-summary', {
          patientId: $scope.patients[0].nhsNumber
        });
      }
      else if ($scope.patients.length > 1) {
        $state.go('patients-list', {
          patientsList: $scope.patients,
          advancedSearchParams: $scope.searchParams
        });
      }
      else {
        $state.go('patients-list', {
          patientsList: $scope.patients,
          advancedSearchParams: $scope.searchParams,
          displayEmptyTable: true
        });
      }
    };

    $scope.openDatePicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.isNhsNumberRequired = function (advancedSearchForm) {
      var nhsNumber = $scope.advancedSearchForm.nhsNumber.$viewValue;

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

    $scope.areDetailsFieldsClean = function (advancedSearchForm) {
      var surname = advancedSearchForm.surname;
      var forename = advancedSearchForm.forename;
      var dateOfBirth = advancedSearchForm.dateOfBirth;

      var surnameClean = surname.$invalid || !$scope.searchParams.surname || $scope.searchParams.surname == '';
      var forenameClean = forename.$invalid || !$scope.searchParams.forename || $scope.searchParams.forename == '';
      var dateOfBirthClean = dateOfBirth.$invalid || !$scope.searchParams.dateOfBirth || $scope.searchParams.dateOfBirth == '';

      return surnameClean && forenameClean && dateOfBirthClean;
    };
  });
