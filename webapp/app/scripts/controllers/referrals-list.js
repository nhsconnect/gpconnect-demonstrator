'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Referral) {

    $scope.query = {};
    $scope.queryBy = '$';
    
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Referral.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
            if(result.data.length > 0){
      $scope.referrals = $scope.result;
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

          var toAdd = {
         compositionId : '',
         author : referral.author,
         clinicalSummary : referral.clinicalSummary,
         dateCreated: new Date(referral.dateCreated),
         dateOfReferral: referral.dateOfReferral,
         reasonForReferral: referral.reasonForReferral,
         referralFrom: referral.referralFrom,
         referralTo: referral.referralTo,
         source: 'openehr'  
        }; 
          
        Referral.create($scope.patient.id, toAdd).then(function () {
          $state.go('referrals', { patientId: $scope.patient.id });
        });
    });
    }
  });