'use strict';

angular.module('openehrPocApp')
  .controller('PatientsLookupCtrl', function ($modal, $state) {

    $modal.open({
      templateUrl: 'views/patients/patients-lookup.html',
      size: 'lg',
      controller: function ($scope, PatientLookup) {

        $scope.search = function (firstname, lastname) {
          $scope.searching = true;

          PatientLookup.bySearch(firstname, lastname).then(function (result) {
            $scope.patients = result;
            $scope.searching = false;
          }, function () {
            $scope.searching = false;
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
