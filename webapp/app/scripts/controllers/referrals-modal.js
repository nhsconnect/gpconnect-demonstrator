'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsModalCtrl', function ($scope, $modalInstance, referral, patient, modal) {

    $scope.referral = referral;
    $scope.patient = patient;
    $scope.modal = modal;

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
