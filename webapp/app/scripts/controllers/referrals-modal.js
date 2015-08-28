'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsModalCtrl', function ($scope, $modalInstance, referral, PatientService, patient, modal) {
   
    $('#dateofreferral').datepicker({dateFormat: 'dd-MMM-y'});
    
    $scope.currentUser = PatientService.getCurrentUser();
    $scope.referral = referral;
    $scope.patient = patient;
    $scope.modal = modal;
    
    if(modal.title === 'Create Referral'){
        $scope.referral.dateCreated = new Date().toISOString().slice(0, 10);
        $scope.author = $scope.currentUser;
    }
    
    $scope.referralCreatedDatepicker = function ($event, name) {
     $event.preventDefault();
     $event.stopPropagation();
     $scope[name] = true;
    };
    
    $scope.dateofReferralDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();
      $scope[name] = true;
    };

    $scope.ok = function (referralForm, referral) {
      $scope.formSubmitted = true;
      if (referralForm.$valid) {
        $modalInstance.close(referral);
      }
    };

    $scope.cancel = function () {
      $modalInstance.dismiss('cancel');
    };

    $scope.openDatepicker = function ($event, name) {
      $event.preventDefault();
      $event.stopPropagation();

      $scope[name] = true;
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
