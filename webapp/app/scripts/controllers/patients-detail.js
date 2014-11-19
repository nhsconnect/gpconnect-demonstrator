'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsDetailCtrl
 * @description
 * # PatientsDetailCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('PatientsDetailCtrl', function ($scope, $stateParams, $location, Patient) {
    $scope.patient = Patient.get($stateParams.patientId);
  });
