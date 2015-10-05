'use strict';

angular.module('rippleDemonstrator')
  .controller('OrdersDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Order) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Order.get($stateParams.patientId, $stateParams.orderId).then(function (result) {
      $scope.order = result.data;
    });

  });
