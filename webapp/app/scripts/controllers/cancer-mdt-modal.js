'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtModalCtrl', function ($scope, $modalInstance, $stateParams, $state, cancerMdt, patient, modal) {

    $scope.cancerMdt = cancerMdt;

    console.log($scope.cancerMdt);
    if(!Object.keys($scope.cancerMdt).length || !$scope.cancerMdt.participation){
      $scope.cancerMdt.participation = [];
    }

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
      $scope.cancerMdt.participation.push({
          'name' : '',
          'function' : '',
          'mode' : ''
      });
    };

    $scope.removeParticipant = function(index) {
      $scope.cancerMdt.participation.splice(index, 1);
    };

    $scope.validate = function(form, name, index){
      var errorToCheckFor = name + index;

      for(var error in form.$error.required){
        var errorName = form.$error.required[error].$name;

        if (errorName === errorToCheckFor){
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
