'use strict';

angular.module('openehrPocApp')
    .controller('AppointmentsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Appointment) {

      $scope.search = function (row) {
        return (
                angular.lowercase(row.dateOfAppointment).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
                angular.lowercase(row.timeOfAppointment).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
                angular.lowercase(row.serviceTeam).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
                angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
        );
      };

      PatientService.get($stateParams.patientId).then(function (patient) {
        $scope.patient = patient;
      });

      Appointment.all($stateParams.patientId).then(function (result) {
        $scope.appointments = result.data;
        for (var i = 0; i < $scope.appointments.length; i++) {
          $scope.appointments[i].dateOfAppointment = moment($scope.appointments[i].dateOfAppointment).format('DD-MMM-YYYY');
          $scope.appointments[i].timeOfAppointment = moment($scope.appointments[i].timeOfAppointment).format('h:mma') + '-' + moment($scope.appointments[i].timeOfAppointment).add(59, 'm').format('h:mma');
        }
      });

      $scope.go = function (id) {
         $location.path('/patients/'+ $scope.patient.id+ '/appointments/' + id);
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
                      return {};
                    },
                    patient: function () {
                      return $scope.patient;
                    }
        }
      });

        modalInstance.result.then(function (appointment) {
          appointment.dateOfAppointment = new Date(appointment.dateOfAppointment);
          appointment.dateCreated = new Date(appointment.dateCreated);

          var toAdd = {
                    compositionId: '',
                    serviceTeam: appointment.serviceTeam,
                    dateOfAppointment: appointment.dateOfAppointment,
                    location: appointment.location,
                    status: appointment.status,
                    author: 'example@email.com',
                    dateCreated: appointment.dateCreated,
                    source: 'openehr',
                    timeOfAppointment: appointment.timeOfAppointment
        };
          Appointment.create($scope.patient.id, toAdd).then(function () {
            $state.go('appointments', {
                        patientId: $scope.patient.id
            });
          });
        });
      };
    });
