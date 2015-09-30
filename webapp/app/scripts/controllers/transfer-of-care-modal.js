'use strict';

/*jshint loopfunc: true*/
angular.module('rippleDemonstrator')
  .controller('TransferOfCareCtrl', function ($modal, $state, $scope, $q, $stateParams, PatientService, TransferOfCare, Allergy, Diagnosis, Medication, Contact) {

    $modal.open({
      templateUrl: 'views/transfer-of-care/transfer-of-care-modal.html',
      size: 'lg',
      resolve: {
        patient: function () {
          return PatientService.get($stateParams.patientId).then(function (patient) {
            $scope.patient = patient;
            return $scope.patient;
          });
        },

        transferOfCareCompositions: function () {
          return TransferOfCare.all($stateParams.patientId).then(function (result) {
            $scope.transferOfCareCompositions = result.data;
            return $scope.transferOfCareCompositions;
          });
        }
      },
      controller: function ($scope, patient, transferOfCareCompositions) {
        $scope.transferOfCare = {};
        $q.all([
          Allergy.all($stateParams.patientId),
          Diagnosis.all($stateParams.patientId),
          Medication.all($stateParams.patientId),
          Contact.all($stateParams.patientId)
        ]).then(function (allResult) {
          $scope.transferOfCare.allergies = {};
          $scope.transferOfCare.problems = {};
          $scope.transferOfCare.medication = {};
          $scope.transferOfCare.contacts = {};

          $scope.transferOfCare.allergies.allergies = allResult[0].data;
          $scope.transferOfCare.problems.problems = allResult[1].data;
          $scope.transferOfCare.medication.medications = allResult[2].data;
          $scope.transferOfCare.contacts.contacts = allResult[3].data;

          $scope.allergies = $scope.transferOfCare.allergies.allergies;
          $scope.problems = $scope.transferOfCare.problems.problems;
          $scope.medications = $scope.transferOfCare.medication.medications;
          $scope.contacts = $scope.transferOfCare.contacts.contacts;
        });

        $scope.patient = patient;
        $scope.transferOfCareComposition = transferOfCareCompositions;
        $scope.siteFrom = 'WORCESTERSHIRE HEALTH AND CARE NHS TRUST';

        $scope.selectedItems = {
          allergies: [],
          contacts: [],
          medications: [],
          problems: []
        };

        $scope.transferDetail = {
          site: {}
        };

        $scope.selectTransferOfCareItem = function (selectedIndex, type) {
          if ($scope.selectedItems[type].indexOf(selectedIndex) !== -1) {
            $scope.selectedItems[type].splice($scope.selectedItems[type].indexOf(selectedIndex), 1);
          }
          else {
            $scope.selectedItems[type].push(selectedIndex);
          }
        };

        $scope.isItemSelected = function (index, type) {
          return $scope.selectedItems[type].indexOf(index) !== -1 ? 'transfer-of-care-green' : 'transfer-of-care-red';
        };

        $scope.isItemSelectedIcon = function (index, type) {
          return $scope.selectedItems[type].indexOf(index) !== -1 ? 'glyphicon glyphicon-ok' : 'glyphicon glyphicon-remove';
        };

        $scope.getSelectedItItemsForSummary = function (transferOfCareForm) {
          $scope.formSubmitted = true;

          if (transferOfCareForm.$valid) {
            $scope.selectedItemsForSummary = updateTransferOfCare();

            $scope.selectedAllergies = $scope.selectedItemsForSummary.allergies;
            $scope.selectedContacts = $scope.selectedItemsForSummary.contacts;
            $scope.selectedProblems = $scope.selectedItemsForSummary.problems;
            $scope.selectedMedications = $scope.selectedItemsForSummary.medications;

            $scope.transferDetail.reasonForContact = !$scope.details.reasonForContact ? 'No reason specified' : $scope.details.reasonForContact;
            $scope.transferDetail.clinicalSummary = !$scope.details.clinicalSummary ? 'No clinical summary' : $scope.details.clinicalSummary;
            $scope.transferDetail.site.siteFrom = !$scope.siteFrom ? 'No site from' : $scope.siteFrom;
            $scope.transferDetail.site.siteTo = !$scope.details.siteTo ? 'No site to' : $scope.details.siteTo;
            $scope.transferDetail.site.patientId = $scope.patientId;

            $scope.toggleDetailView();
          }
        };

        function updateTransferOfCare() {
          var updatedTransferOfCare = jQuery.extend(true, {},
            $scope.transferOfCare.allergies,
            $scope.transferOfCare.contacts,
            $scope.transferOfCare.medication,
            $scope.transferOfCare.problems);

          delete updatedTransferOfCare.compositionId;

          for (var type in $scope.selectedItems) {
            switch (type) {
              case 'allergies':
                    break;
              case 'contacts':
                    break;
              case 'medications':
                    break;
              case 'problems':
                for (var transferIndex = updatedTransferOfCare[type].length; transferIndex--;) {
                  var contains = false;

                  angular.forEach($scope.selectedItems[type], function (value) {
                    if (transferIndex === value) {
                      contains = true;
                    }
                  });

                  if (contains === false) {
                    updatedTransferOfCare[type].splice(transferIndex, 1);
                  }
                }
                break;
            }
          }
          return updatedTransferOfCare;
        }

        $scope.displayDetail = false;
        $scope.toggleDetailView = function () {
          $scope.displayDetail = !$scope.displayDetail;
        };

        $scope.dismiss = function () {
          $scope.$dismiss();

          $state.go('transferOfCare', {
            patientId: $scope.patient.id
          });
        };

        $scope.ok = function () {
          $scope.transferOfCare = updateTransferOfCare();

          $scope.transferOfCare.medication = $scope.transferOfCare.medications;
          delete $scope.transferOfCare.medications;
          $scope.transferOfCare.transferDetail = $scope.transferDetail;

          angular.forEach($scope.transferOfCare.allergies, function (value, key) {
            $scope.transferOfCare.allergies[key] =
            {
              allergy: value.cause,
              source: value.source,
              sourceId: value.sourceId
            };
          });

          angular.forEach($scope.transferOfCare.contacts, function (value, key) {
            $scope.transferOfCare.contacts[key] =
            {
              contactName: value.name,
              source: value.source,
              sourceId: value.sourceId
            };
          });

          angular.forEach($scope.transferOfCare.medication, function (value, key) {
            $scope.transferOfCare.medication[key] =
            {
              medication: value.name,
              source: value.source,
              sourceId: value.sourceId
            };
          });

          var todayDate = new Date();
          var toAdd = {
            allergies: $scope.transferOfCare.allergies,
            contacts: $scope.transferOfCare.contacts,
            medications: $scope.transferOfCare.medication,
            problems: $scope.transferOfCare.problems,
            clinicalSummary: $scope.transferDetail.clinicalSummary,
            dateOfTransfer: todayDate,
            reasonForContact: $scope.transferDetail.reasonForContact,
            siteFrom: 'WORCESTERSHIRE HEALTH AND CARE NHS TRUST',
            siteTo: $scope.details.siteTo,
            source: 'openehr',
            sourceId: ''
          };

          TransferOfCare.create($scope.patient.id, toAdd).then(function () {
            $scope.$close();
          });

          $scope.$close();
        };

      }
    }).result.then(function () {
        confirmTransferOfCareSelections();
      });

    function confirmTransferOfCareSelections() {
      $modal.open({
        templateUrl: 'views/transfer-of-care/transfer-of-care-confirmation.html',
        size: 'md',
        controller: function ($scope) {
          $scope.ok = function () {
            $scope.$close();
          };
        }
      }).result.finally(function () {
          $state.go('transferOfCare', {
            patientId: $scope.patient.id
          });
        });
    }

  });
