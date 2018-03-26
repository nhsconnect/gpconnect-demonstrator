"use strict";

angular
    .module("gpConnect")
    .controller("AppointmentsStructuredCtrl", function(
        $scope,
        $http,
        PatientService,
        $stateParams
    ) {
        $scope.ActiveAllergies = {};
        $scope.ResolvedAllergies = {};
        $scope.ActiveAllergiesList = [];
        $scope.ResolvedAllergiesList = [];

        $scope.patient = {
            name: "",
            id: "",
            birthDate: ""
        };
        $scope.practitioner = {
            name: "",
            id: ""
        };
        $scope.organization = {
            name: "",
            id: ""
        };

        $scope.allergyIntolerance = {
            assertedDate: "",
            category: "",
            clinicalStatus: "",
            patient: "",
            reaction: "",
            resolvedDate: ""
        };

        $scope.getAllergyData = function() {
            {
                PatientService.structured($stateParams.patientId).then(function(
                    patientSummaryResponse
                ) {

                    for (
                        var i = 0;
                        i < patientSummaryResponse.entry.length;
                        i++
                    ) {
                        var resource = patientSummaryResponse.entry[i].resource;

                        if (resource.resourceType == "Patient") {
                            $scope.patient = {
                                name: resource.name[0].text,
                                id: resource.id,
                                birthDate: resource.birthDate
                            };
                        } else if (
                            patientSummaryResponse.entry[i].resource
                                .resourceType == "Organization"
                        ) {
                            $scope.organization = {
                                name: resource.name,
                                id: resource.id
                            };
                        } else if (resource.resourceType == "Practitioner") {
                            $scope.practitioner = {
                                name: resource.name,
                                id: resource.id
                            };
                        } else if (
                            resource.resourceType == "AllergyIntolerance"
                        ) {
                            if (resource.clinicalStatus == "active") {
                                $scope.ActiveAllergiesList.push(resource);
                            } else {
                                $scope.ResolvedAllergiesList.push(resource);
                            }

                          
                        }
                    }
                });
            }
        };
    });
