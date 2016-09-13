'use strict';

angular.module('gpConnect')
        .controller('OrdersReceivedCtrl', function ($scope, $http, $sce, $cacheFactory, $stateParams, PatientService, usSpinnerService, OrderService, Organization) {

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

                OrderService.findAllReceivedOrders($scope.patientFhirId).then(function (result) {
                    $scope.orders = result.data;

                    var orgCache = $cacheFactory.get('orgCache');
                    if (orgCache == undefined) {
                        orgCache = $cacheFactory('orgCache');
                    }

                    $.each($scope.orders, function (key, order) {
                        order.detail = $sce.trustAsHtml(order.detail);
                        order.sourceOrg = orgCache.get(order.sourceOrgId);

                        if (order.sourceOrg == undefined) {
                            order.sourceOrg = Organization.findOrganisation($stateParams.patientId, order.sourceOrgId).then(function (result) {
                                order.sourceOrg = result.data.name;
                                orgCache.put(order.sourceOrgId, order.sourceOrg);
                            });
                        }

                        order.targetOrg = orgCache.get(order.targetOrgId);

                        if (order.targetOrg == undefined) {
                            order.targetOrg = Organization.findOrganisation($stateParams.patientId, order.targetOrgId).then(function (result) {
                                order.targetOrg = result.data.name;
                                orgCache.put(order.targetOrgId, order.targetOrg);
                            });
                        }
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
