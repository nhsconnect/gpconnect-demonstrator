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
              title: 'Create MDT'
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
        cancerMdt.dateOfMeeting = new Date(cancerMdt.dateOfMeeting); 
        cancerMdt.dateOfRequest = new Date(cancerMdt.dateOfRequest);  
        if(cancerMdt.timeOfMeeting !== null){cancerMdt.timeOfMeeting = new Date(cancerMdt.timeOfMeeting);}  
        
        cancerMdt.compositionId = '';
        cancerMdt.source = "openehr";  
          
        CancerMdt.create($scope.patient.id, cancerMdt).then(function () {
          $state.go('cancerMdt', { patientId: $scope.patient.id });
        });
      });
    };

  });
