'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCancelModalCtrl', ['$state', '$stateParams', '$scope', '$modalInstance', '$modal', 'usSpinnerService', 'appointment', 'Appointment', 'gpcResource', function ($state, $stateParams, $scope, $modalInstance, $modal, usSpinnerService, appointment, Appointment, gpcResource) {

            $.each(appointment.patient.identifier, function (key, identifier) {
                if (identifier.system == gpcResource.getConst("ID_NHS_NUMBER")) { $scope.patientNhsNumber = identifier.value; }
            });
            
            $scope.appointmentCancel = appointment;
            $scope.validationError = "";
            $scope.cancelReasonIndex = -1;
            if ($scope.appointmentCancel.appointmentResource.resource.extension != undefined) {
                for (var i = 0; i < $scope.appointmentCancel.appointmentResource.resource.extension.length; i++) {
                    if (gpcResource.getConst("SD_EXT_GPC_APPOINT_CANC_REAS") == $scope.appointmentCancel.appointmentResource.resource.extension[i].url) {
                        $scope.cancelReasonIndex = i;
                        i = $scope.appointmentCancel.appointmentResource.resource.extension.length;
                    }
                }
            }
            if ($scope.cancelReasonIndex == -1) {
                $scope.appointmentCancel.appointmentResource.resource.extension = [{
                        "url": gpcResource.getConst("SD_EXT_GPC_APPOINT_CANC_REAS"),
                        "valueId": ""
                    }];
                $scope.cancelReasonIndex = 0;
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
