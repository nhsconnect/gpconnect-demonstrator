'use strict';

angular.module('openehrPocApp')
  .controller('ProceduresModalCtrl', function ($scope, $filter, $modalInstance, PatientService, patient, modal, Procedure) {

    $scope.currentUser = PatientService.getCurrentUser();
    $scope.procedure = Procedure;
    $scope.patient = patient;
    $scope.modal = modal;
    $scope.procedure.dateSubmitted = new Date();
    
    $scope.procedure.code = '1234567';
    $scope.procedure.terminology = 'ICD-10';
    
   
    
     $scope.dateofProcedureDatepicker = function ($event, name) {
      $event.preventDefault();
     $event.stopPropagation();
        
      $scope[name] = true;
    };   
    
     $scope.dateofSubmittedDatepicker = function ($event, name) {
      $event.preventDefault();
     $event.stopPropagation();
        
      $scope[name] = true;
    };
    


    $scope.ok = function (procedureForm, procedure) {
      $scope.formSubmitted = true;
      if (procedureForm.$valid) {
        $modalInstance.close(procedure);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
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
