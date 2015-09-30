'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsLookupCtrl', function ($modal, $state) {

    $modal.open({
      templateUrl: 'views/patients/patients-lookup.html',
      size: 'lg',
      controller: function ($scope, SmspLookup, PatientService) {

        $scope.search = function (firstname, lastname) {
          $scope.searching = true;

          SmspLookup.byName(firstname, lastname).then(function (result) {
            $scope.patients = result;
            $scope.searching = false;
          }, function () {
            $scope.searching = false;
          });
        };

        $scope.create = function (patient) {
          PatientService.create(patient).then(function () {
            $scope.save();
          });
        };

        $scope.dismiss = function () {
          $scope.$dismiss();
        };

        $scope.save = function () {
          $scope.$close(true);
        };

      }
    }).result.finally(function () {
        $state.go('patients-charts');
      });

  });
