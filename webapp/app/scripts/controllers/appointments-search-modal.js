'use strict';

angular.module('gpConnect')
  .controller('AppointmentsSearchModalCtrl', function ($scope, $modalInstance, $modal, $timeout, modal) {

    $scope.modal = modal;
    $scope.appointmentSearch = undefined;

    $scope.openDatePicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.setPreviousStartDate = function () {
      if ($scope.appointmentSearch && $scope.appointmentSearch.startDate) {
        var currentDate = $scope.appointmentSearch.startDate.getDate();
        $scope.appointmentSearch.startDate.setDate(currentDate - 1);
      }
    };

    $scope.setPreviousEndDate = function () {
      if ($scope.appointmentSearch && $scope.appointmentSearch.endDate) {
        var currentDate = $scope.appointmentSearch.endDate.getDate();
        $scope.appointmentSearch.endDate.setDate(currentDate - 1);
      }
    };

    $scope.setNextStartDate = function () {
      if ($scope.appointmentSearch && $scope.appointmentSearch.startDate) {
        var currentDate = $scope.appointmentSearch.startDate.getDate();
        $scope.appointmentSearch.startDate.setDate(currentDate + 1);
      }
    };

    $scope.setNextEndDate = function () {
      if ($scope.appointmentSearch && $scope.appointmentSearch.endDate) {
        var currentDate = $scope.appointmentSearch.endDate.getDate();
        $scope.appointmentSearch.endDate.setDate(currentDate + 1);
      }
    };

    $scope.ok = function (appointmentSearchForm) {
      $scope.formSubmitted = true;
      var appointmentSearch = $scope.appointmentSearch;

      if (appointmentSearchForm.$valid) {
        $modal.open({
          templateUrl: 'views/appointments/appointments-slots-modal.html',
          size: 'md',
          controller: 'AppointmentsSlotsCtrl',
          resolve: {
            modal: function () {
              return {
                title: 'Available Appointments'
              };
            },
            appointmentSearchParams: function () {
              return appointmentSearch;
            }
          }
        });
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

  });
