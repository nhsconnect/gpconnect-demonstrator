'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCreateModalCtrl', function ($state, $scope, $stateParams, $modalInstance, modal, appointmentBookingParams, Appointment, usSpinnerService, gpcResource) {


            $.each(appointmentBookingParams.patient.identifier, function (key, identifier) {
                if (identifier.system == gpcResource.getConst("ID_NHS_NUMBER")) {
                    $scope.patientNhsNumber = identifier.value;
                }

            });
            $scope.patient = appointmentBookingParams.patient;
            $scope.modal = modal;
            $scope.practitionerName = appointmentBookingParams.practitionerFullName;
            $scope.practitionerRole = appointmentBookingParams.practitionerRole;
            $scope.deliveryChannel = appointmentBookingParams.deliveryChannel;
            $scope.practiceName = appointmentBookingParams.location.practiceName;
            $scope.locationAddress = appointmentBookingParams.location.address;

            $scope.appointmentCreate = {};
            $scope.appointmentCreate.meta = {profile: ["https://fhir.nhs.uk/STU3/StructureDefinition/GPConnect-Appointment-1"]};

            // contained orgaization
            $scope.appointmentCreate.contained = [
                {
                    "resourceType": "Organization",
                    "id": "1",
                    "meta": {
                        "profile": [
                            gpcResource.getConst("SD_CC_ORGANISATION")
                        ]
                    },
                    "identifier": [
                        {
                            "system": "https://fhir.nhs.uk/Id/ods-organization-code",
                            "value": "A20047"
                        }
                    ],
                    "name": "Test Organization Name",
                    "telecom": [
                        {
                            "system": "phone",
                            "value": "0300 303 5678"
                        }
                    ]
                }
            ];

            $scope.appointmentCreate.extension = [
                    {
                        "url": gpcResource.getConst("SD_EXT_ORGANISATION"),
                        "valueReference": {
                            "reference": "#1"
                        }
                    }
            ];

            $scope.appointmentCreate.resourceType = "Appointment";
            $scope.appointmentCreate.status = "booked";
            $scope.appointmentCreate.created = new Date();

            $scope.appointmentCreate.start = appointmentBookingParams.startTime;
            $scope.appointmentCreate.end = appointmentBookingParams.endTime;

            $scope.appointmentCreate.slot = [];
            for (var slotIndex = 0; slotIndex < appointmentBookingParams.slotIds.length; slotIndex++) {
                var reference = {"reference": "Slot/" + appointmentBookingParams.slotIds[slotIndex]};
                $scope.appointmentCreate.slot.push(reference);
            }

            $scope.appointmentCreate.description;
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
                } else {
                    $scope.validationError = "A description is required.";
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        });
