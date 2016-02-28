'use strict';

angular.module('rippleDemonstrator')
  .controller('OrdersModalCtrl', function ($scope, $modalInstance, Order, UserService, order, patient, modal) {

    $scope.currentUser = UserService.getCurrentUser();
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

    $scope.ok = function (orderForm) {
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

    $scope.toggleSelectedItem = function (idSelected) {
      if ($scope.isInSuggestionsList(idSelected)) {
        $scope.setSelectedLeft(idSelected);
        for (var i = 0; i < $scope.suggestions.length; i++) {
          if ($scope.suggestions[i].code === $scope.idSelectedLeft) {
            $scope.chosenOrders.push($scope.suggestions[i]);
            $scope.suggestions.splice(i, 1);
          }
        }
      } else {
        $scope.setSelectedRight(idSelected);
        for (var a = 0; a < $scope.chosenOrders.length; a++) {
          if ($scope.chosenOrders[a].code === $scope.idSelectedRight) {
            $scope.suggestions.push($scope.chosenOrders[a]);
            $scope.chosenOrders.splice(a, 1);
          }
        }
      }
      if ($scope.chosenOrders.length === 0) {
        $scope.firstPage = true;
      }
    };

    $scope.isInSuggestionsList = function (idSelected) {
      for (var b = 0; b < $scope.suggestions.length; b++) {
        if ($scope.suggestions[b].code === idSelected) {
          return true;
        }
      }
      return false;
    };

    $scope.moveItem = function () {
      if ($scope.idSelectedLeft === null) {
        for (var c = 0; c < $scope.chosenOrders.length; c++) {
          if ($scope.chosenOrders[c].code === $scope.idSelectedRight) {
            $scope.suggestions.push($scope.chosenOrders[c]);
            $scope.chosenOrders.splice(c, 1);
          }
        }
      } else {
        for (var d = 0; d < $scope.suggestions.length; d++) {
          if ($scope.suggestions[d].code === $scope.idSelectedLeft) {
            $scope.chosenOrders.push($scope.suggestions[d]);
            $scope.suggestions.splice(d, 1);
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
