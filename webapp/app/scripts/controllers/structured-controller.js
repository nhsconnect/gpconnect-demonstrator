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
    $scope.data = {"Iodine (substance)": "Iodine allergy"};
    $scope.MedicationRequest = [];
    $scope.MedicationStatement = [];
    $scope.historicMedications = [];

    $scope.currentMedications = [];
    $scope.search = {};
    $scope.search.startDate = "";
    $scope.search.endDate ="";
    $scope.includeResolvedAllergies = true;
    $scope.includePrescriptionIssues = true;

    $scope.dateInvalid = false;

    initShowHide();

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
        $scope.dateInvalid = false;
        if(start === undefined || start == null){
            start = "";
        }

        if(end === undefined || end == null){
            end = "";
        }

        var createDate = function(stringDate){
            var date = new Date(stringDate);
            date.setTime(date.getTime() + (60*60*1000));

            return date;
        };

        if(start !== "" && end !== ""){
            var startDate = createDate(start);

            var endDate = new Date(end);
            endDate.setTime(endDate.getTime() + (60*60*1000));

            if(isNaN(startDate) || isNaN(endDate) || startDate > endDate){
                $scope.dateInvalid = true;
                return;
            }

            start = startDate.toISOString().split('T')[0];
            end = endDate.toISOString().split('T')[0];
        } else {
            if(start === "" && end !== ""){
                var endDate = createDate(end);
                if(isNaN(endDate)){
                    $scope.dateInvalid = true;
                    return;
                }

                end = endDate.toISOString().split('T')[0];

            }

            if(start !== "" && end === ""){
                var startDate = new Date(start);

                if(isNaN(startDate)){
                    $scope.dateInvalid = true;
                    return;
                }
                start = startDate.toISOString().split('T')[0];

            }
        }{

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
                const medicationWarningCodeSet = new Set();
                const activewarningCodeSet = new Set();
                const resolvedwarningCodeSet = new Set();
                $scope.MedicationWarningCodes = [];
                $scope.ActiveAllergiesWarningCodes = [];
                $scope.ResolvedAllergisWarningCodes = [];

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
                        }

                    }else if(
                        resource.title == "Resolved Allergies" && 'contained' in resource
                    ) {
                        $scope.ResolvedAllergiesList=  $scope.ResolvedAllergiesList.concat(resource.contained);
                    } else if(
                        resource.resourceType =="MedicationRequest"
                    ) {
                        $scope.MedicationRequest.push(resource);
                    } else if(
                        resource.resourceType =="MedicationStatement"
                    ) {
                        // console.log(resource);
                        $scope.MedicationStatement.push(resource);

                    }

                    if (resource.resourceType == "Medication") {
                        $scope.MedicationListList.push(resource);
                    }
                    if(resource.resourceType == "List" && resource.extension != null) {
                        var warningCodeUrl = "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-ListWarningCode-1";
                        var extentionList = resource.extension;
                        for(var j =0; j <extentionList.length; j++) {
                                if(extentionList[j].url == warningCodeUrl) {
                                    if(extentionList[j].valueCoding.display == "Medications and medical devices") {
                                        medicationWarningCodeSet.add(extentionList[j].valueCoding.code);
                                    }
                                   else  if(extentionList[j].valueCoding.display == "Active Allergies") {
                                        activewarningCodeSet.add(extentionList[j].valueCoding.code);
                                    }
                                    else if(extentionList[j].valueCoding.display == "Resolved Allergies") {
                                        resolvedwarningCodeSet.add(extentionList[j].valueCoding.code);
                                    }
                                }
                        }
                    }


                }

                medicationWarningCodeSet.forEach(function (value) {
                    if(value == "confidential-items") {
                        $scope.MedicationWarningCodes.push("Confidential items");
                    }
                    else if(value == "data-in-transit") {
                        $scope.MedicationWarningCodes.push("Data in transit");
                    }
                    else if(value == "data-awaiting-filing") {
                        $scope.MedicationWarningCodes.push("Data awaiting filing");
                    }
                });

                activewarningCodeSet.forEach(function (value) {
                    if(value == "confidential-items") {
                        $scope.ActiveAllergiesWarningCodes.push("Confidential items");
                    }
                    else if(value == "data-in-transit") {
                        $scope.ActiveAllergiesWarningCodes.push("Data in transit");
                    }
                    else if(value == "data-awaiting-filing") {
                        $scope.ActiveAllergiesWarningCodes.push("Data awaiting filing");
                    }
                });


                resolvedwarningCodeSet.forEach(function (value) {
                    if(value == "confidential-items") {
                        $scope.ResolvedAllergisWarningCodes.push("Confidential items");
                    }
                    else if(value == "data-in-transit") {
                        $scope.ResolvedAllergisWarningCodes.push("Data in transit");
                    }
                    else if(value == "data-awaiting-filing") {
                        $scope.ResolvedAllergisWarningCodes.push("Data awaiting filing");
                    }
                });


                initgetPastMedication();

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

            $scope.actualMedicationRequestWithIssue = {issues: []};

            for(var i=0; i < $scope.MedicationRequest.length; i++) {
                if(listType.id == $scope.MedicationRequest[i].medicationReference.reference.split("/").pop()) {
                    if($scope.MedicationRequest[i].intent === "plan"){
                        $scope.actualMedicationRequestWithIssue.plan = $scope.MedicationRequest[i];
                        if(!$scope.includePrescriptionIssues){
                            break;
                        }
                    } else {
                        $scope.actualMedicationRequestWithIssue.issues.push($scope.MedicationRequest[i]);
                    }
                }
            }

            var extensionArray =  $scope.actualMedicationRequestWithIssue.plan.extension;

            var requestType = extensionArray.find(function(element){
                return element.url === "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-PrescriptionType-1";
            }).valueCodeableConcept.coding[0].display;

            $scope.actualMedicationRequestWithIssue.requestType = requestType;

            if(requestType === "Repeat"){
                $scope.actualMedicationRequestWithIssue.repeat = extensionArray.find(function(element){
                    return element.url === "https://fhir.nhs.uk/STU3/StructureDefinition/Extension-CareConnect-GPC-MedicationRepeatInformation-1";
                }).extension;
            }

            $scope.MedicationName = listType.code.coding[0].extension[0].extension[1].valueString;
            $scope.practitionername = $scope.practitioner.name[0].prefix[0]+" "+$scope.practitioner.name[0].given[0] + " "+ $scope.practitioner.name[0].family;

            var medName = listType.code.coding[0].extension[0].extension[1].valueString == "Transfer-degraded medication entry" ? listType.code.text :  listType.code.coding[0].extension[0].extension[1].valueString
            $scope.title = "Medication Detail: "+ medName;

        }
        else if (listType.resourceType == "AllergyIntolerance" && listType.clinicalStatus == "active") {
            $scope.showAllergyDetail = true;
            $scope.showMedDetail = false;
            $scope.showResAllergyDetail = false;
            $scope.title = "Allergy Detail: "+ listType.code.coding[0].display;
        }
        else if (listType.resourceType == "AllergyIntolerance" && listType.clinicalStatus == "resolved") {
            $scope.showResAllergyDetail = true;
            $scope.showAllergyDetail = false;
            $scope.showMedDetail = false;
            $scope.title = "Allergy Detail: "+ listType.code.coding[0].display;
        }

        $scope.message = "Show Form Button Clicked";
		var global = Function('return this')() || (42, eval)('this');
  		var modalInstance = $modal.open({
            templateUrl: global.__env.templateUrl,
            scope: $scope,
            resolve: {
                params: function () {
                    return {
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

    $scope.getAllergyData("", "", true, true);

});

