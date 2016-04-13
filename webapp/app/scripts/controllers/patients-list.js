'use strict';

angular.module('rippleDemonstrator')
  .controller('PatientsListCtrl', function ($scope, $state, $stateParams, $location, $modal, PatientService) {

    if ($stateParams.patientsList.length == 0 && !$stateParams.displayEmptyTable) {
      PatientService.all().then(function (patients) {
        $scope.patients = patients;
      });

      $scope.order = $stateParams.order || 'name';
      $scope.reverse = $stateParams.reverse === 'true';
      $scope.filters = {
        department: $stateParams.department,
        ageRange: $stateParams.ageRange
      };
    }
    else {
      $scope.patients = $stateParams.patientsList;
      $location.url($location.path());

      $scope.filters = {
        advancedSearch: true,
        advancedSearchParams: $stateParams.advancedSearchParams
      };
    }

    $scope.sort = function (field) {
      var reverse = $scope.reverse;

      if ($scope.order === field) {
        reverse = !reverse;
      }

      $state.transitionTo($state.current, _.extend($stateParams, {order: field, reverse: reverse}));
    };

    $scope.sortClass = function (field) {
      if ($scope.order === field) {
        return $scope.reverse ? 'sort-desc' : 'sort-asc';
      }
    };

    $scope.go = function (patient) {
      $state.go('patients-summary', {
        patientId: patient.id,
        patientsList: $scope.patients
      });
    };

    $scope.patientFilter = function (patient) {
      if ($scope.filters.department) {
        return (patient.department === $scope.filters.department);
      }

      if ($scope.filters.ageRange) {
        return (patient.ageRange === $scope.filters.ageRange);
      }

      return true;
    };

    $scope.openAdvancedSearch = function () {
      var modalInstance = $modal.open({
        templateUrl: 'views/search/advanced-search-modal.html',
        size: 'lg',
        controller: 'AdvancedSearchController',
        resolve: {
          modal: function () {
            return {
              title: 'Advanced Search'
            };
          },
          searchParams: function () {
            return $stateParams.advancedSearchParams;
          }
        }
      });
    };
  });
