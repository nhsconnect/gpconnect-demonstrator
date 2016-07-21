'use strict';

angular.module('gpConnect')
        .controller('AppointmentsAmendModalCtrl', function ($state, $scope, $modalInstance, $modal, usSpinnerService, appointment, Appointment) {

            $scope.appointmentAmend = appointment;
            $scope.validationError = "";
            $scope.cancelReasonIndex = -1;
            if ($scope.appointmentAmend.appointmentResource.resource.modifierExtension != undefined) {
                for (var i = 0; i < $scope.appointmentAmend.appointmentResource.resource.modifierExtension.length; i++) {
                    if ("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0" == $scope.appointmentAmend.appointmentResource.resource.modifierExtension[i].url) {
                        $scope.cancelReasonIndex = i;
                        i = $scope.appointmentAmend.appointmentResource.resource.modifierExtension.length;
                    }
                }
            }
            $scope.commentDisplayText = $scope.appointmentAmend.appointmentResource.resource.comment;

            $scope.amendAppointment = function (appointmentAmendForm) {
                $scope.formSubmitted = true;
                if($scope.cancelReasonIndex != -1){
                    // Amend cancelled appointment
                    if ($scope.appointmentAmend.appointmentResource.resource.modifierExtension[$scope.cancelReasonIndex].valueString.length > 0) {
                        usSpinnerService.spin('appointmentAmend-spinner');
                        Appointment.save($scope.appointmentAmend.appointmentResource.resource.id, $scope.appointmentAmend.appointmentResource.resource).then(function (response) {
                            if (response.status != "200") {
                                alert("An error occurred updating appointment, please try cancelling again");
                            }
                        });
                        $modalInstance.close();
                        $state.reload();
                        usSpinnerService.stop('appointmentAmend-spinner');
                    } else {
                        $scope.validationError = "Cancellation reason cannot be blank";
                    }
                } else {
                    // Amend un-cancelled appointment
                    if ($scope.commentDisplayText.length > 0) {
                        $scope.appointmentAmend.appointmentResource.resource.comment = $scope.commentDisplayText;
                        usSpinnerService.spin('appointmentAmend-spinner');
                        Appointment.save($scope.appointmentAmend.appointmentResource.resource.id, $scope.appointmentAmend.appointmentResource.resource).then(function (response) {
                            if (response.status != "200") {
                                alert("An error occurred updating appointment, please try cancelling again");
                            }
                        });
                        $modalInstance.close();
                        $state.reload();
                        usSpinnerService.stop('appointmentAmend-spinner');
                    } else {
                        $scope.validationError = "A comment is required.";
                    }
                }
            };

            $scope.close = function () {
                $modalInstance.dismiss('cancel');
            };

        });
