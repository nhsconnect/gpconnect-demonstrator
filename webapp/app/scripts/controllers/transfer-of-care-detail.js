'use strict';

angular.module('openehrPocApp')
  .controller('TransferOfCareDetailCtrl', function ($scope, $stateParams, $modal, $location, PatientService, TransferOfCare) {

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    console.log($stateParams);

    $scope.transferofCareCollection = { transferOfCares :[] };

    TransferOfCare.get($stateParams.patientId).then(function (result) {
      $scope.result = result.data;

      // Ask Ian for collection of transferOfCares
      $scope.transferofCareCollection.transferOfCares.push($scope.result);

      $scope.transferOfCare =  $scope.transferofCareCollection.transferOfCares[$stateParams.transferOfCareIndex]; // patient id
      console.log($scope.transferOfCare);
    });

    $scope.edit = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/medications/medications-modal.html',
        size: 'lg',
        controller: 'MedicationsModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'Edit Medication'
            };
          },
          medication: function () {
            return angular.copy($scope.medication);
          },
          patient: function () {
            return $scope.patient;
          }
        }
      });

      modalInstance.result.then(function (medication) {
        $scope.result.medications[$stateParams.medicationIndex] = medication;

        /*Medication.update($scope.patient.id, $scope.result).then(function (result) {
          $scope.medication = result.data;
          $location.path('/patients/' + $scope.patient.id + '/medications/' + $scope.medication.id);
        });*/
      });
    };

  });
