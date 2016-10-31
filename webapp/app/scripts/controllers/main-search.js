'use strict';

angular.module('gpConnect')
  .controller('MainSearchController', function ($scope, $state, $timeout, AdvancedSearch, PatientService, ProviderRouting, usSpinnerService) {

    $scope.mainSearchEnabled = true;
    $scope.searchExpression = '';
    $scope.errorOccurred = false;
    $scope.testingPracticeODSCode = ProviderRouting.getPersistentData.testingOdsCode;
    $scope.testingFhirUrl = ProviderRouting.getPersistentData.testingFhirUrl;
    $scope.toASID = ProviderRouting.getPersistentData.toASID;
    $scope.fromASID = ProviderRouting.getPersistentData.fromASID;

    $scope.openAdvancedSearch = AdvancedSearch.openAdvancedSearch;

    $scope.cancelSearchMode = function () {
      $scope.searchExpression = '';
    };

    $scope.searchFunction = function (expression) {
      var nhsNumber = expression.replace(/\s+/g, '');

      if (!isNaN(nhsNumber) && nhsNumber.length == 10) {

        usSpinnerService.spin('search-spinner');

        PatientService.getFhirPatient(ProviderRouting.defaultPractice().odsCode, nhsNumber).then(function (patient) {
          goToPatientSummary(nhsNumber);

        }).catch(function () {
          usSpinnerService.stop('search-spinner');
          $scope.setErrorOccurred(true);
        });
      }
      else {
        usSpinnerService.stop('search-spinner');
        $scope.setErrorOccurred(true);
      }
    };

    $scope.setErrorOccurred = function (errorOccurred) {
      $timeout(function () {
        $scope.$apply(function () {
          $scope.errorOccurred = errorOccurred;
        }, 1000);
      });
    };

    $scope.populateSearchField = function (nhsNumber) {
      $timeout(function () {
        $scope.$apply(function () {
          $scope.searchExpression = nhsNumber;
        }, 1000);
      });
    };

    var goToPatientSummary = function (nhsNumber) {
      $state.go('patients-summary', {
        patientId: nhsNumber,
        filter: $scope.query
      });
    };
    
    $scope.saveBtnText = "Save";
    $scope.saveTestingConfig = function () {
        usSpinnerService.spin('search-spinner');
        ProviderRouting.setPersistentData("testingOdsCode", $scope.testingPracticeODSCode);
        ProviderRouting.setPersistentData("testingFhirUrl", $scope.testingFhirUrl);
        ProviderRouting.setPersistentData("toASID", $scope.toASID);
        ProviderRouting.setPersistentData("fromASID", $scope.fromASID);
        usSpinnerService.stop('search-spinner');
        $scope.saveBtnText = "Saved";
    };
    $scope.btnReset = function () {
        $scope.saveBtnText = "Save";
    };
    
  });
