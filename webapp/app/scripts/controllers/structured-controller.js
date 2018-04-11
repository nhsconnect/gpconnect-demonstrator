"use strict";

angular
    .module("gpConnect")
    .controller("AppointmentsStructuredCtrl", function(
        $scope,
        $http,
        PatientService,
        $stateParams
    ) {
        $scope.MedicationListList = [];
        $scope.AllMedications = {};
        $scope.IsDisabled = true;
        $scope.ActiveAllergiesList = [];
        $scope.ResolvedAllergiesList = [];
        $scope.patientsAllergies = [];
        $scope.dropdwonListValue = '';
        $scope.allergies = [];
        $scope.highlightValues = false;
        $scope.data = {"Iodine (substance)": "Beer allergy"};

        initGetMedicationData();
        initShowHide();
        initGetAllergies();

         function initGetMedicationData() {
             PatientService.medication($stateParams.patientId).then(function(
                 patientSummaryResponse
             ) {
                 for (
                     var i = 0; i < patientSummaryResponse.entry.length;i++) {
                     var resource = patientSummaryResponse.entry[i].resource;

                     if (resource.resourceType == "Medication") {
                         $scope.MedicationListList.push(resource);
                     }

                 }
             });
         }

        $scope.openDatePicker = function ($event, name) {
            $event.preventDefault();
            $event.stopPropagation();
            $scope.startDate = false;
            $scope.endDate = false;
            $scope[name] = true;
            highlightMonth();
        };

        var highlightMonth = function(){
            $(".text-muted").parent().addClass("otherMonth");
            $("button").unbind('click', highlightMonth);
            $("button").bind('click', highlightMonth);
        };


        function initGetAllergies() {
            PatientService.allergyDetialsByPatient($stateParams.patientId
            ).then(function(allergySummaryResponse) {
                $scope.patientsAllergies = allergySummaryResponse;
            });
        }

        $scope.onChange = function(item) {
            $scope.IsDisabled = false;
            $scope.allergies = $scope.AllMedications[item];
            $scope.selectedMedication =item;


        };

          $scope.styleFunction = function(index) {
             if($scope.allergies.includes(index)) {
                 return{
                     "background-color": "yellow"
                 }
             }
              else {
                 return{
                     "background-color": "white"
                 }
             }
        };

        $scope.addNewMedication = function () {
            // alert("New medication functionality is not available. It will be implemented in an upcoming release.");
            var data = {
                "medication": $scope.selectedMedication,
                "nhsNumber":$stateParams.patientId
            };
            PatientService.addMedication(data ).then(function(response) {
                if(response.status == "200") {
                    console.log("Everything went fine.");
                }
            });
        };



        $scope.getAllergyData = function() {
            {
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
                            } else if(resource.clinicalStatus == "resolved") {
                                $scope.ResolvedAllergiesList.push(resource);
                            }


                        }
                    }
                });
            }
        };

        function initShowHide() {
            PatientService.allMedications().then(function (mediacations) {
                $scope.AllMedications = mediacations;
            });
        }
    });

