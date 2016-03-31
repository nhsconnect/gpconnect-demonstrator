'use strict';

angular.module('rippleDemonstrator')
  .controller('MainSearchController', function($scope, AdvancedSearch, UserService) {
    $scope.mainSearchEnabled = true;
    $scope.searchExpression = '';
    $scope.isClickToAdvancedSearch = UserService.getCurrentUser().feature.autoAdvancedSearch;
    $scope.openAdvancedSearch = AdvancedSearch.openAdvancedSearch;

    $scope.$emit('toggleHeaderSearchEnabled', false);

    $scope.hideSearch = function() {
      $scope.mainSearchEnabled = false;
      $scope.$emit('toggleHeaderSearchEnabled', true);
      $scope.$emit('populateHeaderSearch', $scope.searchExpression);
    };

    $scope.searchFunction = function() {
        if($scope.isClickToAdvancedSearch) {
          $scope.openAdvancedSearch();
        }
    };

  });

