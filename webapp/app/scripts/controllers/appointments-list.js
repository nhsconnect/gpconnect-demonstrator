'use strict';

angular.module('gpConnect')
  .controller('AppointmentsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, usSpinnerService, Appointment) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.dateOfAppointment).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.timeOfAppointment).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.serviceTeam).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Appointment.findAllSummaries($stateParams.patientId).then(function (result) {
      $scope.appointments = result.data;

      for (var i = 0; i < $scope.appointments.length; i++) {
        $scope.appointments[i].dateOfAppointment = moment($scope.appointments[i].dateOfAppointment).format('DD-MMM-YYYY');
        $scope.appointments[i].timeOfAppointment = moment($scope.appointments[i].timeOfAppointment).format('h:mma') + '-' + moment($scope.appointments[i].timeOfAppointment).add(59, 'm').format('h:mma');
      }

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('appointments-detail', {
        patientId: $scope.patient.id,
        appointmentIndex: id,
        filter: $scope.query,
        page: $scope.currentPage
      });
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
          serviceTeam: appointment.serviceTeam,
          dateOfAppointment: appointment.dateOfAppointment,
          location: appointment.location,
          status: appointment.status,
          author: 'example@email.com',
          dateCreated: appointment.dateCreated,
          timeOfAppointment: appointment.timeOfAppointment
        };

        Appointment.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('appointments', {
              patientId: $scope.patient.id,
              filter: $scope.query,
              page: $scope.currentPage
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
