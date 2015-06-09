'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtDetailCtrl', function ($scope, $stateParams, $modal, $location, $state, PatientService, CancerMdt) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    CancerMdt.getComposition($stateParams.patientId).then(function (result) {
      $scope.cancerMdtComposition = result.data;
      $scope.cancerMdt = $scope.cancerMdtComposition.cancerMDT[$stateParams.cancerMdtIndex];
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/cancer-mdt/cancer-mdt-modal.html',
        size: 'lg',
        controller: 'CancerMdtModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Cancer MDT'
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
        $scope.cancerMdtComposition.cancerMDT[$stateParams.cancerMdtIndex] = cancerMdt;

        CancerMdt.update($scope.patient.id,$scope.cancerMdtComposition).then(function () {
          $state.go('cancerMdt', { patientId: $scope.patient.id });
        });
      });
    };

  });
