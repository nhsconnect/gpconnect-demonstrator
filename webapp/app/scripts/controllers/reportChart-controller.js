'use strict';

angular.module('rippleDemonstrator')
  .controller('ReportChartsCtrl', function ($scope, $window, $state, $stateParams, Report) {

    var ageChart = function (graphData) {
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
        var ageArr = row.series.split('-');
        var ageFr = 0;
        var ageT = 0;

        if(ageArr.length === 2){
          ageFr = ageArr[0];
          ageT = ageArr[1];
        }else {
          ageFr = 80;
          ageT = 130;
        }

        $state.go('patients-list-full', {
          queryType: 'Reports: ',
          ageFrom: ageFr,
          ageTo: ageT,
          orderColumn: 'name',
          orderType: 'ASC',
          pageNumber: 1,
          reportType: $scope.requestBody.reportType,
          searchString: $scope.requestBody.searchString
        });
      });
    };

  if($stateParams.searchString !== undefined){
  var searchQuery = $stateParams.searchString.split(':');

  if(searchQuery.length === 1){
    $state.go('patients-charts');
  }

  $scope.requestBody = {
      reportType: searchQuery[0],
      searchString: searchQuery[1]
  }

  Report.getChart($scope.requestBody).then(function(chartData){
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
