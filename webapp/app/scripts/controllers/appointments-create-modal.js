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
                    "name": "Dr Legg's Surgery",
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

            $scope.appointmentCreate.start = formatDate(appointmentBookingParams.startTime);
            $scope.appointmentCreate.end = formatDate(appointmentBookingParams.endTime);
            
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
            
            /**
             * Takes a date object and returns a string formatted in the correct way for booking an appointment
             * YYYY-MM-DDTHH:MM:SS+00:00 or YYYY-MM-DDTHH:MM:SS+01:00
             * @param {type} day
             * @returns {unresolved}
             */
            function formatDate (day){
                // This function ignores the options parm but seems to produce a string in a workable format esp wrt DST offsets
                var bareDate = day.toLocaleString('en-GB').replace(/^..../,"").replace("GMT","").replace(/ /g,"");
                var month = bareDate.replace(/^(...).*$/,"$1");
                var i;
                switch (month) {
                    case "Jan":
                        i = "01";
                        break;
                    case "Feb":
                        i = "02";
                        break;
                    case "Mar":
                        i = "03";
                        break;
                    case "Apr":
                        i = "04";
                        break;
                    case "May":
                        i = "05";
                        break;
                    case "Jun":
                        i = "06";
                        break;
                    case "Jul":
                        i = "07";
                        break;
                    case "Aug":
                        i = "08";
                        break;
                    case "Sep":
                        i = "09";
                        break;
                    case "Oct":
                        i = "10";
                        break;
                    case "Nov":
                        i = "11";
                        break;
                    case "Dec":
                        i = "12";
                        break;
                }
                // reorders the years and day maps a month string to a number and inserts a colon into the DST offset
                return bareDate.replace(/^(...)(..)(....)/,"$3-"+i+"-$2T").replace(/(..)(..)$/,"$1:$2");
            }
        });
