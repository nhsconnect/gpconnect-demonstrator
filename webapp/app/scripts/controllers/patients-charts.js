'use strict';

angular.module('openehrPocApp')
  .controller('PatientsChartsCtrl', function ($scope, $window, $state, PatientService, $rootScope) {

    $rootScope.pageHeader = $state.current.pageHeader;

    PatientService.summaries().then(function (summaries) {
      ageChart(summaries);
      departmentChart(summaries);
    });

    var ageChart = function (summaries) {
      $window.Morris.Bar({
        element: 'age-chart',
        data: summaries.age,
        ykeys: ['value'],
        xkey: 'series',
        labels: ['Patients'],
        hideHover: true,
        ymin: 0,
        ymax: 40
      }).on('click', function (i, row) {
        $state.go('patients-list', { ageRange: row.series });
      });
    };

    var departmentChart = function (summaries) {
      $window.Morris.Bar({
        element: 'department-chart',
        data: summaries.department,
        ykeys: ['value'],
        xkey: 'series',
        labels: ['Patients'],
        hideHover: true,
        barColors: ['#0BA462'],
        ymin: 0,
        ymax: 40
      }).on('click', function (i, row) {
        if (row.series === 'All') {
          row.series = null;
        }

        $state.go('patients-list', { department: row.series });
      });
    };

  });
