'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, TransferOfCare) {

    $scope.query = {};
    $scope.queryBy = '$';
    
    
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;    
    });

    TransferOfCare.getComposition($stateParams.patientId).then(function (result) {
      $scope.transferofCareComposition = result.data;
         for(var i = 0; i < $scope.transferofCareComposition.transfers.length; i++){
          $scope.transferofCareComposition.transfers[i].transferDetail.site.timeStamp = moment($scope.transferofCareComposition.transfers[i].transferDetail.site.timeStamp).format('DD-MMM-YYYY');
      }     
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (transferOfCareIndex) {
      console.log($stateParams);
      return transferOfCareIndex === $stateParams.transferOfCareIndex;
    };

    $scope.create = function () {
      $state.go('transferOfCare-create', {
        patientId: $scope.patient.id
      });
    };

  });
