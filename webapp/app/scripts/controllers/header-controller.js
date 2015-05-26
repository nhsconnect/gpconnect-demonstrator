'use strict';

angular.module('openehrPocApp')
  .controller('headerController', function ($scope, $rootScope) {

    $scope.goBack = function() {
      window.history.back();
    };

    $rootScope.$on('$stateChangeSuccess', function (event, toState, toParams, fromState, fromParams) {

      if(toState.pageHeader == "Patient Dashboard"){
        $scope.previousPage="";
      }else if(angular.isUndefined(fromState.pageHeader)){
        $scope.previousPage = "Patient Dashboard";
      }
      else
      {
        $scope.previousPage = fromState.pageHeader;
      }

    });

  });

