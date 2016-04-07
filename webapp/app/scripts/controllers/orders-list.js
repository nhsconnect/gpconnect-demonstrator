'use strict';

angular.module('rippleDemonstrator')
  .controller('OrdersListCtrl', function ($scope, $location, $stateParams, SearchInput, usSpinnerService, $modal, $state, PatientService, Order) {

    SearchInput.update();

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.name).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.orderDate).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Order.all($stateParams.patientId).then(function (result) {
      if (result.status !== 204) {
        $scope.orders = result.data.reverse();
        for (var i = 0; i < $scope.orders.length; i++) {
          $scope.orders[i].orderDate = moment($scope.orders[i].orderDate).format('DD-MMM-YYYY h:mm a');
        }
      }
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id, source) {
      $state.go('orders-detail', {
        patientId: $scope.patient.id,
        orderId: id,
        filter: $scope.query,
        page: $scope.currentPage,
        reportType: $stateParams.reportType,
        searchString: $stateParams.searchString,
        queryType: $stateParams.queryType,
        source: source
      });
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
              title: 'Create Orders'
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

      modalInstance.result.then(function (orders) {
        var toAdd = [];
        for (var i = 0; i < orders.length; i++) {
          var newItem = {};
          newItem.sourceId = '';
          newItem.dateCreated = new Date();
          newItem.author = 'Dr John Smith';
          newItem.code = orders[i].code;
          newItem.name = orders[i].text;
          toAdd.push(newItem);
        }

        Order.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('orders', {
              patientId: $scope.patient.id,
              filter: $scope.query,
              page: $scope.currentPage,
              reportType: $stateParams.reportType,
              searchString: $stateParams.searchString,
              queryType: $stateParams.queryType
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
