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

    Patient.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

  });
