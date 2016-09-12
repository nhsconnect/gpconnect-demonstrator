'use strict';

angular.module('gpConnect')
        .controller('OrderCreateModalCtrl', function ($state, $stateParams, $scope, $sce, $modalInstance, usSpinnerService, Order, PatientService, ProviderRouting, Organization) {

            $scope.federatedPractices = PatientService.practices;

            PatientService.getFhirPatient(ProviderRouting.defaultPractice().odsCode, $stateParams.patientId).then(function (patient) {
                $scope.patient = patient;
                $.each(patient.identifier, function (key, identifier) {
                    if (identifier.system == "http://fhir.nhs.net/Id/nhs-number") {
                        $scope.patientNhsNumber = identifier.value;
                    }
                });
                $scope.patientLocalIdentifier = patient.id;
            });

            // Set Default Values for screen
            $scope.newOrder = {};
            $scope.newOrder.fromOrg = "GP Connect Demonstrator";
            $scope.newOrder.toOrg = $scope.federatedPractices[0];
            $scope.newOrder.type = "1";


            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };


            $scope.ok = function (orderCreateForm) {

                clearErrorMsg();
                $scope.formSubmitted = true;

                if ($scope.newOrder.details != undefined && $scope.newOrder.details.length > 0) {

                    usSpinnerService.spin('appointmentCreate-spinner');

                    var localModel = {};
                    localModel.identifier = "ID" + new Date().getTime();
                    localModel.subjectPatientId = $scope.patientLocalIdentifier;
                    localModel.sourceOrgId = "1";
                    localModel.targetOrgId = $scope.newOrder.toOrg.id;
                    if ($scope.newOrder.type == "1") {
                        localModel.reasonCode = "1";
                        localModel.reasonDescription = "For your information";
                    } else {
                        localModel.reasonCode = "2";
                        localModel.reasonDescription = "Action needed";
                    }
                    localModel.detail = $scope.newOrder.details;


                    // Lookup the internal ids for the to, from and patient resources on the recieving system. If we fail to find any we should show an error
                    var recievingPracticeOdsCode = $scope.newOrder.toOrg.odsCode;

                    PatientService.getFhirPatient(recievingPracticeOdsCode, $stateParams.patientId).then(function (patientFhirResource) {
                        if (patientFhirResource != undefined) {
                            $scope.recievingSysPatientLocalId = patientFhirResource.id;
                            sendOrder(localModel);
                        } else {
                            addErrorMsg("<div>The recieving system does not have a local identifier for the patient with NHS Number " + $stateParams.patientId + ".</div>");
                        }
                    });

                    // Find the remote systems internal reference to its self for use in the fhir resource
                    Organization.searchForOrganisation(recievingPracticeOdsCode, $stateParams.patientId, recievingPracticeOdsCode).then(function (recievingPracticeOrgResource) {
                        if (recievingPracticeOrgResource.data.entry != undefined) {
                            $scope.recievingSysRecOrgLocalId = recievingPracticeOrgResource.data.entry[0].resource.id;
                            sendOrder(localModel);
                        } else {
                            addErrorMsg("<div>The recieving system does not have a local identifier for it's own ODS code.</div>");
                        }
                    });

                    // Find the local organization Ods Code for the sending organization, so that we can lookup the local identifier for the organization on the recieving system
                    // so we can use that internal reference in the fhir model
                    Organization.findOrganisation($stateParams.patientId, localModel.sourceOrgId).then(function (localOrganizationFhirResourceResponse) {
                        for (var i = 0; i < localOrganizationFhirResourceResponse.data.identifier.length; i++) {
                            if (localOrganizationFhirResourceResponse.data.identifier[i].system == "http://fhir.nhs.net/Id/ods-organization-code") {
                                var sendingPracticeOdsCode = localOrganizationFhirResourceResponse.data.identifier[i].value;
                                Organization.searchForOrganisation(recievingPracticeOdsCode, $stateParams.patientId, sendingPracticeOdsCode).then(function (sendingPracticeOrgResource) {
                                    if (sendingPracticeOrgResource.data.entry != undefined) {
                                        $scope.recievingSysSndOrgLocalId = sendingPracticeOrgResource.data.entry[0].resource.id;
                                        sendOrder(localModel);
                                    } else {
                                        addErrorMsg("<div>The recieving system does not have a local identifier for the sending system's ODS code.</div>");
                                    }
                                });
                            }
                        }
                    });

                } else {
                    addErrorMsg("<div>A task description is required.</div>");
                }
            };


            var sendOrder = function (localModel) {

                // If we have looked up all the recieving system local identifiers for the order fhir resource then proceed to save
                if ($scope.recievingSysPatientLocalId != undefined && $scope.recievingSysRecOrgLocalId != undefined && $scope.recievingSysSndOrgLocalId != undefined) {

                    var fhirModel = {
                        "resourceType": "Order",
                        "identifier": [{"value": localModel.identifier}],
                        "contained": [{
                                "resourceType": "Basic",
                                "id": "1",
                                "text": {"div": "<div>" + localModel.detail + "</div>"},
                                "code": {"coding": [{
                                            "system": "http://hl7.org/fhir/basic-resource-type",
                                            "code": "OrderDetails"}]
                                }
                            }],
                        "subject": {"reference": "Patient/" + $scope.recievingSysPatientLocalId},
                        "source": {"reference": "Organization/" + $scope.recievingSysSndOrgLocalId},
                        "target": {"reference": "Organization/" + $scope.recievingSysRecOrgLocalId},
                        "reasonCodeableConcept": {
                            "coding": [{
                                    "system": "http://hl7.org/fhir/ValueSet/c80-practice-codes",
                                    "code": localModel.reasonCode,
                                    "display": localModel.reasonDescription
                                }]
                        },
                        "detail": [{"reference": "#1"}]
                    };

                    Order.sendOrder($stateParams.patientId, fhirModel, $scope.newOrder.toOrg.odsCode).then(function (result) {
                        localModel.orderDate = result.data.date;
                        Order.saveOrder(localModel).then(function (result) {
                            $modalInstance.close();
                            $state.reload();
                            usSpinnerService.stop('appointmentCreate-spinner');
                        });
                    });
                }

            };

            var addErrorMsg = function (errorHTML) {
                $scope.validationError = $sce.trustAsHtml($scope.validationError + errorHTML);
            };

            var clearErrorMsg = function () {
                $scope.validationError = "";
            };
        });
