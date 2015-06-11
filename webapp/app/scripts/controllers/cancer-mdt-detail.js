'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtDetailCtrl', function ($scope, $stateParams, $modal, $location, $state, PatientService, CancerMdt) {

    var rawComposition;

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    CancerMdt.getComposition($stateParams.patientId).then(function (result) {
      $scope.cancerMdtComposition = result.data;
      $scope.cancerMdt = $scope.cancerMdtComposition.cancerMDT[$stateParams.cancerMdtIndex];
      rawComposition = $scope.cancerMdtComposition.cancerMDT[0].rawComposition;
      $scope.cancerMdtSelectionNumber = $stateParams.cancerMdtIndex + 1;

      // Temporary add participants
      var participants = ['Joe Hughes', 'Lucy Jones', 'Bob Lowe'];
      $scope.cancerMdt.participants = participants;

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
        // temporary remove all hardcoded participants
        delete cancerMdt['participants'];
        cancerMdt.rawComposition = rawComposition;
        $scope.cancerMdtComposition.cancerMDT[$stateParams.cancerMdtIndex] = cancerMdt;

        // temporary remove all hardcoded participants
          for(var people in $scope.cancerMdtComposition.cancerMDT){
            delete people['participants'];
           }
        console.log('to post');
        console.log($scope.cancerMdtComposition);
        CancerMdt.update($scope.patient.id,$scope.cancerMdtComposition).then(function () {
          $state.go('cancerMdt', { patientId: $scope.patient.id });
        });
      });
    };

  });
