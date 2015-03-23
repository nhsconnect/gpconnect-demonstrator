'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareCtrl', function ($modal, $state, $scope, $stateParams, PatientService, TransferOfCare) {

    $modal.open({
      templateUrl: 'views/transfer-of-care/transfer-of-care.html',
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
        }
      },
      controller: function ($scope, patient, transferOfCare) {

        $scope.patient = patient;
        $scope.transferOfCare = transferOfCare;
        $scope.allergies = $scope.transferOfCare.allergies.allergies;
        $scope.contacts = $scope.transferOfCare.contacts.contacts;
        $scope.problems = $scope.transferOfCare.problems.problems;
        $scope.medications = $scope.transferOfCare.medication.medications;

        $scope.selectedItems = {
          problems:[],
          medications:[],
          allergies:[],
          contacts:[]
        }

        $scope.selectTransferOfCareItem = function(selectedIndex,type){
          //console.log("type passed " + type);
          //console.log("selected:" + selectedIndex);
          $scope.selectedItems[type].indexOf(selectedIndex) != -1 ? $scope.selectedItems[type].splice($scope.selectedItems[type].indexOf(selectedIndex),1) : $scope.selectedItems[type].push(selectedIndex);
          //console.log("UPDATED selected:");
          //console.log(selectedItems[type]);
        };

        $scope.isItemSelected = function(index,type){
            return $scope.selectedItems[type].indexOf(index) != -1 ? "transfer-of-care-green" : "transfer-of-care-red";
        }

        $scope.isItemSelectedIcon = function(index,type){
            return $scope.selectedItems[type].indexOf(index) != -1 ? "glyphicon glyphicon-ok" : "glyphicon glyphicon-remove";
        }

        $scope.getSelectedItItemsForSummary = function(transferOfCareForm) {

          $scope.formSubmitted = true; //is this necessary - ask phil?

          if (transferOfCareForm.$valid) { <!--Fix - display errors when invalid-->
            $scope.selectedItemsForSummary = updateTransferOfCare();
            $scope.selectedAllergies = $scope.selectedItemsForSummary.allergies.allergies;
            $scope.selectedContacts = $scope.selectedItemsForSummary.contacts.contacts;
            $scope.selectedProblems = $scope.selectedItemsForSummary.problems.problems;
            $scope.selectedMedications = $scope.selectedItemsForSummary.medication.medications;
            $scope.toggleDetailView();
          }

        };

        function updateTransferOfCare(){

        var updatedTransferOfCare = $.extend( true, {}, $scope.transferOfCare );

          for (var type in $scope.selectedItems) {

            switch (type){
              case 'allergies':
              case 'problems':
              case 'contacts':

                for (var transferIndex = updatedTransferOfCare[type][type].length; transferIndex--;) {

                  var contains = false;
                  angular.forEach($scope.selectedItems[type], function (value) {
                    //console.log("selected type: " + value);
                    var selectedItemIndex = value;

                    if (transferIndex == selectedItemIndex) {
                      contains = true;
                    }
                  });

                  if (contains === false) {
                    //console.log("Transfer does NOT contain " + transferIndex);
                    updatedTransferOfCare[type][type].splice(transferIndex, 1);
                   // console.log("splice " + transferIndex);
                  } else {
                   // console.log("Transfer does contain " + transferIndex);
                  }

                }
                    break;
              case 'medications':
                for (var transferIndex = updatedTransferOfCare['medication'][type].length; transferIndex--;) {

                  var contains = false;
                  angular.forEach($scope.selectedItems[type], function (value) {
                    var selectedItemIndex = value;

                    if (transferIndex == selectedItemIndex) {
                      contains = true;
                    }
                  });

                  if (contains === false) {
                    updatedTransferOfCare['medication'][type].splice(transferIndex, 1);
                  }

                }
                    break;
            }
          }

        return updatedTransferOfCare;
        };

        $scope.displayDetail = false;
        $scope.toggleDetailView = function() {
            $scope.displayDetail = !$scope.displayDetail;
        };

        $scope.dismiss = function () {
            $scope.$dismiss();
        };

         $scope.ok = function () {
            $scope.transferOfCare = updateTransferOfCare();
            console.log("The final transferOfCare object:");
            console.log($scope.transferOfCare);

           //update

           TransferOfCare.update($scope.patient.id ,$scope.transferOfCare ).then(function () {
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
          $state.go('diagnoses-list', { patientId: $scope.patient.id });
      });

    }

  });
