'use strict';

angular.module('openehrPocApp')
  .controller('OrdersDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Order) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Order.get($stateParams.patientId, $stateParams.orderId).then(function (result) {
      $scope.order = result.data;
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/orders/orders-modal.html',
        size: 'lg',
        controller: 'OrdersModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Order'
            };
          },
          order: function () {
            return angular.copy($scope.order);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (order) {
        Order.update($scope.patient.id, order).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/orders');
        });
      });
    };

  });
