'use strict';

angular.module('openehrPocApp')
  .controller('AppointmentsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Appointment) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Appointment.all($stateParams.referralId).then(function (result) {
      $scope.result = result.data;
      $scope.appointment = $scope.result.Appointment[$stateParams.appointmentIndex];
      $scope.appoinmentSelectionNumber = $stateParams.appointmentIndex + 1;
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/appointments/appointments-modal.html',
        size: 'lg',
        controller: 'AppointmentsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Appointment'
            };
          },
          contact: function () {
            return angular.copy($scope.appointment);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (appointment) {
        $scope.result.appointments[$stateParams.appointmentIndex] = appointment;

        Contact.update($scope.patient.id, $scope.result).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/appointments');
        });
      });
    };

  });
