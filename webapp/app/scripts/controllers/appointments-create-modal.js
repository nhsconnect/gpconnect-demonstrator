'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCreateModalCtrl', function ($state, $scope, $stateParams, $modalInstance, modal, appointmentBookingParams, Appointment, usSpinnerService, gpcResource) {


            $.each(appointmentBookingParams.patient.identifier, function (key, identifier) {
            if (identifier.system == gpcResource.getConst("ID_NHS_NUMBER")) { $scope.patientNhsNumber = identifier.value; }

            });
            $scope.patient = appointmentBookingParams.patient;
            $scope.modal = modal;
            $scope.practitionerName = appointmentBookingParams.practitionerFullName;
            $scope.practiceName = appointmentBookingParams.location.practiceName;
            $scope.locationName = appointmentBookingParams.location.name;

            $scope.appointmentCreate = {};
            $scope.appointmentCreate.resourceType = "Appointment";
            $scope.appointmentCreate.status = "booked";
            $scope.appointmentCreate.type = {
                "coding": [
                    {
                        "system": gpcResource.getConst("VS_C80_PRACTICE_CODES"),
                        "code": appointmentBookingParams.typeCode,
                        "display": appointmentBookingParams.type
                    }
                ],
                "text": appointmentBookingParams.type
            };
            $scope.appointmentCreate.reason = {
                "coding": [
                    {
                        "system": gpcResource.getConst("VS_SNOMED_SCT"),
                        "code": "00001",
                        "display": "Default Appointment Type"
                    }
                ],
                "text": "Generic Booking"
            };
           
            $scope.appointmentCreate.start = appointmentBookingParams.startTime;
            $scope.appointmentCreate.end;
            
            $scope.appointmentCreate.slot = [];
            for(var slotIndex = 0; slotIndex < appointmentBookingParams.slotIds.length; slotIndex++){
                var reference = {"reference": "Slot/" + appointmentBookingParams.slotIds[slotIndex]};
                $scope.appointmentCreate.slot.push(reference);
            }
            
            $scope.appointmentCreate.comment;
            $scope.appointmentCreate.participant = [
                {
                    "actor": {
                        "reference": "Patient/" + appointmentBookingParams.patientFhirId
                    },
                    "status": "accepted"
                },
                {
                    "actor": {
                        "reference": "Practitioner/" + appointmentBookingParams.practitionerId
                    },
                    "status": "accepted"
                },
                {
                    "actor": {
                        "reference": "Location/" + appointmentBookingParams.locationId
                    },
                    "status": "accepted"
                }
            ];

            $scope.ok = function (appointmentCreateForm) {
                $scope.validationError = "";
                $scope.formSubmitted = true;
                if (appointmentCreateForm.$valid) {
                    usSpinnerService.spin('appointmentCreate-spinner');
                    Appointment.create(appointmentBookingParams.location.odsCode, $stateParams.patientId, $scope.appointmentCreate).then(function (response) {
                        if (response.status != "201") {
                            $scope.validationError = "An error occurred storing appointment, please try booking again";
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
