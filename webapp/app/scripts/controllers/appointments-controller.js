'use strict';

angular.module('gpConnect')
  .controller('AppointmentsCtrl', function ($scope, $http, $stateParams, $state, PatientService, usSpinnerService, Appointment) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Appointment.findAllAppointments($stateParams.patientId).then(function (result) {
      var appointmentsJson = result.data;
      $scope.appointments = appointmentsJson.entry;
      
      if($scope.appointments != undefined){
        $scope.appointments = $scope.appointments.sort(function (a, b) {
          return a.resource.start.localeCompare( b.resource.start );
        });

        $.each($scope.appointments, function(key, value){
            var startDate = Date.parse(value.resource.start.toString());
            value.resource.start = moment(startDate).format('DD-MMM-YYYY HH:mm');
            var endDate = Date.parse(value.resource.end.toString());
            value.resource.end = moment(endDate).format('DD-MMM-YYYY HH:mm');
        });
      }
    }).catch(function (e) {
      usSpinnerService.stop('patientSummary-spinner');
    }) .finally(function () {
      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
        
        usSpinnerService.spin('patientSummary-spinner');
        $scope.appointmentDetail = undefined;
        
        var appointment;
        for (var index = 0; index < $scope.appointments.length; ++index) {
            appointment = $scope.appointments[index];
            if (appointment.resource.id == id) {
                $scope.appointmentDetail = appointment;
                break;
            }
        }
        
        $.each($scope.appointmentDetail.resource.participant, function(key, value){
          var reference = value.actor.reference.toString();
          if(reference.indexOf("Location") != -1){
              Appointment.findResourceByReference(reference).then(function(response) {
                  $scope.appointmentDetail.appointmentLocation = response.data.name;
              });
          } else if(reference.indexOf("Practitioner") != -1){
              Appointment.findResourceByReference(reference).then(function(response) {
                  $scope.appointmentDetail.practitionerName = response.data.name.prefix[0] + " " + response.data.name.given[0] + " " + response.data.name.family[0];
              });
          }
          
        });
        
        usSpinnerService.stop('patientSummary-spinner');
    };

    $scope.selected = function (appointmentIndex) {
      return appointmentIndex === $stateParams.appointmentIndex;
    };

  });
