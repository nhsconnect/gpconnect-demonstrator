'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Eolcareplan) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Eolcareplan.all($stateParams.eolcareplanId).then(function (result) {
      $scope.result = result.data;
      $scope.eolcareplan = $scope.result.eolCarePlans[$stateParams.eolcareplansIndex];
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
          contact: function () {
            return angular.copy($scope.eolcareplan);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (eolcareplan) {
        $scope.result.eolcareplans[$stateParams.eolcareplansIndex] = eolcareplan;

        Eolcareplan.create($scope.patient.id, $scope.result).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/eolcareplans');
        });
      });
    };

  });
