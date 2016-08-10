'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCancelModalCtrl', function ($state, $stateParams, $scope, $modalInstance, $modal, usSpinnerService, appointment, Appointment) {

            $scope.appointmentCancel = appointment;
            $scope.validationError = "";
            $scope.cancelReasonIndex = -1;
            if ($scope.appointmentCancel.appointmentResource.resource.modifierExtension != undefined) {
                for (var i = 0; i < $scope.appointmentCancel.appointmentResource.resource.modifierExtension.length; i++) {
                    if ("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0" == $scope.appointmentCancel.appointmentResource.resource.modifierExtension[i].url) {
                        $scope.cancelReasonIndex = i;
                        i = $scope.appointmentCancel.appointmentResource.resource.modifierExtension.length;
                    }
                }
            }
            if ($scope.cancelReasonIndex == -1) {
                $scope.appointmentCancel.appointmentResource.resource.modifierExtension = [{
                        "url": "http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0",
                        "valueString": ""
                    }];
                $scope.cancelReasonIndex = 0;
            }

            $scope.cancelAppointment = function (appointmentCancelForm) {
                $scope.formSubmitted = true;
                if ($scope.appointmentCancel.appointmentResource.resource.modifierExtension[$scope.cancelReasonIndex].valueString.length > 0) {
                    usSpinnerService.spin('appointmentCancel-spinner');
                    Appointment.save($stateParams.patientId, $scope.appointmentCancel.appointmentResource.resource.id, $scope.appointmentCancel.appointmentResource.resource).then(function (response) {
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

        });
