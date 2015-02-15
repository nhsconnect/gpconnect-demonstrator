'use strict';

angular.module('openehrPocApp')
  .controller('AllergiesDetailCtrl', function ($scope, $stateParams, Allergy) {

    Allergy.get($stateParams.allergyId).then(function (allergy) {
      $scope.allergy = allergy;
    });

  });
