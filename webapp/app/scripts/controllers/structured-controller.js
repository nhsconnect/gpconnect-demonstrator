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
    $scope.historicMedications = [];
    
    $scope.currentMedications = [];
    $scope.search = {};
    $scope.search.startDate = "2017-01-02";
    $scope.search.endDate ="2019-04-20";
    $scope.includeResolvedAllergies = true;
    $scope.includePrescriptionIssues = true;
    
    
    initShowHide();
    // $scope.getAllergyData("2019-01-02", "2019-04-20");
    
    
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
    
    
    
    $scope.getAllergyData = function (start, end, includePrescriptionIssues, includeResolvedAllergies) {
        console.log("getAllergyData executed");
        console.log('includePrescriptionIssues'+includePrescriptionIssues)
        
        var startDate = new Date(start);
        startDate.setTime(startDate.getTime() + (60*60*1000)); 
        var endDate = new Date(end);
        endDate.setTime(endDate.getTime() + (60*60*1000)); 
        
        
        start = startDate.toISOString().split('T')[0];
        end = endDate.toISOString().split('T')[0];
    
        
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
            PatientService.structured($stateParams.patientId, start, end, includePrescriptionIssues, includeResolvedAllergies).then(function(
                patientSummaryResponse
            ) {
                $scope.ActiveAllergiesList = [];
                $scope.ResolvedAllergiesList=[];
                
                $scope.MedicationStatement=[];
                $scope.MedicationListList=[];
                $scope.currentMedications=[];
                $scope.historicMedications=[];
                $scope.MedicationRequest = [];
                
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
                    } if(resource.resourceType =="MedicationStatement") {
                        // console.log(resource);
                        $scope.MedicationStatement.push(resource);
                        
                    }
                    
                    if (resource.resourceType == "Medication") {
                        $scope.MedicationListList.push(resource);
                    }
                }
                initgetPastMedication()
                
                // $scope.MedicationStatement.forEach(medicationStatement => {
                $scope.MedicationStatement.forEach(function(medicationStatement)  {
                    
                    var medicationId = medicationStatement.medicationReference.reference.split("/").pop();
                    var medication = $scope.MedicationListList.find(function(medication){ return medication.id === medicationId});
                    
                    var medHelpObj = {};
                    medHelpObj.statement = medicationStatement; 
                    medHelpObj.medication = medication;
                    
                    $scope.currentMedications.push(medHelpObj);
                });
                
                
            });
            
        }
    }
    
    function initShowHide() {
        PatientService.allMedications($stateParams.patientId).then(function (mediacations) {
            console.log(mediacations);
            $scope.AllMedications = mediacations;
        });
    }
    
    function initgetPastMedication() {
        $scope.MedicationStatement.forEach(function(medicationStatement) {
            // historic ID
            var historicId = medicationStatement.basedOn[0].reference.split("/").pop();
            
            $scope.MedicationRequest.forEach(function(MedicationRequest) {
                // check if the historic id matches anything in medication request
                if(historicId == MedicationRequest.id) {
                    //get the medication name
                    var medicationId = medicationStatement.medicationReference.reference.split("/").pop();
                    
                    var medication = $scope.MedicationListList.find(function(x) { return x.id === medicationId});
                    
                    var historyObj = {};
                    historyObj.medication = medication;                  
                    historyObj.statement = medicationStatement;
                    $scope.historicMedications.push(historyObj);
                }
            })
        });
    }
    
    $scope.showForm = function (listType) {
        $scope.listType = listType;
        
        if(listType.resourceType == "Medication") {
            $scope.showMedDetail = true;
            $scope.showAllergyDetail = false;
            $scope.showResAllergyDetail = false;
            
            for(var i=0; i < $scope.MedicationRequest.length; i++) {
                if(listType.id == $scope.MedicationRequest[i].medicationReference.reference.split("/").pop()) {
                    $scope.actualMedicationRequest = $scope.MedicationRequest[i];
                }
                break;
            }

            for(var i=0; i < $scope.MedicationStatement.length; i++) {
                if(listType.id == $scope.MedicationStatement[i].medicationReference.reference.split("/").pop()) {
                    $scope.actualMedicationStatement = $scope.MedicationStatement[i];
                }
                break;
            }
            $scope.MedicationName = listType.code.coding[0].extension[0].extension[1].valueString;
            $scope.practitionername = $scope.practitioner.name[0].prefix[0]+" "+$scope.practitioner.name[0].given[0] + " "+ $scope.practitioner.name[0].family;

            var medName = listType.code.coding[0].extension[0].extension[1].valueString == "Transfer-degraded medication entry" ? listType.code.text :  listType.code.coding[0].extension[0].extension[1].valueString
            $scope.title = "Medication Details: "+medName;
            
        }
        else if (listType.resourceType == "AllergyIntolerance" && listType.clinicalStatus == "active") {
            $scope.showAllergyDetail = true;
            $scope.showMedDetail = false;
            $scope.showResAllergyDetail = false;
            $scope.title = "Allergy Detail: "+listType.code.coding[0].display;
        }
        else if (listType.resourceType == "AllergyIntolerance" && listType.clinicalStatus == "resolved") {
            $scope.showResAllergyDetail = true;
            $scope.showAllergyDetail = false;
            $scope.showMedDetail = false;
            $scope.title = "Allergy Detail: "+listType.code.coding[0].display;
        }
        
        $scope.message = "Show Form Button Clicked";
        
        
        var modalInstance = $modal.open({
            templateUrl: '../views/access-record/modal.html',
            scope: $scope,
            // controller: function($scope, listType){
            //     $scope.listType = listType;
            // },
            // showClose: true,
            resolve: {
                params: function () {
                    return {
                        // title: $scope.title,
                        // message: $scope.message,
                        // listType: listType
                    };
                }
            }
        });

        $scope.closeModalInstance = function(){
            modalInstance.close();
        }
        
        modalInstance.result.then(function (selectedItem) {
            $scope.selected = selectedItem;
        }, function () {
        });
    };
    
    
});

