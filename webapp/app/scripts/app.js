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
            'angularSpinner',
            'ui.tree',
            'gantt',
            'gantt.sortable',
            'gantt.tooltips',
            'gantt.bounds',
            'gantt.progress',
            'gantt.table',
            'gantt.tree',
            'gantt.groups'
        ])
        .config(function ($stateProvider, $urlRouterProvider) {

            $urlRouterProvider.otherwise('/');

            $stateProvider
                    .state('patients-list', {
                        url: '/patients?ageRange&department&order&reverse',
                        views: {
                            main: {templateUrl: 'views/patients/patients-list.html', controller: 'PatientsListCtrl'}
                        },
                        params: {patientsList: [], advancedSearchParams: [], displayEmptyTable: false}
                    })

                    .state('main-search', {
                        url: '/search',
                        views: {
                            main: {templateUrl: 'views/search/main-search.html', controller: 'MainSearchController'}
                        }
                    })

                    .state('patients-summary', {
                        url: '/patients/{patientId:int}/patients-summary',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/patients/patients-summary.html', controller: 'PatientsSummaryCtrl'}
                        }
                    })

                    .state('problem-list', {
                        url: '/patients/{patientId:int}/problem',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/problem/problem-list.html', controller: 'ProblemListCtrl'}
                        }
                    })

                    .state('problem-detail', {
                        url: '/patients/{patientId:int}/problem/{problemIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/problem/problem-list.html', controller: 'ProblemListCtrl'},
                            detail: {templateUrl: 'views/problem/problem-detail.html', controller: 'ProblemDetailCtrl'}
                        }
                    })

                    .state('allergies', {
                        url: '/patients/{patientId:int}/allergies',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/allergies/allergies-list.html', controller: 'AllergiesListCtrl'}
                        }
                    })

                    .state('allergies-detail', {
                        url: '/patients/{patientId:int}/allergies/{allergyIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/allergies/allergies-list.html', controller: 'AllergiesListCtrl'},
                            detail: {templateUrl: 'views/allergies/allergies-detail.html', controller: 'AllergiesDetailCtrl'}
                        }
                    })

                    .state('medications', {
                        url: '/patients/{patientId:int}/medications',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/medications/medications-list.html', controller: 'MedicationsListCtrl'}
                        }
                    })

                    .state('medications-detail', {
                        url: '/patients/{patientId:int}/medications/{medicationIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/medications/medications-list.html', controller: 'MedicationsListCtrl'},
                            detail: {templateUrl: 'views/medications/medications-detail.html', controller: 'MedicationsDetailCtrl'}
                        }
                    })

                    .state('referrals', {
                        url: '/patients/{patientId:int}/referrals',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/referrals/referrals-list.html', controller: 'ReferralsListCtrl'}
                        }
                    })

                    .state('referrals-detail', {
                        url: '/patients/{patientId:int}/referrals/{referralId}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/referrals/referrals-list.html', controller: 'ReferralsListCtrl'},
                            detail: {templateUrl: 'views/referrals/referrals-detail.html', controller: 'ReferralsDetailCtrl'}
                        }
                    })

                    .state('observations', {
                        url: '/patients/{patientId:int}/observations',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/observations/observations-list.html', controller: 'ObservationsListCtrl'}
                        }
                    })

                    .state('results-detail', {
                        url: '/patients/{patientId:int}/results/{resultIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/results/observations-list.html', controller: 'ResultsListCtrl'},
                            detail: {templateUrl: 'views/results/observations-detail.html', controller: 'ResultsDetailCtrl'}
                        }
                    })

                    .state('encounters', {
                        url: '/patients/{patientId:int}/encounters',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/encounters/encounters-list.html', controller: 'EncountersListCtrl'}
                        }
                    })

                    .state('encounters-detail', {
                        url: '/patients/{patientId:int}/encounters/{encounterIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/encounters/encounters-list.html', controller: 'EncountersListCtrl'},
                            detail: {templateUrl: 'views/encounters/encounters-detail.html', controller: 'EncountersDetailCtrl'}
                        }
                    })

                    .state('immunisations', {
                        url: '/patients/{patientId:int}/immunisations',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/immunisations/immunisations-list.html', controller: 'ImmunisationsListCtrl'}
                        }
                    })

                    .state('immunisations-detail', {
                        url: '/patients/{patientId:int}/immunisations/{immunisationIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/immunisations/immunisations-list.html', controller: 'ImmunisationsListCtrl'},
                            detail: {templateUrl: 'views/immunisations/immunisations-detail.html', controller: 'ImmunisationsDetailCtrl'}
                        }
                    })

                    .state('adminitems', {
                        url: '/patients/{patientId:int}/adminitems',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/adminitems/adminitems-list.html', controller: 'AdminItemsListCtrl'}
                        }
                    })

                    .state('adminitems-detail', {
                        url: '/patients/{patientId:int}/adminitems/{adminItemIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/adminitems/adminitems-list.html', controller: 'AdminItemsListCtrl'},
                            detail: {templateUrl: 'views/adminitems/adminitems-detail.html', controller: 'AdminItemsDetailCtrl'}
                        }
                    })

                    .state('clinicalitems', {
                        url: '/patients/{patientId:int}/clinicalitems',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/clinicalitems/clinicalitems-list.html', controller: 'ClinicalItemsListCtrl'}
                        }
                    })

                    .state('clinicalitems-detail', {
                        url: '/patients/{patientId:int}/clinicalitems/{clinicalItemIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/clinicalitems/clinicalitems-list.html', controller: 'ClinicalItemsListCtrl'},
                            detail: {templateUrl: 'views/clinicalitems/clinicalitems-detail.html', controller: 'ClinicalItemsDetailCtrl'}
                        }
                    })

                    .state('investigations', {
                        url: '/patients/{patientId:int}/investigations',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/investigations/investigations-list.html', controller: 'InvestigationsListCtrl'}
                        }
                    })

                    .state('investigations-detail', {
                        url: '/patients/{patientId:int}/investigations/{investigationIndex}?filter&source',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/investigations/investigations-list.html', controller: 'InvestigationsListCtrl'},
                            detail: {templateUrl: 'views/investigations/investigations-detail.html', controller: 'InvestigationsDetailCtrl'}
                        }
                    })

                    .state('appointments', {
                        url: '/patients/{patientId:int}/appointments',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/appointments/appointments.html', controller: 'AppointmentsCtrl'}
                        }
                    })

                    .state('orders-sent', {
                        url: '/patients/{patientId:int}/orders/sent',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/orders/sent/orders-sent.html', controller: 'OrdersSentCtrl'}
                        }
                    })

                    .state('orders-received', {
                        url: '/patients/{patientId:int}/orders/received',
                        views: {
                            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
                            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
                            main: {templateUrl: 'views/orders/received/orders-received.html', controller: 'OrdersReceivedCtrl'}
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

        .directive('autoFocus', function ($timeout) {
            return {
                restrict: 'A',
                require: 'ngModel',
                scope: {
                    ngModel: '='
                },
                link: function (scope, elem, attrs, ctrl) {
                    scope.$watch("ngModel", function (value) {
                        $timeout(function () {
                            elem[0].focus();
                        });
                    });
                }
            };
        })

        .directive('isValidNhsNumber', function () {
            return {
                restrict: 'A',
                require: 'ngModel',
                link: function (scope, elem, attrs, ctrl) {
                    scope.$watch(attrs.ngModel, function (value) {
                        checkFormat(value);
                    });

                    ctrl.$parsers.unshift(function (value) {
                        return checkFormat(value);
                    });

                    var checkFormat = function (value) {
                        // Strip white space
                        if (value) {
                            var nhsNum = value.replace(/\s+/g, '');
                            var valid = !isNaN(nhsNum) && nhsNum.length === 10;

                            ctrl.$setValidity('invalidNHSNumFormat', valid);

                            return valid ? nhsNum : '';
                        }
                    }
                }
            }
        })

        .filter('formatNHSNumber', function () {
            return function (number) {
                if (number === undefined) {
                    return;
                }

                return number.slice(0, 3) + " " + number.slice(3, 6) + " " + number.slice(6);
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

        .directive('focusElement', function ($timeout) {
            return {
                link: function (scope, element, attrs) {
                    scope.$watch(attrs.focusElement, function (value) {
                        $timeout(function () {
                            if (value === true) {
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
        })

        .provider("ProviderRouting", function () {
            var providerRouting = {
                    "spineProxy": 	"",
                    "ASID": 		"",
                    "practices": 	[]
            };
            this.$get = function () {
                var q = jQuery.ajax({
                    type: 'GET', url: '/providerRouting.json', cache: false, async: false, contentType: 'application/json', dataType: 'json'
                });
                if (q.status === 200) {
                	 providerRouting = angular.fromJson(q.responseText);

                	 providerRouting.defaultPractice = function () {
                        var defaultPracticeOdsCode = $('#defaultPracticeOdsCode').html();
                    	for(var p = 0; p < this.practices.length; p++) {
                    		var practice = this.practices[p];
                    		if(practice.odsCode == defaultPracticeOdsCode) {
                    			return practice;
                    		}
                    	}
                    };
                }
                return providerRouting;
            };
        })
        .service('genericHttpInterceptor', function ($injector) {
            // Useful interceptor information https://docs.angularjs.org/api/ng/service/$http
            var service = this;
            service.response = function (response) {
                errorModal(response, $injector);
                return response;
            };
            service.responseError = function (errorResponse) {
                errorModal(errorResponse, $injector);
                return errorResponse;
            };
        })
        .config(['$httpProvider', function ($httpProvider) {
                $httpProvider.interceptors.push('genericHttpInterceptor');
            }]);


var errorModal = function (httpResponse, $injector) {
    var errorMsg = errorMapping(httpResponse.status);
    if (errorMsg != undefined) {
        var modal = $injector.get('$modal');
        modal.open({
            templateUrl: 'views/application/generic-error-modal.html',
            size: 'mg',
            controller: 'GenericErrorModalCtrl',
            resolve: {
                errorInfo: function () {
                    return {
                        "response": httpResponse,
                        "errorMsg" : errorMsg
                    };
                }
            }
        });
    }
};