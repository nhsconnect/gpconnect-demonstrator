'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareCtrl', function ($modal, $state, $scope, $stateParams, PatientService) {

    $modal.open({
      templateUrl: 'views/transfer-of-care/transfer-of-care.html',
      size: 'lg',
      resolve: {
        patient: function () {
          return PatientService.get($stateParams.patientId).then(function (patient) {
            $scope.patient = patient;
            return $scope.patient;
          });
        }
      },
      controller: function ($scope, patient) {

        $scope.patient = patient;

        console.log(patient);

        $scope.dismiss = function () {
          $scope.$dismiss();
        };

        $scope.save = function () {
          $scope.$close(true);
        };

      }
    }).result.finally(function () {
      $state.go('diagnoses-list', { patientId: $scope.patient.id });
    });

  });
