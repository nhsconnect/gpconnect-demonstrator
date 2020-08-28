'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCancelModalCtrl', ['$state', '$stateParams', '$scope', '$modalInstance', '$modal', 'usSpinnerService', 'appointment', 'Appointment', 'gpcResource', function ($state, $stateParams, $scope, $modalInstance, $modal, usSpinnerService, appointment, Appointment, gpcResource) {

                $.each(appointment.patient.identifier, function (key, identifier) {
                    if (identifier.system == gpcResource.getConst("ID_NHS_NUMBER")) {
                        $scope.patientNhsNumber = identifier.value;
                    }
                });

                $scope.appointmentCancel = appointment;
                $scope.appointmentCancel.appointmentResource.resource.status = "cancelled";


                $scope.validationError = "";
                $scope.cancelReasonIndex = -1;
                $scope.orgIndex = -1;

                // look for a cancel reason and org in the extensions
                if ($scope.appointmentCancel.appointmentResource.resource.extension != undefined) {
                    for (var i = 0; i < $scope.appointmentCancel.appointmentResource.resource.extension.length; i++) {
                        if (gpcResource.getConst("SD_EXT_GPC_APPOINT_CANC_REAS") == $scope.appointmentCancel.appointmentResource.resource.extension[i].url) {
                            $scope.cancelReasonIndex = i;
                        } else
                        if (gpcResource.getConst("SD_EXT_ORGANISATION") == $scope.appointmentCancel.appointmentResource.resource.extension[i].url) {
                            $scope.orgIndex = i;
                        }
                    }
                }

                // no cancel reason add one
                if ($scope.cancelReasonIndex == -1) {
                    $scope.appointmentCancel.appointmentResource.resource.extension[$scope.appointmentCancel.appointmentResource.resource.extension.length] =
                            {
                                "url": gpcResource.getConst("SD_EXT_GPC_APPOINT_CANC_REAS"),
                                // this looks odd but it gets populated later because its bound in the corresponding view
                                "valueId": ""
                            }
                    ;
                }

                // no org extension => no contained as well?
                if ($scope.orgIndex == -1) {
                    // contained booking org
                    $scope.appointmentCancel.appointmentResource.resource.contained = [
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
                                    "value": "B82617"
                                }
                            ],
                            "name": "COXWOLD Surgery",
                            "telecom": [
                                {
                                    "system": "phone",
                                    "value": "0300 303 5678"
                                }
                            ]
                        }
                    ];
                    // add the organisation to the extension
                    $scope.appointmentCancel.appointmentResource.resource.extension[$scope.appointmentCancel.appointmentResource.resource.extension.length] =
                            {
                                "url": gpcResource.getConst("SD_EXT_ORGANISATION"),
                                "valueReference": {
                                    "reference": "#1"
                                }
                            };
                }

                // cancellation reason may have moved so recalculate  position
                for (var i = 0; i < $scope.appointmentCancel.appointmentResource.resource.extension.length; i++) {
                    if (gpcResource.getConst("SD_EXT_GPC_APPOINT_CANC_REAS") == $scope.appointmentCancel.appointmentResource.resource.extension[i].url) {
                        $scope.cancelReasonIndex = i;
                    }
                }


                $scope.cancelAppointment = function (appointmentCancelForm) {
                    $scope.formSubmitted = true;
                    if ($scope.appointmentCancel.appointmentResource.resource.extension[$scope.cancelReasonIndex].valueId.length > 0) {
                        usSpinnerService.spin('appointmentCancel-spinner');
                        Appointment.cancel($scope.appointmentCancel.appointmentResource.appointmentPracticeOdsCode, $stateParams.patientId, $scope.appointmentCancel.appointmentResource.resource.id, $scope.appointmentCancel.appointmentResource.resource).then(function (response) {
                            if (response.status != "200") {
                                alert("An error occurred updating appointment, please try cancelling again");
                            }
                        });
                        $modalInstance.close();
                        $state.reload();
                        usSpinnerService.stop('appointmentCancel-spinner');
                    } else {
                        $scope.validationError = "A cancellation reason is required.";
                    }
                };

                $scope.close = function () {
                    $modalInstance.dismiss('cancel');
                };

            }]);
