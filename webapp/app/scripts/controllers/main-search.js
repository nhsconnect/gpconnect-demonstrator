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
    $scope.jwtIss = ProviderRouting.getPersistentData.jwtIss;
    $scope.jwtSub = ProviderRouting.getPersistentData.jwtSub;
    $scope.jwtRequesting_practitioner = ProviderRouting.getPersistentData.jwtRequesting_practitioner;
    $scope.jwtRequesting_organization = ProviderRouting.getPersistentData.jwtRequesting_organization;
    $scope.jwtRequesting_device = ProviderRouting.getPersistentData.jwtRequesting_device;
    $scope.jwtReason_for_request = ProviderRouting.getPersistentData.jwtReason_for_request;

    $scope.openAdvancedSearch = AdvancedSearch.openAdvancedSearch;

    $scope.cancelSearchMode = function () {
      $scope.searchExpression = '';
    };

    $scope.searchFunction = function (expression) {
      var nhsNumber = expression.replace(/\s+/g, '');

      if (!isNaN(nhsNumber) && nhsNumber.length == 10) {

        usSpinnerService.spin('search-spinner');

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
        ProviderRouting.setPersistentData("jwtIss", $scope.jwtIss);
        ProviderRouting.setPersistentData("jwtSub", $scope.jwtSub);
        ProviderRouting.setPersistentData("jwtRequesting_practitioner", $scope.jwtRequesting_practitioner);
        ProviderRouting.setPersistentData("jwtRequesting_organization", $scope.jwtRequesting_organization);
        ProviderRouting.setPersistentData("jwtRequesting_device", $scope.jwtRequesting_device);
        ProviderRouting.setPersistentData("jwtReason_for_request", $scope.jwtReason_for_request);
        usSpinnerService.stop('search-spinner');
        $scope.saveBtnText = "Saved";
    };
    $scope.btnReset = function () {
        $scope.saveBtnText = "Save";
    };
    
  });
