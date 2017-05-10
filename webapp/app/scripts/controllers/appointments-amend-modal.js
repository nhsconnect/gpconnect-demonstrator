'use strict';

angular.module('gpConnect')
        .controller('AppointmentsAmendModalCtrl', function ($state, $stateParams, $scope, $modalInstance, $modal, usSpinnerService, appointment, Appointment) {

            $.each(appointment.patient.identifier, function (key, identifier) {
                if (identifier.system == "http://fhir.nhs.net/Id/nhs-number") { $scope.patientNhsNumber = identifier.value; }
            });
            
            $scope.appointmentAmend = appointment;
            $scope.validationError = "";
            $scope.cancelReasonIndex = -1;
            if ($scope.appointmentAmend.appointmentResource.resource.extension != undefined) {
                for (var i = 0; i < $scope.appointmentAmend.appointmentResource.resource.extension.length; i++) {
                    if ("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1" == $scope.appointmentAmend.appointmentResource.resource.extension[i].url) {
                        $scope.cancelReasonIndex = i;
                        i = $scope.appointmentAmend.appointmentResource.resource.extension.length;
                    }
                }
            }
            $scope.commentDisplayText = $scope.appointmentAmend.appointmentResource.resource.comment;

            $scope.amendAppointment = function (appointmentAmendForm) {
                $scope.formSubmitted = true;
                if($scope.cancelReasonIndex != -1){
                    // Amend cancelled appointment
                    if ($scope.appointmentAmend.appointmentResource.resource.extension[$scope.cancelReasonIndex].valueString.length > 0) {
                        usSpinnerService.spin('appointmentAmend-spinner');
                        Appointment.save($scope.appointmentAmend.appointmentResource.appointmentPracticeOdsCode, $stateParams.patientId, $scope.appointmentAmend.appointmentResource.resource.id, $scope.appointmentAmend.appointmentResource.resource).then(function (response) {
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
                        Appointment.save($scope.appointmentAmend.appointmentResource.appointmentPracticeOdsCode, $stateParams.patientId, $scope.appointmentAmend.appointmentResource.resource.id, $scope.appointmentAmend.appointmentResource.resource).then(function (response) {
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
