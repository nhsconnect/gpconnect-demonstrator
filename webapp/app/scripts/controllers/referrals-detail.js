'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Referral) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Referral.all($stateParams.referralId).then(function (result) {
      $scope.result = result.data;
      $scope.referral = $scope.result.Referral[$stateParams.referralIndex];
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
        $scope.result.referrals[$stateParams.referralIndex] = referral;

        Contact.create($scope.patient.id, $scope.result).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/referrals');
        });
      });
    };

  });
