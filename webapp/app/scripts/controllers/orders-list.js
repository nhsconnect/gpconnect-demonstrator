'use strict';

angular.module('rippleDemonstrator')
  .controller('OrdersListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Order) {

    $scope.search = function (row) {
      return (
        angular.lowercase(row.name).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.orderDate).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Order.all($stateParams.patientId).then(function (result) {
      $scope.orders = result.data;

      for (var i = 0; i < $scope.orders.length; i++) {
        $scope.orders[i].orderDate = moment($scope.orders[i].orderDate).format('DD-MMM-YYYY');
      }
    });

    $scope.go = function (id) {
      $location.path('/patients/' + $scope.patient.id + '/orders/' + id);
    };

    $scope.selected = function (orderId) {
      return orderId === $stateParams.orderId;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/orders/orders-modal.html',
        size: 'lg',
        controller: 'OrdersModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Order'
            };
          },
          order: function () {
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (orders, author) {
        var toAdd = [];
        for(var i = 0; i < orders.length; i++){
          var newItem = {};
          newItem.sourceId = '';
          newItem.dateCreated = new Date();
          newItem.author = 'Dr John Smith';
          newItem.code = orders[i].code;
          newItem.name = orders[i].text;
          newItem.source = 'openehr';
          toAdd.push(newItem);
        }

        Order.create($scope.patient.id, toAdd).then(function () {
          $state.go('orders', {patientId: $scope.patient.id});
        });
      });
    };

  });
