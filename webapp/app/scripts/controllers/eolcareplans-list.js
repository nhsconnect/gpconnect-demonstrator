'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Eolcareplan) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Eolcareplan.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.eolcareplans = $scope.result[0].eolCarePlans;
    });

    $scope.go = function (index) {
      $location.path('/patients/'+$scope.patient.id+'/eolcareplans/'+index);
    };

    $scope.selected = function (eolcareplansIndex) {
      return eolcareplansIndex === $stateParams.eolcareplansIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/care-plans/eolcareplans-modal.html',
        size: 'lg',
        controller: 'EolcareplansModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Care plan'
            };
          },
          eolcareplan: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (eolcareplan) {
        $scope.result[0].eolCarePlans.push(eolcareplan);

        Eolcareplan.create($scope.patient.id, $scope.result).then(function () {
          $state.go('eolcareplans', { patientId: $scope.patient.id });
        });
      });
    };

  });
