'use strict';

/**
 * @ngdoc overview
 * @name openehrPocApp
 * @description
 * # openehrPocApp
 *
 * Main module of the application.
 */
angular
  .module('openehrPocApp', [
    'ngResource',
    'ngTouch',
    'ngSanitize',
    'ngAnimate',
    'ui.router',
    'ui.bootstrap',
    'angular-loading-bar',
    'growlNotifications',
    'angularUtils.directives.dirPagination'
  ])
  .config(function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/');

    $stateProvider
      .state('patients-list', {
        url: '/patients?ageRange&department&order&reverse',
        views: {
          'actions': { templateUrl: 'views/patients/patients-list-sidebar.html' },
          'main': { templateUrl: 'views/patients/patients-list.html', controller: 'PatientsListCtrl' }
        }
      })
      .state('patients-charts', {
        url: '/',
        views: {
          'actions': { templateUrl: 'views/patients/patients-list-sidebar.html' },
          'main': { templateUrl: 'views/patients/patients-charts.html', controller: 'PatientsChartsCtrl' }
        }
      })

      .state('diagnoses-list', {
        url: '/patients/:patientId/diagnoses',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          'actions': { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          'main': { templateUrl: 'views/diagnoses/diagnoses-list.html', controller: 'DiagnosesListCtrl' }
        }
      })

      .state('diagnoses-detail', {
        url: '/patients/:patientId/diagnoses/:diagnosisId',
        views: {
          'user-context': { templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl' },
          'actions': { templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl' },
          'main': { templateUrl: 'views/diagnoses/diagnoses-list.html', controller: 'DiagnosesListCtrl' },
          'detail': { templateUrl: 'views/diagnoses/diagnoses-detail.html', controller: 'DiagnosesDetailCtrl' }
        }
      });

  })
  .config(function (datepickerConfig, datepickerPopupConfig, cfpLoadingBarProvider) {
    datepickerConfig.startingDay          = 1;
    datepickerPopupConfig.showButtonBar   = false;
    datepickerPopupConfig.datepickerPopup = 'dd-MMM-yyyy';
    cfpLoadingBarProvider.includeSpinner  = false;
  })
  .config(function(paginationTemplateProvider) {
    paginationTemplateProvider.setPath('views/dirPagination.tpl.html');
  });
