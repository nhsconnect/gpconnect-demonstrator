'use strict';

angular.module('openehrPocApp')
  .controller('OrdersModalCtrl', function ($scope, $modalInstance, order, patient, modal, Order, PatientService) {

    $scope.currentUser = PatientService.getCurrentUser();
    $scope.order = order;
    $scope.patient = patient;
    $scope.modal = modal;
    $scope.order.author =  'Dr John Smith';

    if (modal.title === 'Create Order'){
      $scope.order.date = new Date().toISOString().slice(0, 10);
    }

    Order.suggestion().then(function (suggestions) {
      $scope.suggestions = suggestions.data;
    });

    $scope.ok = function (orderForm, order) {
      $scope.formSubmitted = true;
      if (orderForm.$valid) {
        $modalInstance.close(order);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope[name] = true;
    };

  });
