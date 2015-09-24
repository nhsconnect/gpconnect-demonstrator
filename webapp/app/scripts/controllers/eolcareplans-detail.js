'use strict';

angular.module('openehrPocApp')
  .controller('EolcareplansDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, Eolcareplan) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Eolcareplan.get($stateParams.patientId, $stateParams.eolcareplansIndex).then(function (result) {
      $scope.eolcareplan = result.data;
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/care-plans/eolcareplans-modal.html',
        size: 'lg',
        controller: 'EolcareplansModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit End of Life Care Document'
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
        eolcareplan.careDocument.type = eolcareplan.careDocument.type.type;
        eolcareplan.careDocument.dateCreated = new Date(eolcareplan.careDocument.dateCreated);
        eolcareplan.cprDecision.dateOfDecision = new Date(eolcareplan.cprDecision.dateOfDecision);
        eolcareplan.treatmentDecision.dateOfDecision = new Date(eolcareplan.treatmentDecision.dateOfDecision);

        var toUpdate = {
         sourceId : eolcareplan.sourceId,
         careDocument : eolcareplan.careDocument,
         cprDecision : eolcareplan.cprDecision,
         prioritiesOfCare : eolcareplan.prioritiesOfCare,
         source : 'openehr',
         treatmentDecision : eolcareplan.treatmentDecision
        };

        Eolcareplan.update($scope.patient.id, toUpdate).then(function () {
          $location.path('/patients/' + $scope.patient.id + '/eolcareplans');
        });
      });
    };

  });
