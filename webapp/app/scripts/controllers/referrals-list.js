'use strict';

angular.module('openehrPocApp')
  .controller('ReferralsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Referral) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Referral.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.referrals = $scope.result.referrals;
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
        $scope.result.referrals.push(referral);

        Contact.create($scope.patient.id, $scope.result).then(function () {
          $state.go('referrals', { patientId: $scope.patient.id });
        });
      });
    };

  });
