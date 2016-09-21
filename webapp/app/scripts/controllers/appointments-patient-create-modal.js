'use strict';

angular.module('gpConnect')
        .controller('AppointmentsPatientCreateModalCtrl', function ($stateParams, $scope, $modal, $modalInstance, patient, appointmentBookingParameters, PatientService, usSpinnerService, DateFormatter) {

            $scope.patientDetails = patient;
            $scope.appointmentBooking = appointmentBookingParameters;

            $scope.ok = function () {

                $scope.validationError = "";
                usSpinnerService.spin('patientCreate-spinner');

                // Build patient fhir model
                var newPatient = {};
                newPatient.resourceType = "Patient";
                newPatient.modifierExtension = [
                    {
                        "url": "http://fhir.nhs.net/StructureDefinition/extension-registration-period-1",
                        "valuePeriod": {"start": DateFormatter.clean(new Date())}
                    },
                    {
                        "url": "http://fhir.nhs.net/StructureDefinition/extension-registration-status-1",
                        "valueCodeableConcept": {"coding": [{"system": "http://fhir.nhs.net/ValueSet/registration-status-1", "code": "A", "display": "Active"}]}
                    },
                    {
                        "url": "http://fhir.nhs.net/StructureDefinition/extension-registration-type-1",
                        "valueCodeableConcept": {"coding": [{"system": "http://fhir.nhs.net/ValueSet/registration-type-1", "code": "T", "display": "Temporary Resident"}]}
                    }
                ];
                newPatient.identifier = $scope.patientDetails.identifier;
                newPatient.name = $scope.patientDetails.name;
                newPatient.gender = $scope.patientDetails.gender;
                newPatient.birthDate = $scope.patientDetails.birthDate;
                
                // Create patient on remote system
                PatientService.registerPatient(appointmentBookingParameters.location.odsCode, newPatient, $stateParams.patientId).then(function (registeredPatientResource) {
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
                                appointmentBookingParameters.patientFhirId = registeredPatientResource.id; // Copy new fhir id from returned patient resource
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

        });
