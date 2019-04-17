'use strict';

angular.module('gpConnect')
        .controller('PatientsDetailCtrl', function ($scope, $stateParams, $state, PatientService, ProviderRouting) {

            PatientService.getFhirPatient(ProviderRouting.defaultPractice().odsCode, $stateParams.patientId).then(function (patient) {
                $scope.patient = patient;
                $.each(patient.identifier, function (key, identifier) {
        if(identifier.system == "https://fhir.nhs.uk/Id/nhs-number"){
                        $scope.patientNhsNumber = identifier.value;
                    }
                });
                $scope.patientLocalIdentifier = patient.id;
                
                // since display is not returned in the patient practitioner reference anymore we need to separately retrieve the practitioner details
                // must be done asynchronously but only after we have retrieved the patient details
                PatientService.getPractitioner(ProviderRouting.defaultPractice().odsCode, patient.generalPractitioner[0].reference.replace("Practitioner/", "")).then(function (practitioner) {
                    $scope.practitioner = practitioner;
                });

            });


            $scope.goTo = function (section) {
                var requestHeader = {
                    patientId: $stateParams.patientId
                };

                var toState = '';

                switch (section) {
                    case 'appointments':
                        toState = 'appointments';
                        break;
                    case 'access-record-html':
                        toState = 'access-record-html';
                        break;
                    case 'access-record-structured':
                        toState = 'access-record-structured';
                        break;
                }

                $state.go(toState, requestHeader);
            };

        });
