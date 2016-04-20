'use strict';

angular
  .module('gpConnect', [
    'ngResource',
    'ngTouch',
    'ngAnimate',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'growlNotifications',
    'angularUtils.directives.dirPagination',
    'ui.timepicker',
    'ui.calendar',
    'angularSpinner'
  ])
  .config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider
      .state('patients-list', {
        url: '/patients?ageRange&department&order&reverse',
        views: {
          main: { templateUrl: 'views/patients/patients-list.html', controller: 'PatientsListCtrl' }
        },
        params: { patientsList: [], advancedSearchParams: [], displayEmptyTable: false }
      })

      .state('main-search', {
        url: '/search',
        views: {
          main: { templateUrl: 'views/main-search/main-search.html', controller: 'MainSearchController' }
        }
      })

      .state('search-report', {
         url: '/search-report?searchString',
        views: {
          main: { templateUrl: 'views/search/report-chart.html', controller: 'ReportChartsCtrl' }
        }
      })

      .state('patients-lookup', {
        url: '/lookup',
        views: {
          actions: { templateUrl: 'views/home-sidebar.html' },
          main: { controller: 'PatientsLookupCtrl' }
        }
      })

      .state('patients-summary', {
        url: '/patients/{patientId:int}/patients-summary',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/patients/patients-summary.html', controller: 'PatientsSummaryCtrl' }
        }
      })

      .state('diagnoses-list', {
        url: '/patients/{patientId:int}/diagnoses',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/diagnoses/diagnoses-list.html', controller: 'DiagnosesListCtrl' }
        }
      })

      .state('diagnoses-detail', {
        url: '/patients/{patientId:int}/diagnoses/{diagnosisIndex}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/diagnoses/diagnoses-list.html', controller: 'DiagnosesListCtrl' },
          detail: { templateUrl: 'views/diagnoses/diagnoses-detail.html', controller: 'DiagnosesDetailCtrl' }
        }
      })

      .state('allergies', {
        url: '/patients/{patientId:int}/allergies',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/allergies/allergies-list.html', controller: 'AllergiesListCtrl' }
        }
      })

      .state('allergies-detail', {
        url: '/patients/{patientId:int}/allergies/{allergyIndex}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/allergies/allergies-list.html', controller: 'AllergiesListCtrl' },
          detail: { templateUrl: 'views/allergies/allergies-detail.html', controller: 'AllergiesDetailCtrl' }
        }
      })

      .state('medications', {
        url: '/patients/{patientId:int}/medications',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/medications/medications-list.html', controller: 'MedicationsListCtrl' }
        }
      })

      .state('medications-detail', {
        url: '/patients/{patientId:int}/medications/{medicationIndex}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/medications/medications-list.html', controller: 'MedicationsListCtrl' },
          detail: { templateUrl: 'views/medications/medications-detail.html', controller: 'MedicationsDetailCtrl' }
        }
      })

    .state('cancerMdt', {
      url: '/patients/{patientId:int}/cancer-mdt-list',
      views: {
        'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
        actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
        main: { templateUrl: 'views/cancer-mdt/cancermdt-list.html', controller: 'CancerMdtListCtrl' }
      }
    })

      .state('cancerMdt-detail', {
        url: '/patients/{patientId:int}/cancer-mdt-detail/{cancerMdtIndex}?filter',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/cancer-mdt/cancermdt-list.html', controller: 'CancerMdtListCtrl' },
          detail: { templateUrl: 'views/cancer-mdt/cancer-mdt-detail.html', controller: 'CancerMdtDetailCtrl' }
        }
      })

     .state('procedures', {
        url: '/patients/{patientId:int}/procedures',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/procedures/procedures-list.html', controller: 'ProceduresListCtrl' }
        }
      })

      .state('procedures-detail', {
        url: '/patients/{patientId:int}/procedures/{procedureId}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/procedures/procedures-list.html', controller: 'ProceduresListCtrl' },
          detail: { templateUrl: 'views/procedures/procedures-detail.html', controller: 'ProceduresDetailCtrl' }
        }
      })

    .state('referrals', {
        url: '/patients/{patientId:int}/referrals',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/referrals/referrals-list.html', controller: 'ReferralsListCtrl' }
        }
      })

      .state('referrals-detail', {
        url: '/patients/{patientId:int}/referrals/{referralId}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/referrals/referrals-list.html', controller: 'ReferralsListCtrl' },
          detail: { templateUrl: 'views/referrals/referrals-detail.html', controller: 'ReferralsDetailCtrl' }
        }
      })

    .state('eolcareplans', {
        url: '/patients/{patientId:int}/eolcareplans',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/care-plans/eolcareplans-list.html', controller: 'EolcareplansListCtrl' }
        }
      })

      .state('eolcareplans-detail', {
        url: '/patients/{patientId:int}/eolcareplans/{eolcareplansIndex}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/care-plans/eolcareplans-list.html', controller: 'EolcareplansListCtrl' },
          detail: { templateUrl: 'views/care-plans/eolcareplans-detail.html', controller: 'EolcareplansDetailCtrl' }
        }
      })

       .state('results', {
        url: '/patients/{patientId:int}/results',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/results/results-list.html', controller: 'ResultsListCtrl' }
        }
      })

      .state('results-detail', {
        url: '/patients/{patientId:int}/results/{resultIndex}?filter&source',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          actions: { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          main: { templateUrl: 'views/results/results-list.html', controller: 'ResultsListCtrl' },
          detail: { templateUrl: 'views/results/results-detail.html', controller: 'ResultsDetailCtrl' }
        }
      })
  })

  .directive('datepickerPopup', function () {
    return {
      restrict: 'EAC',
      require: 'ngModel',
      link: function (scope, element, attr, controller) {
        controller.$formatters.unshift();
      }
    };
  })

  .directive('mySpace', function () {
    return function (scope, element, attrs) {
      element.bind('keydown keypress', function (event) {
        if (event.which === 32) {
          scope.$apply(function () {
            scope.$eval(attrs.myEnter);
          });

          event.preventDefault();
        }
      });
    };
  })

  .directive('autoFocus', function($timeout) {
    return {
      restrict: 'A',
      require: 'ngModel',
      scope : {
        ngModel: '='
      },
      link: function(scope, elem, attrs, ctrl) {
        scope.$watch("ngModel", function (value) {
          $timeout(function () {
            elem[0].focus();
          });
        });
      }
    };
  })

  .directive('isValidNhsNumber', function() {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function(scope, elem, attrs, ctrl) {
        scope.$watch(attrs.ngModel, function(value) {
          checkFormat(value);
        });

        ctrl.$parsers.unshift(function(value) {
          return checkFormat(value);
        });

        var checkFormat = function(value) {
          // Strip white space
          if(value) {
            var nhsNum = value.replace(/\s+/g, '');
            var valid = !isNaN(nhsNum) && nhsNum.length === 10;

            ctrl.$setValidity('invalidNHSNumFormat', valid);

            return valid ? nhsNum : '';
          }
        }
      }
    }
  })

  .filter('formatNHSNumber', function() {
    return function(number) {
      if (number === undefined) {
        return;
      }

      return number.slice(0,3) + " " + number.slice(3,6) + " " + number.slice(6);
    };
  })

  .constant('keyCodes', {
    esc: 27,
    enter: 13
  })

  .directive('keyBind', ['keyCodes', function (keyCodes) {
    function map(obj) {
      var mapped = {};
      for (var key in obj) {
        var action = obj[key];
        if (keyCodes.hasOwnProperty(key)) {
          mapped[keyCodes[key]] = action;
        }
      }
      return mapped;
    }

    return function (scope, element, attrs) {
      var bindings = map(scope.$eval(attrs.keyBind));
      element.bind('keydown keypress', function (event) {
        if (bindings.hasOwnProperty(event.which)) {
          scope.$apply(function () {
            scope.$eval(bindings[event.which]);
          });
        }
      });
    };
  }])

  .directive('focusElement', function($timeout) {
    return {
      link: function(scope, element, attrs) {
        scope.$watch(attrs.focusElement, function(value) {
          $timeout(function() {
            if(value === true) {
              element.focus();
            }
          });
        });
      }
    }
  })

  .directive('rpOnLoad', ['$parse', function ($parse) {
    return function (scope, elem, attrs) {
      var fn = $parse(attrs.rpOnLoad);

      elem.on('load', function (event) {
        scope.$apply(function () {
          fn(scope, {$event: event});
        });
      });
    };
  }])

  .config(function (datepickerConfig, datepickerPopupConfig, cfpLoadingBarProvider) {
    datepickerConfig.startingDay = 1;
    datepickerPopupConfig.showButtonBar = false;
    datepickerPopupConfig.datepickerPopup = 'dd-MMM-yyyy';
    cfpLoadingBarProvider.includeSpinner = false;
  })

  .config(function (paginationTemplateProvider) {
    paginationTemplateProvider.setPath('views/dirPagination.tpl.html');
  });
