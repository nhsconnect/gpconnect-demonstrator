'use strict';

angular.module('openehrPocApp')
  .controller('OrdersModalCtrl', function ($scope, $modalInstance, order, patient, modal, Order) {

    $scope.order = order;
    $scope.patient = patient;
    $scope.modal = modal;
    
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
