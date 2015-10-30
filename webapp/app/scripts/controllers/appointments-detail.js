'use strict';

angular.module('rippleDemonstrator')
  .controller('AppointmentsDetailCtrl', function ($scope, $stateParams, SearchInput, $modal, Helper, $state, $location, usSpinnerService, PatientService, Appointment) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Appointment.get($stateParams.patientId, $stateParams.appointmentIndex).then(function (result) {
      $scope.appointment = result.data;
      $scope.timeOfAppointment = moment($scope.appointment.timeOfAppointment).format('h:mma') + '-' + moment($scope.appointment.timeOfAppointment).add(59, 'm').format('h:mma');
      usSpinnerService.stop('appointmentsDetail-spinner');
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
          setTimeout(function () {
            $state.go('appointments-detail', {
              patientId: $scope.patient.id,
              appointmentIndex: Helper.updateId(appointment.sourceId)
            });
          }, 2000);
        });
      });
    };

  });
