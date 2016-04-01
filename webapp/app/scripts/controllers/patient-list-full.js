'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsListFullCtrl', function ($scope, $rootScope, $state, $stateParams, Report, Patient) {

    $scope.pagingInfo = {
      page: 1,
      totalItems: 0,
      orderType: 'ASC'
    };

    $scope.noResults = '';
    $scope.tab = 'patientInfo';
    $scope.patients = [];
    $rootScope.searchMode = true;

    function getPageInfo() {
      var from = (15 * $scope.pagingInfo.page - 14);
      var to = 0;
      var totalPages = 0;
      if ($scope.pagingInfo.totalItems % 15 === 0) {
        totalPages = $scope.pagingInfo.totalItems / 15;
      } else {
        totalPages = Math.floor($scope.pagingInfo.totalItems / 15) + 1;
      }
      if ($scope.pagingInfo.page === totalPages) {
        to = from + $scope.pagingInfo.totalItems % 15 - 1;
      } else {
        to = $scope.pagingInfo.page * 15;
      }
      var total = from + ' to ' + to + ' of ' + $scope.pagingInfo.totalItems;
      return total;
    }

    function getData() {
      if ($stateParams.queryType === 'Setting: ') {
        $rootScope.settingsMode = true;
        $rootScope.reportMode = false;
        $rootScope.patientMode = false;
        $rootScope.subHeader = $stateParams.queryType + $stateParams.searchString;
        var patientListQuery = {
          searchString: $stateParams.searchString,
          orderType: $stateParams.orderType,
          pageNumber: $stateParams.pageNumber
        };
        Report.getSettingsTable(patientListQuery).then(function (result) {
          $scope.patients = result.data.patientDetails;
          $scope.pagingInfo.totalItems = result.data.totalPatients;

          if ($scope.pagingInfo.totalItems === 0) {
            $scope.noResults = 'There are no results that match your search criteria';
          } else {
            $scope.processData();
          }
        });

      } else if ($stateParams.queryType === 'Reports: ') {
        $rootScope.reportMode = true;
        $rootScope.settingsMode = false;
        $rootScope.patientMode = false;
        $rootScope.reportTypeSet = true;
        $rootScope.reportTypeString = $stateParams.reportType;
        $scope.subHeader = $stateParams.queryType + $stateParams.reportType + ': ' + $stateParams.searchString + ' & Aged ' + $stateParams.ageFrom + ' to ' + $stateParams.ageTo;

        var patientReportListQuery = {
          ageFrom: $stateParams.ageFrom,
          ageTo: $stateParams.ageTo,
          orderType: $stateParams.orderType,
          pageNumber: $stateParams.pageNumber,
          reportType: $stateParams.reportType,
          searchString: $stateParams.searchString
        };

        Report.getTable(patientReportListQuery).then(function (result) {
          $scope.patients = result.data.patientDetails;
          $scope.pagingInfo.totalItems = result.data.totalPatients;

          if ($scope.pagingInfo.totalItems === 0) {
            $scope.noResults = 'There are no results that match your search criteria';
          } else {
            $scope.processData();
          }
        });
      } else {
        $rootScope.reportMode = false;
        $rootScope.settingsMode = false;
        $rootScope.reportTypeSet = false;
        $rootScope.patientMode = true;
        $rootScope.subHeader = $stateParams.queryType + $stateParams.searchString;
        var searchPatientQuery = {
          orderType: $stateParams.orderType,
          pageNumber: $stateParams.pageNumber,
          searchString: $stateParams.searchString
        };

        Report.searchByPatient(searchPatientQuery).then(function (result) {
//          $scope.patients = result.data.patientDetails;
          var patients = [];

                  angular.forEach(result.data.patientDetails, function (patient) {
                    patient = new Patient(patient);
                    patients.push(patient);
                  });

          $scope.patients = patients;
          $scope.pagingInfo.totalItems = result.data.totalPatients;
          if ($scope.pagingInfo.totalItems === 0) {
            $scope.noResults = 'There are no results that match your search criteria';
          } else {
            $scope.processData();
          }
        });
      }
    }

    $scope.processData = function () {
      for (var i = 0; i < $scope.patients.length; i++) {
        $scope.patients[i].ordersHeadline.latestEntry = $scope.processDateFormat($scope.patients[i].ordersHeadline.latestEntry);
        $scope.patients[i].vitalsHeadline.latestEntry = $scope.processDateFormat($scope.patients[i].vitalsHeadline.latestEntry);
        $scope.patients[i].medsHeadline.latestEntry = $scope.processDateFormat($scope.patients[i].medsHeadline.latestEntry);
        $scope.patients[i].resultsHeadline.latestEntry = $scope.processDateFormat($scope.patients[i].resultsHeadline.latestEntry);
        $scope.patients[i].treatmentsHeadline.latestEntry = $scope.processDateFormat($scope.patients[i].treatmentsHeadline.latestEntry);
        $scope.patients[i].ordersHeadline.totalEntries = $scope.processCounts($scope.patients[i].ordersHeadline.totalEntries);
        $scope.patients[i].vitalsHeadline.totalEntries = $scope.processCounts($scope.patients[i].vitalsHeadline.totalEntries);
        $scope.patients[i].medsHeadline.totalEntries = $scope.processCounts($scope.patients[i].medsHeadline.totalEntries);
        $scope.patients[i].resultsHeadline.totalEntries = $scope.processCounts($scope.patients[i].resultsHeadline.totalEntries);
        $scope.patients[i].treatmentsHeadline.totalEntries = $scope.processCounts($scope.patients[i].treatmentsHeadline.totalEntries);
      }
      $scope.pagingInfo.orderType = $stateParams.orderType;
      $scope.pagingInfo.page = $stateParams.pageNumber;
      $scope.pageInfoText = getPageInfo();
    };

    $scope.orderBy = function () {
      return $scope.pagingInfo.orderType === 'ASC' ? 'sort-asc' : 'sort-desc';
    };

    $scope.sort = function () {
      if ($stateParams.orderType === 'ASC') {
        $stateParams.orderType = 'DESC';
      } else {
        $stateParams.orderType = 'ASC';
      }
      $stateParams.pageNumber = 1;
      getData();
    };

    $scope.processCounts = function (countString) {
      return countString === null ? 0 : countString;
    };

    $scope.processDateFormat = function (dateString) {
      if (dateString === null) {
        return 'N/A';
      }
      dateString = moment(dateString);
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


    $scope.$watch('pagingInfo.page', function (page) {
      $scope.pagingInfo.page = page;
      $stateParams.pageNumber = page;
      getData();
    });

    $scope.clickGetItem = false;
    $scope.go = function (patient) {
      if (!$scope.clickGetItem) {
        $state.go('patients-summary', {
          patientId: patient.nhsNumber,
          ageFrom: $stateParams.ageFrom,
          ageTo: $stateParams.ageTo,
          orderType: $stateParams.orderType,
          pageNumber: $stateParams.pageNumber,
          reportType: $stateParams.reportType,
          searchString: $stateParams.searchString,
          queryType: $stateParams.queryType
        });
      }
      $scope.clickGetItem = false;
    };

    $scope.goToSection = function (itemType, nhsNumber) {
      $scope.clickGetItem = true;
      var requestHeader = {
        patientId: nhsNumber,
        ageFrom: $stateParams.ageFrom,
        ageTo: $stateParams.ageTo,
        orderType: $stateParams.orderType,
        pageNumber: $stateParams.pageNumber,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
      };

      var toState = '';

      switch (itemType) {
        case 'orders':
          toState = 'orders';
          break;
        case 'results':
          toState = 'results';
          break;
        case 'procedures':
          toState = 'procedures';
          break;
        case 'medications':
          toState = 'medications';
          break;
      }
      $state.go(toState, requestHeader);
    };

    $scope.getItem = function (itemType, nhsNumber, itemId) {
      $scope.clickGetItem = true;
      var requestHeader = {
        patientId: nhsNumber,
        ageFrom: $stateParams.ageFrom,
        ageTo: $stateParams.ageTo,
        orderType: $stateParams.orderType,
        pageNumber: $stateParams.pageNumber,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType
      };

      var toState = '';

      switch (itemType) {
      case 'orders':
        requestHeader.orderId = itemId;
        toState = 'orders-detail';
        break;
      case 'results':
        requestHeader.resultIndex = itemId;
        toState = 'results-detail';
        break;
      case 'procedures':
        requestHeader.procedureId = itemId;
        toState = 'procedures-detail';
        break;
      case 'medications':
        requestHeader.medicationIndex = itemId;
        toState = 'medications-detail';
        break;
      }
      $state.go(toState, requestHeader);
    };

    getData();

  });
