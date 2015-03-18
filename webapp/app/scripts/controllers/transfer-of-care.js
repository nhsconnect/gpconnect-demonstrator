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

        $scope.selecteditemzzz = selectedItems;

        $scope.selectTransferOfCareItem = function(selectedIndex,type){
          console.log("type passed " + type);
          console.log("selected:" + selectedIndex);

          selectedItems[type].indexOf(selectedIndex) != -1 ? selectedItems[type].splice(selectedItems[type].indexOf(selectedIndex),1) : selectedItems[type].push(selectedIndex);

          console.log("UPDATED selected:");
          console.log(selectedItems[type]);
        };

        $scope.isItemSelected = function(index,type){
        return selectedItems[type].indexOf(index) != -1 ? "green" : "red";
        }

        $scope.isItemSelectedIcon = function(index,type){
          return selectedItems[type].indexOf(index) != -1 ? "glyphicon glyphicon-ok" : "glyphicon glyphicon-remove";
        }

        $scope.dismiss = function () {
          $scope.$dismiss();
        };

        $scope.save = function () {
          $scope.$close(true);
        };

        $scope.ok = function (form, toc) {
          $scope.formSubmitted = true;
          if (form.$valid) {
            $scope.$close(toc);
          }
        };

      }
    }).result.finally(function () {
      $state.go('diagnoses-list', { patientId: $scope.patient.id });
    });

  });
