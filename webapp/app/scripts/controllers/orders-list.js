'use strict';

angular.module('openehrPocApp')
  .controller('OrdersListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Order) {
    
      $scope.search = function (row) {
        return (
          angular.lowercase(row.name).indexOf(angular.lowercase($scope.query) || '') !== -1 
       || angular.lowercase(row.date).indexOf(angular.lowercase($scope.query) || '') !== -1
       || angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
        );
    };
    
    
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Order.all($stateParams.patientId).then(function (result) {
      $scope.orders = result.data;
        for(var i = 0; i < $scope.orders.length; i++){
          $scope.orders[i].date = moment($scope.orders[i].date).format('DD-MMM-YYYY');
      } 
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (orderIndex) {
      return orderIndex === $stateParams.orderIndex;
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
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (order) {
        order.compositionId = '';
        order.date = new Date(order.date);
          
          
        Order.create($scope.patient.id, order).then(function () {
          $state.go('orders', { patientId: $scope.patient.id });
        });
      });
    };


  });
