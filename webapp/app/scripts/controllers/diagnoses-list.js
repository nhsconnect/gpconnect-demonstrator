'use strict';

angular.module('openehrPocApp')
  .controller('DiagnosesListCtrl', function ($scope, $state, $stateParams, $location, $modal, PatientService, Diagnosis) {

   $scope.search = function (row) {
        return (
          angular.lowercase(row.problem).indexOf(angular.lowercase($scope.query) || '') !== -1 
       || angular.lowercase(row.dateOfOnset).indexOf(angular.lowercase($scope.query) || '') !== -1
       || angular.lowercase(row.source).indexOf(angular.lowercase($scope.query) || '') !== -1
        );
    };
    
    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Diagnosis.all($stateParams.patientId).then(function (result) {
      $scope.result = result.data;
      $scope.diagnoses = $scope.result.problems;
        for(var i = 0; i < $scope.diagnoses.length; i++){
          $scope.diagnoses[i].dateOfOnset = moment($scope.diagnoses[i].dateOfOnset).format('DD-MMM-YYYY');
      } 
    });

    $scope.go = function (path) {
      $location.path(path);
    };

    $scope.selected = function (diagnosisIndex) {
      return diagnosisIndex === $stateParams.diagnosisIndex;
    };

    $scope.create = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/diagnoses/diagnoses-modal.html',
        size: 'lg',
        controller: 'DiagnosesModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Create Problem / Diagnosis'
            };
          },
          diagnosis: function () {
            return { };
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (diagnosis) {
        $scope.result.problems.push(diagnosis);

        Diagnosis.create($scope.patient.id, $scope.result).then(function () {
          $state.go('diagnoses-list', { patientId: $scope.patient.id });
        });
      });
    };
  });
