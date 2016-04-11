'use strict';

angular.module('rippleDemonstrator')
  .controller("AdvancedSearchController", function ($scope, $modalInstance, modal, searchParams, AdvancedSearch) {

    $scope.modal = modal;
    $scope.searchParams = searchParams;
    $scope.formSubmitted = false;
    $scope.detailsFocused = false;

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.ok = function (searchForm) {
      var nhsNumber = $scope.searchParams.nhsNumber;

      if (nhsNumber != undefined && !$scope.isNhsNumberRequired(searchForm) && !$scope.isNhsNumberTooShort(nhsNumber) && !$scope.isNhsNumberTooShort(nhsNumber)) {
        AdvancedSearch.searchByNhsNumber(nhsNumber).then(function (result) {
          $scope.patients = [];
          $scope.patients.push(result.data);
        });

        $scope.formSubmitted = true;
        $modalInstance.close();
      }
      else if (!$scope.areDetailsFieldsClean(searchForm)) {
        AdvancedSearch.searchByDetails($scope.searchParams).then(function (result) {
          $scope.patients = result.data;
        });

        $scope.formSubmitted = true;
        $modalInstance.close();
      }
    };

    $scope.openDatePicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.isNhsNumberRequired = function (advancedSearchForm) {
      var nhsNumber = $scope.searchParams.nhsNumber;

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

      var surnameClean = surname.$invalid || surname.$pristine || $scope.searchParams.surname == '';
      var forenameClean = forename.$invalid || forename.$pristine || $scope.searchParams.forename == '';
      var dateOfBirthClean = dateOfBirth.$invalid || dateOfBirth.$pristine || $scope.searchParams.dateOfBirth == '';

      return surnameClean && forenameClean && dateOfBirthClean;
    };
  });
