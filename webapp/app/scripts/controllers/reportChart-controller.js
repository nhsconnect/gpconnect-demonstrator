'use strict';

angular.module('rippleDemonstrator')
  .controller('ReportChartsCtrl', function ($scope, $window, $state, $stateParams, PatientService) {

    var a = $stateParams.searchString;
    var ageChart = function (summaries) {
      $window.Morris.Bar({
        element: 'age-chart',
        resize: true,
        data: summaries.age,
        ykeys: ['value'],
        xkey: 'series',
        labels: ['Patients'],
        barColors: ['#7E28CD'],
        ymin: 0,
        ymax: 46,
        barGap: 4,
        barSizeRatio: 0.55,
        xLabelAngle: 50,
        redraw: true
      }).on('click', function (i, row) {

        var chartType = 'age';
        //$scope.openModal(row, chartType);
      });
    };

  PatientService.summaries().then(function (summaries) {
            ageChart(summaries);
  });

  });
