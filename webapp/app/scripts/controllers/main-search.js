'use strict';

angular.module('gpConnect')
  .controller('MainSearchController', function ($scope, $state, $timeout, AdvancedSearch, PatientService) {

    $scope.mainSearchEnabled = true;
    $scope.searchExpression = '';
    $scope.errorOccurred = false;

    $scope.openAdvancedSearch = AdvancedSearch.openAdvancedSearch;

    $scope.cancelSearchMode = function () {
      $scope.searchExpression = '';
    };

    $scope.searchFunction = function (expression) {
      var nhsNumber = expression.replace(/\s+/g, '');

      if (!isNaN(nhsNumber) && nhsNumber.length == 10) {

        PatientService.get(nhsNumber).then(function (patient) {
          goToPatientSummary(patient.nhsNumber);

        }).catch(function () {
          $scope.applyError();
        });
      }
      else {
        $scope.applyError();
      }
    };

    $scope.applyError = function () {
      $timeout(function () {
        $scope.$apply(function () {
          $scope.errorOccurred = true;
        }, 1000);
      });
    };

    $scope.removeError = function () {
      $timeout(function () {
        $scope.$apply(function () {
          $scope.errorOccurred = false;
        }, 1000);
      });
    };

    $scope.populateSearchField = function (nhsNumber) {
      $timeout(function () {
        $scope.$apply(function () {
          $scope.searchExpression = nhsNumber;
        }, 1000);
      });
    };

    var goToPatientSummary = function (nhsNumber) {
      $state.go('patients-summary', {
        patientId: nhsNumber,
        filter: $scope.query
      });
    };
  });
