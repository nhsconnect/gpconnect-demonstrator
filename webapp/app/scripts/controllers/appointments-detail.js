'use strict';

angular.module('openehrPocApp')
  .controller('AppointmentsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Appointment) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Appointment.all($stateParams.appointmentIndex).then(function (result) {
      $scope.appointment = result.data[$stateParams.appointmentIndex];
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
          appointment.date = new Date(appointment.date);
  
          var toUpdate =  {
                  compositionId: $scope.appointment.compositionId,
                  careServiceTeam: appointment.careServiceTeam,
                  dateOfAppointment:  appointment.dateOfAppointment,
                  location:  appointment.location,
                  status:  appointment.status,
                  author:  appointment.author,
                  dateCreated:  appointment.date,
                  source: "openehr",
                  timeSlot : appointment.timeSlot    
            }
        Appointment.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/appointments');
        });
      });
    };
  });
