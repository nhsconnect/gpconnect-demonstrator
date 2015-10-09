'use strict';

angular.module('rippleDemonstrator')
  .controller('OrdersModalCtrl', function ($scope, $modalInstance, Order, PatientService, order, patient, modal) {

    $scope.currentUser = PatientService.getCurrentUser();
    $scope.order = order;
    $scope.patient = patient;
    $scope.modal = modal;
    $scope.order.author = 'Dr John Smith';
    $scope.firstPage = true;
    $scope.chosenOrders = [];

    if (modal.title === 'Create Orders') {
      $scope.order.orderDate = new Date().toISOString().slice(0, 10);
    }

    Order.suggestion().then(function (suggestions) {
      $scope.suggestions = suggestions.data;
    });

    $scope.ok = function (orderForm, order) {
      $scope.formSubmitted = true;

      if (orderForm.$valid) {
        $modalInstance.close($scope.chosenOrders);
      }
    };

    $scope.idSelectedLeft = null;
    $scope.idSelectedRight = null;

    $scope.setSelectedLeft = function (idSelectedLeft) {
      $scope.idSelectedRight = null;
      $scope.idSelectedLeft = idSelectedLeft;
    };

    $scope.setSelectedRight = function (idSelectedRight) {
      $scope.idSelectedLeft = null;
      $scope.idSelectedRight = idSelectedRight;
    };

    $scope.moveToRight = function (idSelectedLeft) {
      $scope.setSelectedLeft(idSelectedLeft);
      for (var i = 0; i < $scope.suggestions.length; i++) {
        if ($scope.suggestions[i].code === $scope.idSelectedLeft) {
          $scope.chosenOrders.push($scope.suggestions[i]);
          $scope.suggestions.splice(i, 1);
        }
      }
    };

    $scope.moveToLeft = function (idSelectedRight) {
      $scope.setSelectedRight(idSelectedRight);
      for (var i = 0; i < $scope.chosenOrders.length; i++) {
        if ($scope.chosenOrders[i].code === $scope.idSelectedRight) {
          $scope.suggestions.push($scope.chosenOrders[i]);
          $scope.chosenOrders.splice(i, 1);
        }
      }
      if ($scope.chosenOrders.length === 0){
        $scope.firstPage = true;
      }
    };

    $scope.moveItem = function () {
      if ($scope.idSelectedLeft == null) {
        for (var i = 0; i < $scope.chosenOrders.length; i++) {
          if ($scope.chosenOrders[i].code === $scope.idSelectedRight) {
            $scope.suggestions.push($scope.chosenOrders[i]);
            $scope.chosenOrders.splice(i, 1)
          }
        }
      } else {
        for (var i = 0; i < $scope.suggestions.length; i++) {
          if ($scope.suggestions[i].code === $scope.idSelectedLeft) {
            $scope.chosenOrders.push($scope.suggestions[i]);
            $scope.suggestions.splice(i, 1)
          }
        }
      }
    };

    $scope.pageTwo = function () {
      $scope.firstPage = false;
    };

    $scope.pageOne = function () {
      $scope.firstPage = true;
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
