'use strict';

angular.module('gpConnect')
        .controller('OrdersCtrl', function ($scope, $http, $sce, $stateParams, $state, $modal, PatientService, usSpinnerService, Order) {

            $scope.currentPage = 1;

            $scope.pageChangeHandler = function (newPage) {
                $scope.currentPage = newPage;
            };

            if ($stateParams.page) {
                $scope.currentPage = $stateParams.page;
            }

            if ($stateParams.filter) {
                $scope.query = $stateParams.filter;
            }

            PatientService.getPatientFhirId($stateParams.patientId).then(function (result) {
                return result;
            }).then(function (result) {

                $scope.patientFhirId = result;

                Order.findAllOrders($scope.patientFhirId).then(function (result) {
                    console.log(result.data);
                    $scope.orders = result.data;
                    $.each($scope.orders, function (key, order) {
                        order.detail = $sce.trustAsHtml(order.detail);
                    });
                }).catch(function (e) {
                    usSpinnerService.stop('patientSummary-spinner');
                }).finally(function () {
                    usSpinnerService.stop('patientSummary-spinner');
                });
            });

            $scope.go = function (id) {

                usSpinnerService.spin('patientSummary-spinner');
                $scope.orderDetail = undefined;
                
                var order;
                for (var index = 0; index < $scope.orders.length; ++index) {
                    order = $scope.orders[index];
                    if (order.id == id) {
                        $scope.orderDetail = order;
                        break;
                    }
                }

                usSpinnerService.stop('patientSummary-spinner');
            };

            $scope.selected = function (orderIndex) {
                return orderIndex === $stateParams.orderIndex;
            };

        });
