'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Referral) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Referral.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
            if(result.data.length > 0){
      $scope.referrals = $scope.result[0].referrals;
        }
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (referralIndex) {
      return referralIndex === $stateParams.referralIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/referrals/referrals-modal.html',
        size: 'lg',
        controller: 'ReferralsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Referral'
            };
          },
          referral: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (referral) {

            referral.source = 'OpenEHR';  
          if($scope.result.length > 0){
            $scope.result[0].push(referral);
            Referral.update($scope.patient.id, $scope.result[0]).then(function () {
            $state.go('referrals', { patientId: $scope.patient.id });
            });
          }else{
           var toAdd = {
         compositionId : '',
         referrals : [referral]   
        }; 

        Referral.create($scope.patient.id, toAdd).then(function () {
          $state.go('referrals', { patientId: $scope.patient.id });
        });
        }
          
          
      });
    };

  });
