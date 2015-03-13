'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareCtrl', function ($modal, $state, $scope, $stateParams, PatientService, TransferOfCare) {

    $modal.open({
      templateUrl: 'views/transfer-of-care/transfer-of-care.html',
      size: 'lg',
      resolve: {
        patient: function () {
          return PatientService.get($stateParams.patientId).then(function (patient) {
            $scope.patient = patient;
            return $scope.patient;
          });
        },
        transferOfCare: function () {
          return TransferOfCare.get($stateParams.patientId).then(function (result) {
            $scope.transferOfCare = result.data;
            return $scope.transferOfCare;
          });
        }
      },
      controller: function ($scope, patient, transferOfCare) {

        $scope.patient = patient;
        $scope.transferOfCare = transferOfCare;

        $scope.dismiss = function () {
          $scope.$dismiss();
        };

        $scope.save = function () {
          $scope.$close(true);
        };

        $scope.ok = function (form, toc) {
          $scope.formSubmitted = true;
          if (form.$valid) {
            $scope.$close(toc);
          }
        };

      }
    }).result.finally(function () {
      $state.go('diagnoses-list', { patientId: $scope.patient.id });
    });

  });
