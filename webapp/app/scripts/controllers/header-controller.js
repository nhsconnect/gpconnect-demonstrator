'use strict';

angular.module('openehrPocApp')
  .controller('headerController', function ($scope, $rootScope, $state, $stateParams) {

    var role = $stateParams.role;
    var email = $stateParams.email;

    $scope.email = email;
    $scope.role = role;

    switch(role) {
      case "idcr":
        $state.go('patients-charts');
        break;
      case "phr":
        $state.go('patients-summary');
        break;
      default:
        $state.go('patients-charts');
    }

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {

      var previousState = '';
      var pageHeader = '';
      var previousPage = '';

      var mainWidth = 0;
      var detailWidth = 0;

      switch(toState.name) {
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
          previousState = '';
          pageHeader = 'Patients Summary';
          previousPage = '';
          mainWidth = 12;
          detailWidth = 0;
          break;
        case 'patients-lookup':
          previousState = '';
          pageHeader = '';
          previousPage = '';
          mainWidth = 6;
          detailWidth = 6;
          break;
        default:
          previousState = 'patients-list';
          pageHeader = 'Patients Details';
          previousPage = 'Patient Lists';
          mainWidth = 6;
          detailWidth = 6;
          break;
        }

      $scope.pageHeader = pageHeader;
      $scope.previousState = previousState;
      $scope.previousPage = previousPage;

      // Set bootstrap column sizes
      $scope.mainWidth = mainWidth;
      $scope.detailWidth = detailWidth;

      $scope.goBack = function() {
        $state.go(previousState);
      };

      if("user-context" in $state.current.views){
        $scope.userContextViewExists = true;
      }else{
        $scope.userContextViewExists = false;
      }

      if('actions' in $state.current.views){
        $scope.actionsExists = true;
      }else{
        $scope.actionsExists = false;
      }

    });

  });


