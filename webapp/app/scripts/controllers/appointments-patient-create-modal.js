'use strict';

angular.module('gpConnect')
        .controller('AppointmentsPatientCreateModalCtrl', ['$stateParams', '$scope', '$modal', '$modalInstance', 'patient', 'appointmentBookingParameters', 'PatientService', 'usSpinnerService', 'DateFormatter', 'gpcResource', function ($stateParams, $scope, $modal, $modalInstance, patient, appointmentBookingParameters, PatientService, usSpinnerService, DateFormatter, gpcResource) {

            $scope.patientDetails = patient;
            $scope.appointmentBooking = appointmentBookingParameters;

            $scope.ok = function () {

                $scope.validationError = "";
                usSpinnerService.spin('patientCreate-spinner');

                // Build patient fhir model
                var newPatient = {};
                newPatient.resourceType = "Patient";
                newPatient.extension = [
                    {
                        "url": gpcResource.getConst("SD_EXT_REG_PERIOD"),
                        "valuePeriod": {"start": DateFormatter.clean(new Date())}
                    },
                    {
                        "url": gpcResource.getConst("SD_EXT_REG_STATUS"),
                        "valueCodeableConcept": {"coding": [{"system": gpcResource.getConst("VS_REG_STATUS"), "code": "A", "display": "Active"}]}
                    },
                    {
                        "url": gpcResource.getConst("SD_EXT_REG_TYPE"),
                        "valueCodeableConcept": {"coding": [{"system": gpcResource.getConst("VS_REG_TYPE"), "code": "T", "display": "Temporary Resident"}]}
                    }
                ];
                newPatient.identifier = $scope.patientDetails.identifier;
                newPatient.name = $scope.patientDetails.name;
                newPatient.gender = $scope.patientDetails.gender;
                newPatient.birthDate = $scope.patientDetails.birthDate;
                
                var requestParameters = {
                    "resourceType": "Parameters",
                    "parameter": [
                        {
                            "name": "registerPatient",
                            "resource": newPatient
                        }
                    ]
                };
                
                // Create patient on remote system
                PatientService.registerPatient(appointmentBookingParameters.location.odsCode, requestParameters, $stateParams.patientId).then(function (registeredPatientResource) {
                    usSpinnerService.stop('patientCreate-spinner');
                    $modalInstance.close();
                    $modal.open({
                        templateUrl: 'views/appointments/appointments-create-modal.html',
                        size: 'md',
                        controller: 'AppointmentsCreateModalCtrl',
                        resolve: {
                            modal: function () {
                                return {
                                    title: 'Book Appointment'
                                };
                            },
                            patient: function () {
                                return $scope.patientDetails;
                            },
                            appointmentBookingParams: function () {
                                appointmentBookingParameters.patientFhirId = registeredPatientResource.entry[0].resource.id; // Copy new fhir id from returned patient resource
                                return appointmentBookingParameters;
                            }
                        }
                    });
                }, function (errorResult){
                    usSpinnerService.stop('patientCreate-spinner');
                    $scope.validationError = "Failed to create patient on remote system";
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        }]);
