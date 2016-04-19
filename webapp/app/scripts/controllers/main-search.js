'use strict';

angular.module('rippleDemonstrator')
  .controller('MainSearchController', function ($scope, $state, AdvancedSearch, PatientService) {
    $scope.mainSearchEnabled = true;
    $scope.searchExpression = '';

    $scope.openAdvancedSearch = AdvancedSearch.openAdvancedSearch;

    $scope.cancelSearchMode = function () {
      $scope.searchExpression = '';
    };

    $scope.searchFunction = function(expression) {
      var nhsNumber = expression.replace(/\s+/g, '');

      if (!isNaN(nhsNumber) && nhsNumber.length == 10) {

        PatientService.get(nhsNumber).then(function (patient) {
          $scope.errorOccurred = false;
          goToPatientSummary(patient.nhsNumber);

        }).catch(function () {
          $scope.errorOccurred = true;
        });
      }
      else {
        $scope.errorOccurred = true;
      }
    };

    var goToPatientSummary = function (nhsNumber) {
      $state.go('patients-summary', {
        patientId: nhsNumber,
        filter: $scope.query
      });
    };

  });
