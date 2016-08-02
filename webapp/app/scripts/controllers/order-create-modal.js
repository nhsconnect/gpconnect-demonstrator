'use strict';

angular.module('gpConnect')
        .controller('OrderCreateModalCtrl', function ($state, $stateParams, $scope, $modalInstance, usSpinnerService, Order, PatientService) {

            PatientService.getFhirPatient($stateParams.patientId).then(function (patient) {
                $scope.patient = patient;
                $.each(patient.identifier, function (key, identifier) {
                    if (identifier.system == "http://fhir.nhs.net/Id/nhs-number") {
                        $scope.patientNhsNumber = identifier.value;
                    }
                });
                $scope.patientLocalIdentifier = patient.id;
            });

            $scope.newOrder = {};
            $scope.newOrder.fromOrg = "GP Connect Demonstrator";
            $scope.newOrder.toOrg = "GPConnectDemonstrator";
            $scope.newOrder.type = "1";

            $scope.ok = function (orderCreateForm) {
                
                $scope.formSubmitted = true;
                usSpinnerService.spin('appointmentCreate-spinner');

                var localModel = {};
                localModel.identifier = "ID" + new Date().getTime();
                localModel.subjectPatientId = $scope.patientLocalIdentifier;
                localModel.sourceOrgId = "1";
                localModel.targetOrgId = "1";
                if ($scope.newOrder.type == "1") {
                    localModel.reasonCode = "1";
                    localModel.reasonDescription = "For your information";
                } else {
                    localModel.reasonCode = "2";
                    localModel.reasonDescription = "Action needed";
                }
                localModel.detail = $scope.newOrder.details;

                var fhirModel = {
                    "resourceType": "Order",
                    "identifier": [{
                            "value": localModel.identifier
                        }],
                    "contained": [{
                            "resourceType": "Basic",
                            "id": "1",
                            "text": {
                                "div": "<div>"+localModel.detail+"</div>"
                            },
                            "code": {
                                "coding": [{
                                        "system": "http://hl7.org/fhir/basic-resource-type",
                                        "code": "OrderDetails"
                                    }]
                            }
                        }],
                    "subject": {
                        "reference": "Patient/"+localModel.subjectPatientId
                    },
                    "source": {
                        "reference": "Organization/"+localModel.sourceOrgId
                    },
                    "target": {
                        "reference": "Organization/"+localModel.targetOrgId
                    },
                    "reasonCodeableConcept": {
                        "coding": [{
                                "system": "http://hl7.org/fhir/ValueSet/c80-practice-codes",
                                "code": localModel.reasonCode,
                                "display": localModel.reasonDescription
                            }]
                    },
                    "detail": [{
                            "reference": "#1"
                        }]
                };

                Order.sendOrder(fhirModel).then(function(result){
                    localModel.orderDate = result.data.date;
                    Order.saveOrder(localModel).then(function(result){
                        $modalInstance.close();
                        $state.reload();
                        usSpinnerService.stop('appointmentCreate-spinner');
                    });
                });
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

        });
