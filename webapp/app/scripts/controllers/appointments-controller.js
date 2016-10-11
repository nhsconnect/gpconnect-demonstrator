'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCtrl', function ($scope, $http, $stateParams, $state, $modal, $q, PatientService, usSpinnerService, Appointment, ProviderRouting, FhirEndpointLookup) {

            $scope.currentPage = 1;

            $scope.pageChangeHandler = function (newPage) {
                $scope.currentPage = newPage;
            };

            if ($stateParams.page) {
                $scope.currentPage = $stateParams.page;
            }

            if ($stateParams.filter) {
                $scope.query = $stateParams.filter;
            }

            initAppointments();

            function initAppointments() {
                $scope.appointments = [];
                $scope.allPractices = ProviderRouting.practices;
                $.each($scope.allPractices, function (key, practice) {
                    if ($scope.searchCount == undefined) {
                        $scope.searchCount = 1;
                    } else {
                        $scope.searchCount++;
                    }
                    practice.statusMsg = "Searching";
                    practice.status = "Searching";
                    PatientService.getFhirPatient(practice.odsCode, $stateParams.patientId).then(
                            function (patient) {
                                if (patient == undefined) {
                                    practice.statusMsg = "Failed to find patient";
                                    practice.status = "Failed";
                                    onFindAppointmentDone();
                                } else {
                                    $scope.fhirPatient = patient;
                                    Appointment.findAllAppointments($stateParams.patientId, patient.id, practice.odsCode).then(
                                            function (findAllAppointmentsSuccess) {
                                                var appointments = findAllAppointmentsSuccess.data.entry;
                                                if (appointments != undefined) {
                                                    $.each(appointments, function (key, appointment) {
                                                        appointment.appointmentPractice = practice.name;
                                                        appointment.appointmentPracticeOdsCode = practice.odsCode;
                                                    });
                                                    $scope.appointments = $scope.appointments.concat(appointments); // Add appointments to total list
                                                    practice.statusMsg = "Appointments found";
                                                    practice.status = "Success";
                                                    onFindAppointmentDone();
                                                } else {
                                                    practice.statusMsg = "No appointments found";
                                                    practice.status = "Success";
                                                    onFindAppointmentDone();
                                                }
                                            },
                                            function (findAllAppointmentsFailure) {
                                                practice.statusMsg = "Failed appointment search";
                                                practice.status = "Failed";
                                                onFindAppointmentDone();
                                            }
                                    );
                                }
                            },
                            function (getPatientFhirIdFailure) {
                                practice.statusMsg = "Failed to connect";
                                practice.status = "Failed";
                                onFindAppointmentDone();
                            }
                    );
                });
            }

            function onFindAppointmentDone() {
                $scope.searchCount--;
                if ($scope.searchCount <= 0) {
                    if ($scope.appointments != undefined) {
                        $scope.appointments = $scope.appointments.sort(function (a, b) {
                            return a.resource.start.localeCompare(b.resource.start);
                        });
                        $.each($scope.appointments, function (key, appointment) {
                            if (appointment.resource.modifierExtension != undefined) {
                                for (var i = 0; i < appointment.resource.modifierExtension.length; i++) {
                                    if ("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0" == appointment.resource.modifierExtension[i].url) {
                                        appointment.cancellationReason = appointment.resource.modifierExtension[i].valueString;
                                        i = appointment.resource.modifierExtension.length;
                                    }
                                }
                            }
                        });
                    }
                    usSpinnerService.stop('patientSummary-spinner');
                }
            }

            $scope.go = function (id) {
                usSpinnerService.spin('patientSummary-spinner');
                $scope.appointmentDetail = undefined;
                $scope.appointmentLocation = undefined;
                $scope.practitionerName = undefined;

                var appointment;
                for (var index = 0; index < $scope.appointments.length; ++index) {
                    appointment = $scope.appointments[index];
                    if (appointment.resource.id == id) {
                        $scope.appointmentDetail = appointment;
                        break;
                    }
                }

                $.each($scope.appointmentDetail.resource.participant, function (key, value) {
                    var reference = value.actor.reference.toString();
                    if (reference.indexOf("Location") != -1) {
                        $scope.appointmentLocation = "[Searching...]";
                        Appointment.findResourceByReference($scope.appointmentDetail.appointmentPracticeOdsCode, $stateParams.patientId, reference).then(function (response) {
                            if(response.data == undefined || response.data == null){
                                $scope.appointmentLocation = "[Lookup failed]";
                            } else {
                                $scope.appointmentLocation = response.data.name;
                            }
                        },
                        function (failedResponse) {
                            $scope.appointmentLocation = "[Lookup failed]";
                        });
                    } else if (reference.indexOf("Practitioner") != -1) {
                        $scope.practitionerName = "[Searching...]";
                        Appointment.findResourceByReference($scope.appointmentDetail.appointmentPracticeOdsCode, $stateParams.patientId, reference).then(function (response) {
                            if(response.data == undefined || response.data == null){
                                $scope.practitionerName = "[Lookup failed]";
                            } else {
                                $scope.practitionerName = response.data.name.prefix[0] + " " + response.data.name.given[0] + " " + response.data.name.family[0];
                            }
                        },
                        function (failedResponse) {
                            $scope.practitionerName = "[Lookup failed]";
                        });
                    }

                });

                usSpinnerService.stop('patientSummary-spinner');
            };

            $scope.selected = function (appointmentIndex) {
                return appointmentIndex === $stateParams.appointmentIndex;
            };

            $scope.cancelAppointment = function () {
                $modal.open({
                    templateUrl: 'views/appointments/appointments-cancel-modal.html',
                    size: 'md',
                    controller: 'AppointmentsCancelModalCtrl',
                    resolve: {
                        appointment: function () {
                            return {
                                patient: $scope.fhirPatient,
                                appointmentResource: $scope.appointmentDetail,
                                practitionerName: $scope.practitionerName,
                                appointmentLocation: $scope.appointmentLocation
                            };
                        }
                    }
                });
            };

            $scope.amendAppointment = function () {
                $modal.open({
                    templateUrl: 'views/appointments/appointments-amend-modal.html',
                    size: 'md',
                    controller: 'AppointmentsAmendModalCtrl',
                    resolve: {
                        appointment: function () {
                            return {
                                appointmentResource: $scope.appointmentDetail,
                                practitionerName: $scope.practitionerName,
                                appointmentLocation: $scope.appointmentLocation
                            };
                        }
                    }
                });
            };

            $scope.create = function () {
                $modal.open({
                    templateUrl: 'views/appointments/appointments-search-modal.html',
                    size: 'md',
                    controller: 'AppointmentsSearchModalCtrl',
                    resolve: {
                        modal: function () {
                            return {
                                title: 'Appointment Search'
                            };
                        }
                    }
                });
            };

        });
