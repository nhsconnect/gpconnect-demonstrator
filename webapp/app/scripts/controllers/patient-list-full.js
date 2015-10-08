'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsListFullCtrl', function ($scope, $state, $stateParams, Report) {

    $scope.patients = [];

    var patientListQuery = {
      ageFrom: $stateParams.ageFrom,
      ageTo: $stateParams.ageTo,
      orderColumn: $stateParams.orderColumn,
      orderType: $stateParams.orderType,
      pageNumber: $stateParams.pageNumber,
      reportType: $stateParams.reportType,
      searchString: $stateParams.searchString
    }

    $scope.subHeader = $stateParams.queryType + $stateParams.reportType + ': ' + $stateParams.searchString + ' & Aged ' + $stateParams.ageFrom + ' to ' + $stateParams.ageTo;

    Report.getTable(patientListQuery).then(function (result) {
      $scope.patients = result.data.patientDetails;
    });

    $scope.order = $stateParams.order || 'name';
    $scope.reverse = $stateParams.reverse === 'true';

    $scope.sort = function (field) {
      var reverse = $scope.reverse;

      if ($scope.order === field) {
        reverse = !reverse;
      }

      $state.transitionTo($state.current, _.extend($stateParams, {
        order: field,
        reverse: reverse
      }));
    };

    $scope.sortClass = function (field) {
      if ($scope.order === field) {
        return $scope.reverse ? 'sort-desc' : 'sort-asc';
      }
    };

    $scope.go = function (patient) {
      $state.go('patients-summary', {
        patientId: patient.id
      });
    };

  });
