'use strict';

angular.module('openehrPocApp')
  .controller('headerController', function ($scope, $rootScope, $state) {

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {

      var previousState = "";
      var pageHeader = "";
      var previousPage = "";

      switch(toState.name) {
        case 'patients-list':
          previousState = 'patients-charts';
          pageHeader = 'Patient Lists';
          previousPage = 'Patient Dashboard';
          break;
        case 'patients-charts':
          previousState = '';
          pageHeader = 'Patient Dashboard';
          previousPage = ''
          break;
        case 'patients-lookup':
          previousState = '';
          pageHeader = '';
          previousPage = ''
          break;
        default:
          previousState = 'patients-list';
          pageHeader = 'Patients Details';
          previousPage = 'Patient Lists';
          break;
        }

      $scope.pageHeader = pageHeader;
      $scope.previousState = previousState;
      $scope.previousPage = previousPage;
      $scope.goBack = function() {
        $state.go(previousState);
      };

    });

  });


