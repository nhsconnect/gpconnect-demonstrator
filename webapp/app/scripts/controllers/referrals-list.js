'use strict';

angular.module('rippleDemonstrator')
  .controller('ReferralsListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Referral) {

    $scope.search = function (row) {
      return (
        angular.lowercase(row.dateOfReferral).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.referralFrom).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.referralTo).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Referral.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;

      if (result.data.length > 0) {
        $scope.referrals = $scope.result;

        for (var i = 0; i < $scope.referrals.length; i++) {
          $scope.referrals[i].dateOfReferral = moment($scope.referrals[i].dateOfReferral).format('DD-MMM-YYYY');
        }
      }
    });

    $scope.go = function (id) {
       $state.go('referrals-detail', { patientId: $scope.patient.id, referralId: id, filter: $scope.query });
    };

    $scope.selected = function (referralId) {
      return referralId === $stateParams.referralId;
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
            return {};
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (referral) {
        referral.dateOfReferral = new Date(referral.dateOfReferral);
        referral.dateOfReferral.setMinutes(referral.dateOfReferral.getMinutes() - referral.dateOfReferral.getTimezoneOffset());

        var toAdd = {
          sourceId: '',
          author: referral.author,
          clinicalSummary: referral.clinicalSummary,
          dateCreated: new Date(referral.dateCreated),
          dateOfReferral: referral.dateOfReferral,
          reason: referral.reason,
          referralFrom: referral.referralFrom,
          referralTo: referral.referralTo,
          source: 'openehr'
        };

        Referral.create($scope.patient.id, toAdd).then(function () {
          $state.go('referrals', { patientId: $scope.patient.id });
        });
      });
    };

  });
