'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsModalCtrl', function ($scope, $modalInstance, referral, PatientService, patient, modal) {
   
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

  });
