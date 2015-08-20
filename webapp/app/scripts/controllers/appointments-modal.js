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
    $scope.uiConfig = {
      calendar:{
        height: 450,
        width: 400,  
        editable: true,
        aspectRatio: 1.7,
		defaultDate: '2015-02-12',
        Duration: '01:00:00',  
		lang: 'en-gb',
		eventColor: '#378006',  
          events: [
				{ title : 'Time Slot 1', start: '2015-02-09 09:00', end: '2015-02-09 09:59'},
				{ title : 'Time Slot 2', start: '2015-02-09 10:00', end: '2015-02-09 10:59'},
				{ title : 'Time Slot 3', start: '2015-02-09 11:00', end: '2015-02-09 11:59'},
				{ title : 'Time Slot 4', start: '2015-02-09 12:00', end: '2015-02-09 12:59'},
				{ title : 'Time Slot 5', start: '2015-02-09 13:00', end: '2015-02-09 13:59'},
				{ title : 'Time Slot 6', start: '2015-02-09 14:00', end: '2015-02-09 14:59'},
				{ title : 'Time Slot 7', start: '2015-02-09 15:00', end: '2015-02-09 15:59'},
				{ title : 'Time Slot 8', start: '2015-02-09 16:00', end: '2015-02-09 16:59'},
				
				{ title : 'Time Slot 1', start: '2015-02-10 09:00', end: '2015-02-10 09:59'},
				{ title : 'Time Slot 2', start: '2015-02-10 10:00', end: '2015-02-10 10:59'},
				{ title : 'Time Slot 3', start: '2015-02-10 11:00', end: '2015-02-10 11:59'},
				{ title : 'Time Slot 4', start: '2015-02-10 12:00', end: '2015-02-10 12:59'},
				{ title : 'Time Slot 5', start: '2015-02-10 13:00', end: '2015-02-10 13:59'},
				{ title : 'Time Slot 6', start: '2015-02-10 14:00', end: '2015-02-10 14:59'},
				{ title : 'Time Slot 7', start: '2015-02-10 15:00', end: '2015-02-10 15:59'},
				{ title : 'Time Slot 8', start: '2015-02-10 16:00', end: '2015-02-10 16:59'},
				
				{ title : 'Time Slot 1', start: '2015-02-11 09:00', end: '2015-02-11 09:59'},
				{ title : 'Time Slot 2', start: '2015-02-11 10:00', end: '2015-02-11 10:59'},
				{ title : 'Time Slot 3', start: '2015-02-11 11:00', end: '2015-02-11 11:59'},
				{ title : 'Time Slot 4', start: '2015-02-11 12:00', end: '2015-02-11 12:59'},
				{ title : 'Time Slot 5', start: '2015-02-11 13:00', end: '2015-02-11 13:59'},
				{ title : 'Time Slot 6', start: '2015-02-11 14:00', end: '2015-02-11 14:59'},
				{ title : 'Time Slot 7', start: '2015-02-11 15:00', end: '2015-02-11 15:59'},
				{ title : 'Time Slot 8', start: '2015-02-11 16:00', end: '2015-02-11 16:59'},
				
				{ title : 'Time Slot 1', start: '2015-02-12 09:00', end: '2015-02-12 09:59'},
				{ title : 'Time Slot 2', start: '2015-02-12 10:00', end: '2015-02-12 10:59'},
				{ title : 'Time Slot 3', start: '2015-02-12 11:00', end: '2015-02-12 11:59'},
				{ title : 'Time Slot 4', start: '2015-02-12 12:00', end: '2015-02-12 12:59'},
				{ title : 'Time Slot 5', start: '2015-02-12 13:00', end: '2015-02-12 13:59'},
				{ title : 'Time Slot 6', start: '2015-02-12 14:00', end: '2015-02-12 14:59'},
				{ title : 'Time Slot 7', start: '2015-02-12 15:00', end: '2015-02-12 15:59'},
				{ title : 'Time Slot 8', start: '2015-02-12 16:00', end: '2015-02-12 16:59'},
				
				{ title : 'Time Slot 1', start: '2015-02-13 09:00', end: '2015-02-13 09:59'},
				{ title : 'Time Slot 2', start: '2015-02-13 10:00', end: '2015-02-13 10:59'},
				{ title : 'Time Slot 3', start: '2015-02-13 11:00', end: '2015-02-13 11:59'},
				{ title : 'Time Slot 4', start: '2015-02-13 12:00', end: '2015-02-13 12:59'},
				{ title : 'Time Slot 5', start: '2015-02-13 13:00', end: '2015-02-13 13:59', color: '#dd2b08'},
				{ title : 'Time Slot 6', start: '2015-02-13 14:00', end: '2015-02-13 14:59'},
				{ title : 'Time Slot 7', start: '2015-02-13 15:00', end: '2015-02-13 15:59'},
				{ title : 'Time Slot 8', start: '2015-02-13 16:00', end: '2015-02-13 16:59'},
			],
        header:{
          right: 'month,agendaWeek,agendaDay',
          center: 'title',
          left: 'prev,next'
        },
        dayClick: $scope.alertEventOnClick,
        eventClick: function(calEvent, jsEvent, view) {
            $(this).css('backgroundColor', '#6599C8');
            $scope.setTimeSlot(calEvent.start)
        },
        eventDrop: $scope.alertOnDrop,
        eventResize: $scope.alertOnResize,
        defaultView: 'agendaWeek',
		allDaySlot: false,
		minTime: '9:00',
		maxTime: '17:00',
		weekends: false
      }
    };
    
    $scope.eventSources = [];
    
    if(modal.title === 'Create Appointment'){$scope.appointment.dateCreated = new Date().toISOString().slice(0, 10);}else{
           $scope.appointment.timeSlot = new Date($scope.appointment.timeSlot);
           setBookedSlot();   
    }
    
    function setBookedSlot(){
        var booking = $scope.appointment.dateOfAppointment.slice(0, 10)+' '+$scope.appointment.timeSlot.toISOString().slice(11, 13)+':00';
        for(var i = 0; i < $scope.uiConfig.calendar.events.length; i++){
            var event = $scope.uiConfig.calendar.events[i];
            if(event.color === '#6599C8'){
                event.color = '#378006';
            }
            if(event.start === booking){
                event.color = '#6599C8';
            }
        }
    }
    
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
    
    $scope.setTimeSlot = function (time) {
           $scope.appointment.timeSlot = time;
           $scope.appointment.dateOfAppointment = time.toISOString().slice(0, 10);
           setBookedSlot();  
           $scope.radioModel = 'Tab1';
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
