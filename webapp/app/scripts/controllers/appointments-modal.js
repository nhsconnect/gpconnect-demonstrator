'use strict';

angular.module('gpConnect')
  .controller('AppointmentsModalCtrl', function ($scope, $modalInstance, $modal, modal) {

    $scope.modal = modal;

    $scope.openDatePicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

    $scope.nextDate = function () {

    };

    $scope.previousDate = function () {

    };

    $scope.ok = function (startDate, endDate) {
      $scope.formSubmitted = true;

      if (startDate.$valid && endDate.$valid) {
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
              return $scope.appointmentSearch;
            }
          }
        });
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

  });
