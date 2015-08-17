'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, CancerMdt) {

    $scope.query = {};
    $scope.queryBy = '$';
    
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    CancerMdt.getComposition($stateParams.patientId).then(function (result) {
      $scope.cancerMdtComposition = result.data;
      console.log(  $scope.cancerMdtComposition );
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (cancerMdtIndex) {
      return cancerMdtIndex === $stateParams.cancerMdtIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/cancer-mdt/cancer-mdt-modal.html',
        size: 'lg',
        controller: 'CancerMdtModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Cancer MDT'
            };
          },
          cancerMdt: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (cancerMdt) {
        $scope.cancerMdtComposition.cancerMDT.push(cancerMdt);
        CancerMdt.create($scope.patient.id, $scope.cancerMdtComposition).then(function () {
          $state.go('cancerMdt', { patientId: $scope.patient.id });
        });
      });
    };

  });
