'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Eolcareplan) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Eolcareplan.all($stateParams.eolcareplanId).then(function (result) {
      $scope.result = result.data;
      $scope.eolcareplan = $scope.result[0].eolCarePlans[$stateParams.eolcareplansIndex];
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/care-plans/eolcareplans-modal.html',
        size: 'lg',
        controller: 'EolcareplansModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Care Plan'
            };
          },
          eolcareplan: function () {
            return angular.copy($scope.eolcareplan);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (eolcareplan) {
        $scope.result[0].eolCarePlans[$stateParams.eolcareplansIndex] = eolcareplan;
          
        var toUpdate = {
         compositionId : $scope.result[0].compositionId,
         eolCarePlans : $scope.result[0].eolCarePlans     
        }; 
          
        Eolcareplan.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/eolcareplans');
        });
      });
    };

  });
