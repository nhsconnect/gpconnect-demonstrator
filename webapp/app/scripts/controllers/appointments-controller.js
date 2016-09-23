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
            	$scope.missingPractices = [];
            	$scope.foundPractices = [];
            	var allPractices = ProviderRouting.practices;
            	var practiceMap = {};
            	
            	var appointmentsMap = {};
            	
            	for(var practiceIndex in allPractices) {
                	var practice = allPractices[practiceIndex];
                	practiceMap[practice.ASID] = practice;
                	            		
                	PatientService.getFhirPatientResponse($stateParams.patientId, practice.odsCode).then(
            			function(getPatientFhirIdSuccess) {
            				// lookup ODS code
            				var sspTo = getPatientFhirIdSuccess.config.headers['Ssp-To'];
            				var practice = practiceMap[sspTo]
            				
            				$scope.foundPractices.push(practiceMap[sspTo]);
            				
            				var patient = PatientService.getPatientFromResponse(getPatientFhirIdSuccess);
            				
            				Appointment.findAllAppointments($stateParams.patientId, patient.id, practice.odsCode).then(
        						function(findAllAppointmentsSuccess) {
        							var appointments = findAllAppointmentsSuccess.data.entry;
                        			
                        			if(appointments != undefined && appointments.length > 0) {
                        				setCancellationReason(appointments);
                        				var sspTo = findAllAppointmentsSuccess.config.headers['Ssp-To'];
                        				appointmentsMap[sspTo] = appointments;
                        				
                        				// now check if we're done by comparing the number of keys to the number of
                        				// practices
                        				if(Object.keys(appointmentsMap).length == $scope.foundPractices.length) {
                        					onFindAllAppointmentsDone(appointmentsMap);
                        				}
                        			}
        						},
        						function(findAllAppointmentsFailure) {
        							var sspTo = findAllAppointmentsFailure.config.headers['Ssp-To'];
                    				appointmentsMap[sspTo] = [];
                    				
                    				// now check if we're done by comparing the number of keys to the number of
                    				// practices
                    				if(Object.keys(appointmentsMap).length == $scope.foundPractices.length) {
                    					onFindAllAppointmentsDone(appointmentsMap);
                    				}                    				
        						}
            				);
            			}, 
            			function(getPatientFhirIdFailure) {		
            				var sspTo = getPatientFhirIdFailure.config.headers['Ssp-To'];
            				$scope.missingPractices.push(practiceMap[sspTo]);
            			}
                	);                	
            	}
            }
            
            function onFindAllAppointmentsDone(appointmentsMap) {
            	var allPracticeAppointments = [];
            	
            	for(var practice in appointmentsMap) {
            		allPracticeAppointments = allPracticeAppointments.concat(appointmentsMap[practice]);
            	}
            	
            	// sort by appointment start datetime ascending
            	allPracticeAppointments.sort(function (a, b) {
                    return a.resource.start.localeCompare(b.resource.start);
                });
            	
            	$scope.appointments = allPracticeAppointments;
            	usSpinnerService.stop('patientSummary-spinner');   
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
