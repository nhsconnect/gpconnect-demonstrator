'use strict';

angular.module('rippleDemonstrator')
  .controller('OrdersDetailCtrl', function ($scope, $stateParams, SearchInput, $modal, $location, usSpinnerService, PatientService, Order) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Order.get($stateParams.patientId, $stateParams.orderId, $stateParams.source).then(function (result) {
      $scope.order = result.data;
      usSpinnerService.stop('ordersDetail-spinner');
    });

  });
