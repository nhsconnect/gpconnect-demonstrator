'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsListFullCtrl', function ($scope, $rootScope, $state, $stateParams, Report) {

      $scope.tab = 'patientInfo';
      $scope.patients = [];
      $rootScope.searchMode = true;
      if ($stateParams.queryType === 'Setting: ') {
        $rootScope.settingsMode = true;
        $rootScope.reportMode = false;
        $rootScope.subHeader = $stateParams.queryType + $stateParams.searchString;
        var patientListQuery = {
          searchString: $stateParams.searchString,
          orderType: $stateParams.orderType,
          pageNumber: $stateParams.pageNumber
        }
        Report.getSettingsTable(patientListQuery).then(function (result) {
        $scope.patients = result.data.patientDetails;
        for (var i = 0; i < $scope.patients.length; i++) {
          $scope.patients[i].ordersHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].ordersHeadline.latestEntry));
          $scope.patients[i].medsHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].medsHeadline.latestEntry));
          $scope.patients[i].resultsHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].resultsHeadline.latestEntry));
          $scope.patients[i].treatmentsHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].treatmentsHeadline.latestEntry));
        }
        });

    } else {
      $rootScope.reportMode = true;
      $rootScope.settingsMode = false;
      $scope.subHeader = $stateParams.queryType + $stateParams.reportType + ': ' + $stateParams.searchString + ' & Aged ' + $stateParams.ageFrom + ' to ' + $stateParams.ageTo;

      var patientListQuery = {
        ageFrom: $stateParams.ageFrom,
        ageTo: $stateParams.ageTo,
        orderType: $stateParams.orderType,
        pageNumber: $stateParams.pageNumber,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString
      }

      Report.getTable(patientListQuery).then(function (result) {
        $scope.patients = result.data.patientDetails;
        for (var i = 0; i < $scope.patients.length; i++) {
          $scope.patients[i].ordersHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].ordersHeadline.latestEntry));
          $scope.patients[i].medsHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].medsHeadline.latestEntry));
          $scope.patients[i].resultsHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].resultsHeadline.latestEntry));
          $scope.patients[i].treatmentsHeadline.latestEntry = $scope.processDateFormat(moment($scope.patients[i].treatmentsHeadline.latestEntry));
        }
      });
    }

    $scope.processDateFormat = function (dateString) {
      if (moment().diff(dateString, 'days') < 1) {
        return dateString.format('h:mm a');
      }
      if (moment().startOf('year') <= dateString) {
        return dateString.format('DD-MMM');
      }
      if (moment().startOf('year').subtract(1, 'year') < dateString) {
        return dateString.format('MMM-YY');
      }
      return dateString.format('YYYY');
    }

    $scope.order = $stateParams.order || 'name'; $scope.reverse = $stateParams.reverse === 'true';

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

    $scope.viewPatients = function () {
      $scope.tab = 'patientInfo';
    };

    $scope.viewDateTime = function () {
      $scope.tab = 'dateTime';
    };

    $scope.viewCounts = function () {
      $scope.tab = 'counts';
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
