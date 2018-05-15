'use strict';

angular.module('gpConnect', [
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
]).config(function($stateProvider, $urlRouterProvider) {
    $urlRouterProvider.otherwise('/');

    $stateProvider.state('patients-list', {
        url: 'patients?ageRange&department&order&reverse',
        views: {
            main: {
                templateUrl: 'views/patients/patients-list.html',
                controller: 'PatientsListCtrl'
            }
        },
        params: {
            patientsList: [],
            advancedSearchParams: [],
            displayEmptyTable: false
        }
    }).state('main-search', {
        url: 'search',
        views: {
            main: {
                templateUrl: 'views/search/main-search.html',
                controller: 'MainSearchController'
            }
        }
    }).state('appointments', {
        url: 'patients/{patientId:int}/appointments',
        views: {
            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
            main: {templateUrl: 'views/appointments/appointments.html', controller: 'AppointmentsCtrl'}
        }
    }).state('access-record-html', {
        url: 'patients/{patientId:int}/access-record/html',
        views: {
            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
            main: {templateUrl: 'views/access-record/access-record-html.html'}
        }
    }).state('access-record-structured', {
        url: 'patients/{patientId:int}/access-record/structured',
        views: {
            'user-context': {templateUrl: 'views/patients/patients-context.html', controller: 'PatientsDetailCtrl'},
            actions: {templateUrl: 'views/patients/patients-sidebar.html', controller: 'PatientsDetailCtrl'},
            main: {templateUrl: 'views/access-record/access-record-structured.html' ,controller: 'AppointmentsStructuredCtrl'}
        }
    });
}).directive('datepickerPopup', function() {
    return {
        restrict: 'EAC',
        require: 'ngModel',
        link: function (scope, element, attr, controller) {
            controller.$formatters.unshift();
        }
    };
}).directive('mySpace', function() {
    return function (scope, element, attrs) {
        element.bind('keydown keypress', function(event) {
            if (event.which === 32) {
                scope.$apply(function() {
                    scope.$eval(attrs.myEnter);
                });

                event.preventDefault();
            }
        });
    };
}).directive('autoFocus', function($timeout) {
    return {
        restrict: 'A',
        require: 'ngModel',
        scope: {
            ngModel: '='
        },
        link: function(scope, elem, attrs, ctrl) {
            scope.$watch("ngModel", function(value) {
                $timeout(function() {
                    elem[0].focus();
                });
            });
        }
    };
}).directive('isValidNhsNumber', function() {
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
                if (value) {
                    var nhsNum = value.replace(/\s+/g, '');
                    var valid = !isNaN(nhsNum) && nhsNum.length === 10;

                    ctrl.$setValidity('invalidNHSNumFormat', valid);

                    return valid ? nhsNum : '';
                }
            };
        }
    };
}).filter('formatNHSNumber', function() {
    return function(number) {
        if (number === undefined) {
            return;
        }

        return number.slice(0, 3) + " " + number.slice(3, 6) + " " + number.slice(6);
    };
}).constant('keyCodes', {
    esc: 27,
    enter: 13
}).directive('keyBind', ['keyCodes', function(keyCodes) {
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

    return function(scope, element, attrs) {
        var bindings = map(scope.$eval(attrs.keyBind));
        element.bind('keydown keypress', function(event) {
            if (bindings.hasOwnProperty(event.which)) {
                scope.$apply(function() {
                    scope.$eval(bindings[event.which]);
                });
            }
        });
    };
}]).directive('focusElement', function($timeout) {
    return {
        link: function(scope, element, attrs) {
            scope.$watch(attrs.focusElement, function(value) {
                $timeout(function() {
                    if (value === true) {
                        element.focus();
                    }
                });
            });
        }
    };
}).directive('rpOnLoad', ['$parse', function($parse) {
    return function(scope, elem, attrs) {
        var fn = $parse(attrs.rpOnLoad);

        elem.on('load', function(event) {
            scope.$apply(function() {
                fn(scope, {$event: event});
            });
        });
    };
}]).config(function(datepickerConfig, datepickerPopupConfig, cfpLoadingBarProvider) {
    datepickerConfig.startingDay = 1;
    datepickerPopupConfig.showButtonBar = false;
    datepickerPopupConfig.datepickerPopup = 'dd-MMM-yyyy';
    cfpLoadingBarProvider.includeSpinner = false;
}).config(function(paginationTemplateProvider) {
    paginationTemplateProvider.setPath('views/dirPagination.tpl.html');
}).provider("ProviderRouting", function() {
    var providerRouting = {
        "spineProxy": "",
        "ASID": "",
        "practices": []
    };

    var persistentDataModel = {}; // Holds configuration values

    this.$get = function() {
        var q = jQuery.ajax({
            type: 'GET', url: 'providerRouting.json', cache: false, async: false, contentType: 'application/json', dataType: 'json'
        });

        if (q.status === 200) {
            providerRouting = angular.fromJson(q.responseText);

            providerRouting.defaultPractice = function() {
                if (persistentDataModel.testingOdsCode != undefined && persistentDataModel.testingOdsCode.length > 0) {
                    var defaultPracticeOdsCode = persistentDataModel.testingOdsCode;
                } else {
                    var defaultPracticeOdsCode = $('#defaultPracticeOdsCode').html();
                }

                for (var p = 0; p < this.practices.length; p++) {
                    var practice = this.practices[p];

                    if (practice.odsCode == defaultPracticeOdsCode) {
                        return practice;
                    }
                }
            };

            providerRouting.getPersistentData = persistentDataModel;

            providerRouting.setPersistentData = function(name, value) {
                persistentDataModel[name] = value;
            };
        }

        return providerRouting;
    };
}).service('genericHttpInterceptor', function($injector) {
    // Useful interceptor information https://docs.angularjs.org/api/ng/service/$http
    var service = this;

    service.response = function(response) {
        errorModal(response, $injector);
        return response;
    };

    service.responseError = function(errorResponse) {
        errorModal(errorResponse, $injector);
        return errorResponse;
    };
}).config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('genericHttpInterceptor');
}]);

var errorModal = function(httpResponse, $injector) {
    if (httpResponse.status >= 400) {
        var modal = $injector.get('$modal');

        modal.open({
            templateUrl: 'views/application/generic-error-modal.html',
            size: 'mg',
            controller: 'GenericErrorModalCtrl',
            resolve: {
                errorInfo: function() {
                    return {
                        "response": httpResponse
                    };
                }
            }
        });
    }
};
