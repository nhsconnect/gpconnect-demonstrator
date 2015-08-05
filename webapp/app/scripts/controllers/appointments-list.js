'use strict';

angular.module('openehrPocApp')
  .controller('AppointmentsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Appointment) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Appointment.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.appontments = $scope.result.appointments;
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (appointmentIndex) {
      return appointmentIndex === $stateParams.appointmentIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/appointments/appointments-modal.html',
        size: 'lg',
        controller: 'AppointmentsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Appointment'
            };
          },
          appointment: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (appointment) {

          var toAdd =  {
                  compositionId: '',
                  careServiceTeam: appointment.careServiceTeam,
                  dateOfAppointment:  appointment.dateOfAppointment,
                  timeSlot:  appointment.timeSlot,
                  location:  appointment.location,
                  status:  appointment.status,
                  author:  appointment.author,
                  dateCreated:  appointment.date,
                  source: "openehr"
            }
        Appointment.create($scope.patient.id, toAdd).then(function () {
          $state.go('appointments', { patientId: $scope.patient.id });
        });
      });
    };
  });
