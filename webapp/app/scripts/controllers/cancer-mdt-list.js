'use strict';

angular.module('openehrPocApp')
  .controller('CancerMdtListCtrl', function ($scope, $location, $stateParams, $modal, $state, PatientService, CancerMdt) {

    $scope.search = function (row) {
      return (
          angular.lowercase(row.dateOfRequest).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.serviceTeam).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.dateOfMeeting).indexOf(angular.lowercase($scope.query) || '') !== -1 ||
          angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
        );
    };

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    CancerMdt.all($stateParams.patientId).then(function (result) {
      $scope.cancerMdtComposition = result.data;
      for (var i = 0; i < $scope.cancerMdtComposition.length; i++){
        $scope.cancerMdtComposition[i].dateOfRequest = moment($scope.cancerMdtComposition[i].dateOfRequest).format('DD-MMM-YYYY');
        $scope.cancerMdtComposition[i].dateOfMeeting = moment($scope.cancerMdtComposition[i].dateOfMeeting).format('DD-MMM-YYYY');
      }
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
        if (cancerMdt.timeOfMeeting !== null){
          cancerMdt.timeOfMeeting = new Date(cancerMdt.timeOfMeeting);
          cancerMdt.timeOfMeeting.setMinutes(cancerMdt.timeOfMeeting.getMinutes() - cancerMdt.timeOfMeeting.getTimezoneOffset());
        }

        cancerMdt.compositionId = '';
        cancerMdt.source = 'openehr';

        CancerMdt.create($scope.patient.id, cancerMdt).then(function () {
          $state.go('cancerMdt', { patientId: $scope.patient.id });
        });
      });
    };

  });
