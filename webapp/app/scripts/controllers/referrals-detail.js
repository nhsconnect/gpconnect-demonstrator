'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Referral) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Referral.all($stateParams.referralId).then(function (result) {
      $scope.result = result.data;
      $scope.referral = $scope.result[$stateParams.referralIndex];
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/referrals/referrals-modal.html',
        size: 'lg',
        controller: 'ReferralsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Referral'
            };
          },
          referral: function () {
            return angular.copy($scope.referral);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (referral) {

        var toUpdate = {
         compositionId : $scope.referral.compositionId,
         author : referral.author,
         clinicalSummary : referral.clinicalSummary,
         dateCreated: referral.dateCreated,
         dateOfReferral: referral.dateOfReferral,
         reasonForReferral: referral.reasonForReferral,
         referralFrom: referral.referralFrom,
         referralTo: referral.referralTo,
         source: 'openehr'
        };

        Referral.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/referrals');
        });
      });
    };
  });
