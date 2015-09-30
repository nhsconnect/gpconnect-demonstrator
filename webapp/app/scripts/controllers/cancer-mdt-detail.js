'use strict';

angular.module('rippleDemonstrator')
  .controller('CancerMdtDetailCtrl', function ($scope, $stateParams, $modal, $location, $state, PatientService, CancerMdt) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    CancerMdt.get($stateParams.patientId, $stateParams.cancerMdtIndex).then(function (result) {
      $scope.cancerMdt = result.data;
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/cancer-mdt/cancer-mdt-modal.html',
        size: 'lg',
        controller: 'CancerMdtModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit MDT'
            };
          },
          cancerMdt: function () {
            return angular.copy($scope.cancerMdt);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (cancerMdt) {
        cancerMdt.dateOfMeeting = new Date(cancerMdt.dateOfMeeting);
        cancerMdt.dateOfRequest = new Date(cancerMdt.dateOfRequest);

        if (cancerMdt.timeOfMeeting !== null) {
          cancerMdt.timeOfMeeting = new Date(cancerMdt.timeOfMeeting);
        }

        CancerMdt.update($scope.patient.id, cancerMdt).then(function () {
          $state.go('cancerMdt', { patientId: $scope.patient.id });
        });
      });
    };

  });
