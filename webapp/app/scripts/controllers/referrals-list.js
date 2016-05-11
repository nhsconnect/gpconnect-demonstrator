'use strict';

angular.module('gpConnect')
  .controller('ReferralsListCtrl', function ($scope, $location, $stateParams, $sce, $modal, $state, usSpinnerService, PatientService, Referral) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    $scope.search = function (row) {
      return (
        angular.lowercase(row.dateOfReferral).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.referralFrom).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.referralTo).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
        angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
      );
    };

    PatientService.findDetails($stateParams.patientId).then(function (patient) {
      $scope.patient = patient.data;
    });

    if ($stateParams.filter) {
      $scope.query = $stateParams.filter;
    }

    Referral.findAllHTMLTables($stateParams.patientId).then(function (result) {
      if (result.data.length == 0) {
        var text = '[{"sourceId":"1","source":"Legacy","provider":"No Data","html":"No referrals data available for this patient."}]';
        $scope.referralTables = JSON.parse(text);
      } else {
        $scope.referralTables = result.data;
      }

      for (var i = 0; i < $scope.referralTables.length; i++) {
         $scope.referralTables[i].html = $sce.trustAsHtml($scope.referralTables[i].html);
      }

      usSpinnerService.stop('patientSummary-spinner');
    });

    $scope.go = function (id) {
      $state.go('referrals-detail', {
        patientId: $scope.patient.id,
        referralId: id,
        filter: $scope.query,
        page: $scope.currentPage
      });
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
          author: referral.author,
          clinicalSummary: referral.clinicalSummary,
          dateCreated: new Date(referral.dateCreated),
          dateOfReferral: referral.dateOfReferral,
          reason: referral.reason,
          referralFrom: referral.referralFrom,
          referralTo: referral.referralTo
        };

        Referral.create($scope.patient.id, toAdd).then(function () {
          setTimeout(function () {
            $state.go('referrals', {
              patientId: $scope.patient.id,
              filter: $scope.query,
              page: $scope.currentPage
            }, {
              reload: true
            });
          }, 2000);
        });
      });
    };

  });
