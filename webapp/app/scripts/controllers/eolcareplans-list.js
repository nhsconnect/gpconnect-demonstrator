'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, Eolcareplan) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Eolcareplan.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
        if(result.data.length > 0){
      $scope.eolcareplans = $scope.result;
        }
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
              title: 'Create End of Life Care Document'
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
          eolcareplan.careDocument.date = new Date(eolcareplan.careDocument.date);
          var toAdd = {
         compositionId : '',
         careDocument : eolcareplan.careDocument,
         cprDecision : eolcareplan.cprDecision,
         prioritiesOfCare : eolcareplan.prioritiesOfCare,
         source : 'openehr',
         treatmentDecision : eolcareplan.treatmentDecision       
        }; 

        Eolcareplan.create($scope.patient.id, toAdd).then(function () {
          $state.go('eolcareplans', { patientId: $scope.patient.id });
        });
    });
    }
  });
