'use strict';

angular.module('openehrPocApp')
  .controller('AppointmentsModalCtrl', function ($scope, $modalInstance, appointment, patient, modal, PatientService) {
    
    
    $scope.currentUser = PatientService.getCurrentUser();
    $scope.appointment = appointment;
    $scope.patient = patient;
    $scope.modal = modal; 
    $scope.radioModel = 'Tab1';
    $scope.appointment.location = appointment.location || 'Leeds General';
    $scope.appointment.status = appointment.status || 'Scheduled';
    
    if(modal.title === 'Create Appointment'){$scope.appointment.date = new Date().toISOString().slice(0, 10);}
    
    $scope.openAppointmentDatepicker = function ($event, name) {
    $event.preventDefault();
    $event.stopPropagation();
        
      $scope[name] = true;
    }; 

    $scope.openDatepicker = function ($event, name) {
    $event.preventDefault();
    $event.stopPropagation();
        
      $scope[name] = true;
    };
    
    $scope.imgClick = function () {
        var a = $('#scheduler').attr('src');
        
        if($('#scheduler').attr('src') === 'images/Before.png'){
            $("#scheduler").attr("src","images/After.png");
        }else{
           $scope.appointment.timeSlot = '12:00pm';
           $scope.appointment.dateOfAppointment = new Date().toISOString().slice(0, 10);;
           $scope.radioModel = 'Tab1';
        }
    }
    
    $scope.schedule = function () {
        $scope.radioModel = 'Tab2';
    }

    $scope.ok = function (appointmentForm, appointment) {
      $scope.formSubmitted = true;
      if (appointmentForm.$valid) {
        $modalInstance.close(appointment);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
    };

  });
