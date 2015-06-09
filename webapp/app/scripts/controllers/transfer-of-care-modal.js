'use strict';/*jshint loopfunc: true*/
angular.module('openehrPocApp')
    .controller('TransferOfCareCtrl', function ($modal, $state, $scope, $stateParams, PatientService, TransferOfCare) {

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
          transferOfCare: function () {
            return TransferOfCare.get($stateParams.patientId).then(function (result) {
              $scope.transferOfCare = result.data;
              return $scope.transferOfCare;
            });
          },

          transferOfCareCompositions: function () {
            return TransferOfCare.getComposition($stateParams.patientId).then(function (result) {
              $scope.transferOfCareCompositions = result.data;
              return $scope.transferOfCareCompositions;
            });
          }
        },
        controller: function ($scope, patient, transferOfCare, transferOfCareCompositions) {

          $scope.patient = patient;
          $scope.transferOfCare = transferOfCare;
          $scope.allergies = $scope.transferOfCare.allergies.allergies;
          $scope.contacts = $scope.transferOfCare.contacts.contacts;
          $scope.problems = $scope.transferOfCare.problems.problems;
          $scope.medications = $scope.transferOfCare.medication.medications;
          $scope.transferOfCareComposition = transferOfCareCompositions;
          $scope.siteFrom = $scope.transferOfCareComposition.transfers[0].transferDetail.site.siteFrom;

          $scope.selectedItems = {
            allergies: [],
            contacts: [],
            medications: [],
            problems: []
          };

          $scope.transferDetail = {
            site : {}
          };

          $scope.selectTransferOfCareItem = function (selectedIndex, type) {

            if ($scope.selectedItems[type].indexOf(selectedIndex) !== -1) {
              $scope.selectedItems[type].splice($scope.selectedItems[type].indexOf(selectedIndex), 1);
            } else {
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
              $scope.transferDetail.clinicalSummary = !$scope.details.clinicalSummary  ? 'No clinical summary' : $scope.details.clinicalSummary;
              $scope.transferDetail.site.siteFrom = !$scope.siteFrom  ? 'No site from' : $scope.siteFrom;
              $scope.transferDetail.site.siteTo = !$scope.details.siteTo  ? 'No site to' : $scope.details.siteTo;
              $scope.transferDetail.site.patientId = 9999999000;
              $scope.toggleDetailView();
            }

          };

          function updateTransferOfCare () {

            var updatedTransferOfCare = jQuery.extend(true, {},
              $scope.transferOfCare.allergies,
              $scope.transferOfCare.contacts,
              $scope.transferOfCare.medication,
              $scope.transferOfCare.problems);

            delete updatedTransferOfCare.compositionId;

            for (var type in $scope.selectedItems) {

              switch (type) {
                case 'allergies':
                case 'contacts':
                case 'medications':
                case 'problems':

                  for (var transferIndex = updatedTransferOfCare[type].length; transferIndex--;) {
                    var contains = false;
                    angular.forEach($scope.selectedItems[type], function (value) {
                      var selectedItemIndex = value;

                      if (transferIndex === selectedItemIndex) {
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
            $scope.transferOfCareComposition.transfers.push($scope.transferOfCare);
            console.log($scope.transferOfCareComposition);
            TransferOfCare.create($scope.patient.id, $scope.transferOfCareComposition).then(function () {
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
