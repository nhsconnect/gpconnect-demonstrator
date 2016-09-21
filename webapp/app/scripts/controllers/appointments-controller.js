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
            
            initAppointments($stateParams.patientId);
            
            function initAppointments(externalPatientId) {
            	var patientIdPromises = [];
            	for(var practiceIndex in ProviderRouting.practices) {
                	var practice = ProviderRouting.practices[practiceIndex];
            		
                	var patientIdPromise = PatientService.getPatientFhirId(externalPatientId, practice.odsCode)
                	patientIdPromises.push(patientIdPromise);
            	}
            	
            	var appointmentsPromises = [];
            	$q.all(patientIdPromises).then(function(patientIdResponses) {
            		for(var patientIdIndex in patientIdResponses) {
            			var patientId = patientIdResponses[patientIdIndex];
            			
            			var appointmentsPromise = Appointment.findAllAppointments(externalPatientId, patientId, practice.odsCode);
            			appointmentsPromises.push(appointmentsPromise);
            		}
            		
            		var allPracticeAppointments = [];
                	$q.all(appointmentsPromises).then(function(appointmentsResponses) {
                		for(var appointmentsIndex in appointmentsResponses) {
                			var appointmentsResponse = appointmentsResponses[appointmentsIndex];
                			var appointments = appointmentsResponse.data.entry
                			
                			if(appointments != undefined && appointments.length > 0) {
                				setCancellationReason(appointments);
                				allPracticeAppointments = allPracticeAppointments.concat(appointments);
                			}
                		}
                		
                    	// sort by appointment start datetime ascending
                    	allPracticeAppointments.sort(function (a, b) {
                            return a.resource.start.localeCompare(b.resource.start);
                        });
                    	
                    	$scope.appointments = allPracticeAppointments;
                    	usSpinnerService.stop('patientSummary-spinner');
                    	
                	}).catch(function(e) {
                		usSpinnerService.stop('patientSummary-spinner');
                	});            		
            	}).catch(function(e) {
            		usSpinnerService.stop('patientSummary-spinner');
            	});    	
            }
            
            function setCancellationReason(practiceAppointments) {
                for(var practiceAppointmentIndex in practiceAppointments) {  	
                	var practiceAppointment = practiceAppointments[practiceAppointmentIndex];
                	
	            	if (practiceAppointment.resource.modifierExtension != undefined) {
	                    for (var i = 0; i < practiceAppointment.resource.modifierExtension.length; i++) {
	                        if ("http://fhir.nhs.net/StructureDefinition/extension-gpconnect-appointment-cancellation-reason-1-0" == practiceAppointment.resource.modifierExtension[i].url) {
	                        	practiceAppointment.cancellationReason = practiceAppointment.resource.modifierExtension[i].valueString;
	                            i = practiceAppointment.resource.modifierExtension.length;
	                        }
	                    }
	                }
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
                        Appointment.findResourceByReference($stateParams.patientId, reference).then(function (response) {
                            $scope.appointmentLocation = response.data.name;
                        });
                    } else if (reference.indexOf("Practitioner") != -1) {
                        Appointment.findResourceByReference($stateParams.patientId, reference).then(function (response) {
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
