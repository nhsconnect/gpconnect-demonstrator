'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsCtrl
 * @description
 * # PatientsCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('PatientsListCtrl', function ($scope, $location, $stateParams, PatientService) {
    PatientService.all().then(function (patients) {
      $scope.patients = patients;
    });

    $scope.department = $stateParams.department;
    $scope.ageRange = $stateParams.ageRange;

    $scope.go = function (path) {
      $location.path(path);
    };
  });
