'use strict';

angular.module('gpConnect')
        .controller('AppointmentsCtrl', function ($scope, $http, $stateParams, $state, $modal, PatientService, usSpinnerService, Appointment) {

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

            PatientService.getPatientFhirId($stateParams.patientId).then(function (result) {
                return result;
            }).then(function (result) {

                $scope.patientFhirId = result;

                Appointment.findAllAppointments($scope.patientFhirId).then(function (result) {
                    var appointmentsJson = result.data;
                    $scope.appointments = appointmentsJson.entry;

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
                }).catch(function (e) {
                    usSpinnerService.stop('patientSummary-spinner');
                }).finally(function () {
                    usSpinnerService.stop('patientSummary-spinner');
                });
            });

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
                        Appointment.findResourceByReference(reference).then(function (response) {
                            $scope.appointmentLocation = response.data.name;
                        });
                    } else if (reference.indexOf("Practitioner") != -1) {
                        Appointment.findResourceByReference(reference).then(function (response) {
                            $scope.practitionerName = response.data.name.prefix[0] + " " + response.data.name.given[0] + " " + response.data.name.family[0];
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
