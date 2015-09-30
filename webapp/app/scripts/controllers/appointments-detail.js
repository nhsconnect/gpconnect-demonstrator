'use strict';

angular.module('rippleDemonstrator')
  .controller('AppointmentsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Appointment) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Appointment.get($stateParams.patientId, $stateParams.appointmentIndex).then(function (result) {
      $scope.appointment = result.data;
      $scope.timeOfAppointment = moment($scope.appointment.timeOfAppointment).format('h:mma') + '-' + moment($scope.appointment.timeOfAppointment).add(59, 'm').format('h:mma');
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
          appointment: function () {
            return angular.copy($scope.appointment);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (appointment) {
        appointment.dateOfAppointment = new Date(appointment.dateOfAppointment);
        appointment.dateCreated = new Date(appointment.dateCreated);

        var toUpdate = {
          sourceId: appointment.sourceId,
          serviceTeam: appointment.serviceTeam,
          dateOfAppointment: appointment.dateOfAppointment,
          location: appointment.location,
          status: appointment.status,
          author: 'example@email.com',
          dateCreated: appointment.dateCreated,
          source: 'openehr',
          timeOfAppointment: appointment.timeOfAppointment
        };

        Appointment.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/appointments');
        });
      });
    };

  });
