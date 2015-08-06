'use strict';

angular.module('openehrPocApp')
  .controller('AppointmentsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Appointment) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Appointment.all($stateParams.patientId).then(function (result) {
      $scope.appointments = result.data;
    });

        $scope.go = function (index) {
      $location.path('/patients/'+$scope.patient.id+'/appointments/'+index);
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
          appointment.dateOfAppointment = new Date(appointment.dateOfAppointment);
          appointment.date = new Date(appointment.date);
          var time = new Date();
          time.setHours(appointment.timeSlot.slice(0,2));
          time.setMinutes(appointment.timeSlot.slice(3,4));
          var toAdd =  {
                  compositionId: '',
                  careServiceTeam: appointment.careServiceTeam,
                  dateOfAppointment:  appointment.dateOfAppointment,
                  location:  appointment.location,
                  status:  appointment.status,
                  author:  appointment.author,
                  dateCreated:  appointment.date,
                  source: "openehr",
                  timeSlot : time    
            }
        Appointment.create($scope.patient.id, toAdd).then(function () {
          $state.go('appointments', { patientId: $scope.patient.id });
        });
      });
    };
  });
