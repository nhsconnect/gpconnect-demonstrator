'use strict';

angular.module('gpConnect')
        .controller('AppointmentsAmendModalCtrl', ['$state', '$stateParams', '$scope', '$modalInstance', '$modal', 'usSpinnerService', 'appointment', 'Appointment', 'gpcResource', function ($state, $stateParams, $scope, $modalInstance, $modal, usSpinnerService, appointment, Appointment, gpcResource) {

            $.each(appointment.patient.identifier, function (key, identifier) {
                if (identifier.system == gpcResource.getConst("ID_NHS_NUMBER")) { $scope.patientNhsNumber = identifier.value; }
            });
            
            $scope.appointmentAmend = appointment;
            $scope.validationError = "";
            $scope.cancelReasonIndex = -1;
            if ($scope.appointmentAmend.appointmentResource.resource.extension != undefined) {
                for (var i = 0; i < $scope.appointmentAmend.appointmentResource.resource.extension.length; i++) {
                    if (gpcResource.getConst("SD_EXT_GPC_APPOINT_CANC_REAS") == $scope.appointmentAmend.appointmentResource.resource.extension[i].url) {
                        $scope.cancelReasonIndex = i;
                        i = $scope.appointmentAmend.appointmentResource.resource.extension.length;
                    }
                }
            }
            $scope.descriptionDisplayText = $scope.appointmentAmend.appointmentResource.resource.description;

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
                    if ($scope.descriptionDisplayText != null && $scope.descriptionDisplayText.length > 0) {
                        $scope.appointmentAmend.appointmentResource.resource.description = $scope.descriptionDisplayText;
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
                        $scope.validationError = "A description is required.";
                    }
                }
            };

            $scope.close = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
