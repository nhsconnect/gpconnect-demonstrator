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

        var selectedItems = {
          problems:[],
          medications:[],
          allergies:[],
          contacts:[]
        }

        $scope.selecteditemzzz = selectedItems; //REMOVE THIS

        $scope.selectTransferOfCareItem = function(selectedIndex,type){
          //console.log("type passed " + type);
          //console.log("selected:" + selectedIndex);
          selectedItems[type].indexOf(selectedIndex) != -1 ? selectedItems[type].splice(selectedItems[type].indexOf(selectedIndex),1) : selectedItems[type].push(selectedIndex);
          //console.log("UPDATED selected:");
          //console.log(selectedItems[type]);
        };

        $scope.isItemSelected = function(index,type){
        return selectedItems[type].indexOf(index) != -1 ? "transfer-of-care-green" : "transfer-of-care-red";
        }

        $scope.isItemSelectedIcon = function(index,type){
          return selectedItems[type].indexOf(index) != -1 ? "glyphicon glyphicon-ok" : "glyphicon glyphicon-remove";
        }

        function updateTransferOfCare(){

          for (var type in selectedItems) {

            switch (type){
              case 'allergies':
              case 'problems':
              case 'contacts':

                for (var transferIndex = $scope.transferOfCare[type][type].length; transferIndex--;) {

                  var contains = false;
                  angular.forEach(selectedItems[type], function (value) {
                    //console.log("selected type: " + value);
                    var selectedItemIndex = value;

                    if (transferIndex == selectedItemIndex) {
                      contains = true;
                    }
                  });

                  if (contains === false) {
                    //console.log("Transfer does NOT contain " + transferIndex);
                    $scope.transferOfCare[type][type].splice(transferIndex, 1);
                   // console.log("splice " + transferIndex);
                  } else {
                   // console.log("Transfer does contain " + transferIndex);
                  }

                }
                    break;
              case 'medications':
                for (var transferIndex = $scope.transferOfCare['medication'][type].length; transferIndex--;) {

                  var contains = false;
                  angular.forEach(selectedItems[type], function (value) {
                    var selectedItemIndex = value;

                    if (transferIndex == selectedItemIndex) {
                      contains = true;
                    }
                  });

                  if (contains === false) {
                    $scope.transferOfCare['medication'][type].splice(transferIndex, 1);
                  }

                }
                    break;
            }
          }
        return $scope.transferOfCare;
        }

       /* $scope.dismiss = function () {
          $scope.$dismiss();
        };

        $scope.save = function () {
          $scope.$close(true);
        };*/

        $scope.ok = function (form, toc) {

          $scope.formSubmitted = true;
          if (form.$valid) {

            $scope.$close(toc);

             var result = updateTransferOfCare();
             console.log(result);
            //alert("returned!")

          }
        };

      }
    }).result.then(function () {

        confirmTransferOfCareSelections();

    });

    function confirmTransferOfCareSelections() {
      alert("confirmTransferOfCareSelections called");

      $modal.open({
        templateUrl: 'views/transfer-of-care/transfer-of-care-confirmation.html',
        size: 'md',
        controller: function ($scope) {

          $scope.ok = function () {
            $scope.close();
          };

          $scope.cancel = function () {
            alert("needs to go back to first modal");
          };

        }
      }).result.finally(function () {
          $state.go('diagnoses-list', { patientId: $scope.patient.id });
      });

    }

  });
