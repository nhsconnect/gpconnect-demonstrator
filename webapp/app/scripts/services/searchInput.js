'use strict';

angular.module('rippleDemonstrator')
  .factory('SearchInput', function ($rootScope, $stateParams) {

    var update = function () {
      if ($stateParams.queryType === 'Setting: ') {
        $rootScope.searchMode = true;
        $rootScope.settingsMode = true;
        $rootScope.reportMode = false;
        $rootScope.patientMode = false;
        $rootScope.subHeader = $stateParams.queryType + $stateParams.searchString;
      } else if ($stateParams.queryType === 'Reports: ') {
        $rootScope.searchMode = true;
        $rootScope.reportMode = true;
        $rootScope.settingsMode = false;
        $rootScope.patientMode = false;
        $rootScope.reportTypeSet = true;
        $rootScope.reportTypeString = $stateParams.reportType;
        $rootScope.subHeader = $stateParams.queryType + $stateParams.reportType + ': ' + $stateParams.searchString + ' & Aged ' + $stateParams.ageFrom + ' to ' + $stateParams.ageTo;
      } else if ($stateParams.queryType === 'Patient: '){
        $rootScope.searchMode = true;
        $rootScope.reportMode = false;
        $rootScope.settingsMode = false;
        $rootScope.reportTypeSet = false;
        $rootScope.patientMode = true;
        $rootScope.subHeader = $stateParams.queryType + $stateParams.searchString;
      }
    };

    return {
      update: update
    };

  });
