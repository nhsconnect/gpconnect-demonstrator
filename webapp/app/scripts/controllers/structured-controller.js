"use strict";

angular
    .module("gpConnect")
    .controller("AppointmentsStructuredCtrl", function(
        $scope,
        $http,
        PatientService,
        $stateParams,
        $modal
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
        $scope.MedicationRequest = [];
        $scope.MedicationStatement = [];

        initGetMedicationData();
        initShowHide();
        // initGetAllergies();
        getAllergyData();

         function initGetMedicationData() {
             $scope.MedicationListList.length = 0;
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
                initGetMedicationData();
                if(response.status == "200") {
                    console.log("Everything went fine.");
                }
            });
        };



        function getAllergyData() {
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

                        } if(resource.resourceType =="MedicationRequest") {
                            $scope.MedicationRequest.push(resource);
                        }
                        if(resource.resourceType =="MedicationStatement") {
                            $scope.MedicationStatement.push(resource);
                        }
                    }
                });

                console.log($scope.MedicationStatement);
            }
        };

        function initShowHide() {
            PatientService.allMedications($stateParams.patientId).then(function (mediacations) {
                console.log(mediacations);
                $scope.AllMedications = mediacations;
            });
        }

        $scope.showForm = function (listType) {
            // $scope.title ='';

            if(listType.resourceType == "Medication") {
                $scope.showMedDetail = true;
                $scope.showAllergyDetail = false;
                $scope.showResAllergyDetail = false;
                $scope.title = "Medication Details";

                for(var i=0; i < $scope.MedicationRequest.length; i++) {
                    if(listType.id == $scope.MedicationRequest[i].medicationReference.reference.split("/").slice(1).pop()) {
                        $scope.medicationType = $scope.MedicationRequest[i].extension[1].valueCodeableConcept.coding[0].display;
                    }
                    break;
                }
                $scope.MedicationName = listType.code.coding[0].display;
                $scope.practitionername = $scope.practitioner.name[0].prefix[0]+" "+$scope.practitioner.name[0].given[0] + " "+ $scope.practitioner.name[0].family;
            }
            else if (listType.resourceType == "AllergyIntolerance" && listType.clinicalStatus == "active") {
                $scope.showAllergyDetail = true;
                $scope.showMedDetail = false;
                $scope.title = "Allergy Details";
            }
            else if (listType.resourceType == "AllergyIntolerance" && listType.clinicalStatus == "resolved") {
                $scope.showResAllergyDetail = true;
                $scope.showAllergyDetail = false;
                $scope.showMedDetail = false;
                $scope.title = "Resolved Allergy Details";
            }

            $scope.message = "Show Form Button Clicked";


            var modalInstance = $modal.open({
                templateUrl: '../views/access-record/modal.html',
                // controller: ModalInstanceCtrl,
                scope: $scope,
                resolve: {
                    params: function () {
                        return {
                            title: $scope.title,
                            message: $scope.message
                        };
                    }
                }
            });

            modalInstance.result.then(function (selectedItem) {
                $scope.selected = selectedItem;
            }, function () {
            });
        };

        var ModalInstanceCtrl = function ($scope, $modalInstance) {
            $scope.submitForm = function () {
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };
        }


    });

