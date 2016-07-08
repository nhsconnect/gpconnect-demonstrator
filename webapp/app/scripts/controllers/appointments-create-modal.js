'use strict';

angular.module('gpConnect')
  .controller('AppointmentsCreateModalCtrl', function ($state, $scope, $stateParams, $modalInstance, PatientService, Appointment, modal, appointmentBookingParams, usSpinnerService) {

    PatientService.getPatientFhirId($stateParams.patientId).then(function (result) {
        $scope.patientFhirId = result;
    }).then(function(){
    
        $scope.modal = modal;
        $scope.practitionerName = appointmentBookingParams.practitionerFullName;

        $scope.appointmentCreate = {};
        $scope.appointmentCreate.resourceType = "Appointment";
        $scope.appointmentCreate.status = "booked";
        $scope.appointmentCreate.type = {
            "coding": [
                {
                    "system": "http://hl7.org/fhir/ValueSet/c80-practice-codes",
                    "code": appointmentBookingParams.typeCode,
                    "display": appointmentBookingParams.type
                }
            ],
            "text": appointmentBookingParams.type
        };
        $scope.appointmentCreate.reason = {
            "coding": [
                {
                    "system": "http://snomed.info/sct",
                    "code": "00001",
                    "display": "Default Appointment Type"
                }
            ],
            "text": "Generic Booking"
        };
        $scope.appointmentCreate.description = "";
        $scope.appointmentCreate.start = appointmentBookingParams.startTime;
        $scope.appointmentCreate.end = appointmentBookingParams.endTime;
        $scope.appointmentCreate.slot = [{ "reference": "Slot/"+appointmentBookingParams.slotId }];
        $scope.appointmentCreate.comment = "";
        $scope.appointmentCreate.participant = [
            {
                "actor": {
                    "reference": "Patient/"+$scope.patientFhirId
                },
                "status": "accepted"
            },
            {
                "actor": {
                    "reference": "Practitioner/"+appointmentBookingParams.practitionerId
                },
                "status": "accepted"
            },
            {
                "actor": {
                    "reference": "Location/"+appointmentBookingParams.locationId
                },
                "status": "accepted"
            }
        ];
    });

    $scope.ok = function (appointmentCreateForm) {
      $scope.formSubmitted = true;
      if (appointmentCreateForm.$valid) {
          usSpinnerService.spin('appointmentCreate-spinner');
          Appointment.create($scope.appointmentCreate).then(function(response){
              if(response.status != "201"){
                  alert("An error occurred storing appointment, please try booking again");
              }
              $modalInstance.close();
              $state.reload();
              usSpinnerService.stop('appointmentCreate-spinner');
          });
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

  });
