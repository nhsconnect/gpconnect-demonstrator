'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtModalCtrl', function ($scope, $modalInstance, $stateParams, $state, cancerMdt, patient, modal) {

    $scope.cancerMdt = cancerMdt;

      // Temporary add participants
      $scope.participation = [
     {
       "name" : "Dr. Marcus Johnson",
       "function" : "Oncologist",
       "mode" : "face-to-face communication",
       "id" : "1345678"
     },
     {
       "name" : "Heather Smith",
       "function" : "McMillan Nurse",
       "mode" : "face-to-face communication",
       "id" : "365672345"
     }
     ];

    $scope.patient = patient;
    $scope.modal = modal;
    $scope.cancerMdtSelectionNumber = $stateParams.cancerMdtIndex + 1;
    $scope.ok = function (cancerMdtForm, cancerMdt) {
      $scope.formSubmitted = true;
      if (cancerMdtForm.$valid) {
        $modalInstance.close(cancerMdt);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.addNewParticipant = function() {
      $scope.participation.push({
          "name" : "",
          "function" : "",
          "mode" : ""
      });
    };

    $scope.removeParticipant = function(index) {
      $scope.participation.splice(index, 1);
    };

    $scope.validate = function(form, name, index){
      var errorToCheckFor = name + index;

      for(var error in form.$error.required){
        var errorName = form.$error.required[error].$name;

        if (errorName == errorToCheckFor){
          return true;
        }
      }
    };

    $scope.validateDirty = function(form, name, index){
      var errorToCheckFor = name + index;

      if(form[errorToCheckFor].$dirty && form[errorToCheckFor].$invalid){
        return true;
      } else {
        return false;
      }
    };

    $scope.validateClean = function(form, name, index){
      var errorToCheckFor = name + index;

      if(form[errorToCheckFor].$dirty && form[errorToCheckFor].$valid){
        return true;
      } else {
        return false;
      }
    };

  });
