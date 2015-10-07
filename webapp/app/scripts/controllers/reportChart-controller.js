'use strict';

angular.module('rippleDemonstrator')
  .controller('ReportChartsCtrl', function ($scope, $window, $state, $stateParams, Report) {

    var ageChart = function (graphData) {
      $('svg').height(700);
      $window.Morris.Bar({
      element: 'age-chart',
        resize: true,
        data: graphData,
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

//  PatientService.summaries().then(function (summaries) {
//            ageChart(summaries);
//  });

  if($stateParams.searchString !== undefined){
  var searchQuery = $stateParams.searchString.split(':');

  if(searchQuery.length === 1){
    $state.go('patients-charts');
  }
  var requestBody = {
      reportType: searchQuery[0],
      searchString: searchQuery[1]
  }
  Report.getChart(requestBody).then(function(chartData){
    var graphData = [
      { series: '11-18', value: chartData.data.agedElevenToEighteen },
      { series: '19-30', value: chartData.data.agedNineteenToThirty },
      { series: '31-60', value: chartData.data.agedThirtyOneToSixty },
      { series: '61-80', value: chartData.data.agedSixtyToEighty },
      { series: '>80', value: chartData.data.agedEightyPlus }
      ];

    ageChart(graphData);
  })
  }else {
    $state.go('patients-charts');
  }

  });
