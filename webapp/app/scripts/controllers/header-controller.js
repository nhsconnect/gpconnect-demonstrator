'use strict';

angular.module('rippleDemonstrator')
  .controller('headerController', function ($scope, $rootScope, $state, usSpinnerService, $stateParams, PatientService) {


    var role = $stateParams.role;
    var email = $stateParams.email;
    $rootScope.searchExpression = '';
    $scope.searchExpression = $rootScope.searchExpression;
    $scope.reportTypes = [];

    // Set current user
    PatientService.setCurrentUser(role, email);

    // Get current user
    $scope.currentUser = PatientService.getCurrentUser();

    // Temporary default user
    if (!$scope.currentUser.role) {
      $scope.currentUser.role = 'idcr';
      $scope.currentUser.email = 'example@email.com';
    }

    // Direct different roles to different pages at login
    switch ($scope.currentUser.role) {
    case 'idcr':
      $state.go('patients-charts');
      break;
    case 'phr':
      $state.go('patients-summary', {
        patientId: 10
      }); // id is hard coded
      break;
    default:
      $state.go('patients-summary', {
        patientId: 10
      }); // id is hard coded
    }

    $rootScope.$on('$stateChangeSuccess', function (event, toState, fromState) {
      var params = $stateParams;
      var previousState = '';
      var pageHeader = '';
      var previousPage = '';

      var mainWidth = 0;
      var detailWidth = 0;

      switch (toState.name) {
      case 'patients-list':
        previousState = 'patients-charts';
        pageHeader = 'Patient Lists';
        previousPage = 'Patient Dashboard';
        mainWidth = 12;
        detailWidth = 0;
        break;
      case 'patients-charts':
        previousState = '';
        pageHeader = 'Patient Dashboard';
        previousPage = '';
        mainWidth = 12;
        detailWidth = 0;
        break;
      case 'patients-summary':
        previousState = 'patients-list';
        pageHeader = 'Patient Summary';
        previousPage = 'Patient Lists';
        mainWidth = 12;
        detailWidth = 0;
        break;
      case 'patients-lookup':
        previousState = '';
        pageHeader = 'Patients lookup';
        previousPage = '';
        mainWidth = 6;
        detailWidth = 6;
        break;
      case 'search-report':
        previousState = 'patients-charts';
        pageHeader = 'Report Search';
        previousPage = 'Patient Dashboard';
        mainWidth = 12;
        detailWidth = 0;
        break;
      case 'patients-list-full':
        previousState = 'patients-charts';
        pageHeader = 'Patients Details';
        previousPage = 'Patient Dashboard';
        mainWidth = 12;
        detailWidth = 0;
        break;
      default:
        previousState = 'patients-list';
        pageHeader = 'Patients Details';
        previousPage = 'Patient Lists';
        mainWidth = 6;
        detailWidth = 6;
        break;
      }

      if (params.queryType === 'Reports: ') {
        previousState = 'search-report';
        previousPage = 'Report Chart';
      }

      $scope.containsReportString = function () {
        return $scope.searchExpression.indexOf('rp ') === 0 ? true : false;
      };

      $scope.containsSettingString = function () {
        return $scope.searchExpression.lastIndexOf('st ') === 0 ? true : false;
      };

      $scope.containsPatientString = function () {
        return $scope.searchExpression.lastIndexOf('pt ') === 0 ? true : false;
      };

      $scope.containsReportTypeString = function () {
        for (var i = 0; i < $scope.reportTypes.length; i++) {
          if ($scope.searchExpression.lastIndexOf($scope.reportTypes[i]) !== -1) {
            return true;
          }
        }
        return false;
      };



      $rootScope.searchMode = false;
      $rootScope.reportMode = false;
      $rootScope.settingsMode = false;
      $rootScope.patientMode = false;
      $rootScope.reportTypeSet = false;
      $rootScope.reportTypeString = '';

      $scope.checkExpression = function () {
        if ($rootScope.searchMode) {
          if ($rootScope.reportMode && !$rootScope.reportTypeSet) {
            $scope.reportTypes = [
              'Diagnosis: ',
              'Orders: '
              ];
          }
          if ($scope.containsReportTypeString() && !$scope.patientMode) {
            $rootScope.reportTypeSet = true;
            $scope.processReportTypeMode();
          }
        } else {
          $scope.reportTypes = [];
          $rootScope.searchMode = ($scope.containsReportString() || $scope.containsSettingString() || $scope.containsPatientString());
          $rootScope.reportMode = $scope.containsReportString();
          $rootScope.settingsMode = $scope.containsSettingString();
          $rootScope.patientMode = $scope.containsPatientString();
          if ($rootScope.reportMode) {
            if ($scope.containsReportTypeString) {
              $scope.processReportTypeMode();
            }
            $scope.processReportMode();
          }
          if ($rootScope.settingsMode) {
            $scope.processSettingMode();
          }
          if ($rootScope.patientMode) {
            $scope.processPatientMode();
          }
        }
      };

      $scope.cancelSearchMode = function () {
        $rootScope.reportMode = false;
        $rootScope.searchMode = false;
        $rootScope.patientMode = false;
        $rootScope.settingsMode = false;
        $scope.searchExpression = '';
        $scope.reportTypes = '';
        $rootScope.reportTypeSet = false;
        $rootScope.reportTypeString = '';
      };

      $scope.cancelReportType = function () {
        $rootScope.reportTypeString = '';
        $rootScope.reportTypeSet = false;
      };

      $scope.searchFunction = function () {
        if ($rootScope.reportTypeSet && $scope.searchExpression !== '') {
          var tempExpression = $rootScope.reportTypeString + ': ' + $scope.searchExpression;
          $state.go('search-report', {
            searchString: tempExpression
          });
        }
        if ($rootScope.settingsMode && $scope.searchExpression !== '') {
          $state.go('patients-list-full', {
            queryType: 'Setting: ',
            searchString: $scope.searchExpression,
            orderType: 'ASC',
            pageNumber: '1'
          });
        }
        if ($rootScope.patientMode && $scope.searchExpression !== '') {
          $state.go('patients-list-full', {
            queryType: 'Patient: ',
            searchString: $scope.searchExpression,
            orderType: 'ASC',
            pageNumber: '1'
          });
        }
      };

      $scope.processReportMode = function () {
        if ($scope.searchExpression === 'rp ') {
          $scope.searchExpression = '';
        }
      };

      $scope.processReportTypeMode = function () {
        for (var i = 0; i < $scope.reportTypes.length; i++) {
          if ($scope.searchExpression.lastIndexOf($scope.reportTypes[i]) !== -1) {
            var arr = $scope.searchExpression.split(':');
            $rootScope.reportTypeString = arr[0];
            $rootScope.reportTypeSet = true;
            $scope.searchExpression = '';
          }
        }
        $scope.reportTypes = [];
      };

      $scope.processSettingMode = function () {
        if ($scope.searchExpression === 'st ') {
          $scope.searchExpression = '';
        }
      };

      $scope.processPatientMode = function () {
        if ($scope.searchExpression === 'pt ') {
          $scope.searchExpression = '';
        }
      };

      if (typeof $stateParams.ageFrom === 'undefined') {
        previousState = 'patients-list';
        previousPage = 'Patient Lists';
      }

      $scope.pageHeader = pageHeader;
      $scope.previousState = previousState;
      $scope.previousPage = previousPage;

      $scope.mainWidth = mainWidth;
      $scope.detailWidth = detailWidth;

      $scope.goBack = function () {
        history.back();
      };

      $scope.userContextViewExists = ('user-context' in $state.current.views);
      $scope.actionsExists = ('actions' in $state.current.views);

      $scope.go = function (patient) {
        $state.go('patients-summary', {
          patientId: patient.id
        });
      };

      if ($scope.currentUser.role === 'idcr') {
        $scope.title = 'IDCR POC';
      }
      if ($scope.currentUser.role === 'phr') {
        $scope.title = 'PHR';
      }

      $scope.goHome = function () {
        $scope.cancelSearchMode();
        if ($scope.currentUser.role === 'idcr') {
          $state.go('patients-charts');
        }
        if ($scope.currentUser.role === 'phr') {
          $state.go('patients-summary', {
            patientId: 10
          }); // Id is hardcoded
        }
      };
    });

  });
