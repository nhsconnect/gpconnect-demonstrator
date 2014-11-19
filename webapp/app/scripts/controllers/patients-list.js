'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsCtrl
 * @description
 * # PatientsCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('PatientsListCtrl', function ($scope, $location, Patient) {
    $scope.patients = Patient.all();

    $scope.go = function (path) {
      $location.path(path);
    };
  });
