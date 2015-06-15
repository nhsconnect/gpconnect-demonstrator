'use strict';

angular.module('openehrPocApp')
  .controller('PatientsChartsCtrl', function ($scope, $window, $state, PatientService, $modal, $stateParams) {

    $scope.openModal = function (row, chartType){
    $modal.open({
      templateUrl: 'views/confirmation.html',
      size: 'md',
      controller: function ($scope) {

        $scope.cancel = function () {
          $scope.$close(true);
        };

        $scope.ok = function () {
          $scope.$close(true);

          switch(chartType) {
            case 'all':
              $state.go('patients-list');
              break;
            case 'age':
              $state.go('patients-list', { ageRange: row.series });
              break;
            case 'summary':
              if (row.series === 'All') {
                row.series = null;
              }
              $state.go('patients-list', { department: row.series });
              break;
            default:
              $state.go('patients-list');
              break;
          }
        };

      }
    });
    };

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
        ymax: 46
      }).on('click', function (i, row) {

        var chartType = 'age';
        $scope.openModal(row, chartType);

      });
    };

    var departmentChart = function (summaries) {
      $window.Morris.Bar({
        element: 'department-chart',
        resize: true,
        data: summaries.department,
        ykeys: ['value'],
        xkey: 'series',
        labels: ['Patients'],
        hideHover: true,
        barColors: ['#25A174'],
        ymin: 0,
        ymax: 40
      }).on('click', function (i, row) {

        var chartType = 'summary';
        $scope.openModal(row, chartType);

      });
    };

    jQuery(document).ready(function($) {
      // Chart Toggle
      $('.chart-inner select').change(function(){

        // Get the target
        var target = $(this).val();

        // Show / Hide loop through all charts witihn this section
        $(this).closest('.chart-inner').find('.chart').each(function(){
          if( $(this).attr('id') === target ){
            $(this).show();
          } else {
            $(this).hide();
          }
        });

      });
       // Clear previous chart
       $scope.toggleChart = function(){
        if($scope.selectedChart === 'age'){
          $('#age-chart').empty();
          $('#department-chart').empty();
          $('#age-chart').off('click');
          $('#department-chart').off('click');

          PatientService.summaries().then(function (summaries) {
            ageChart(summaries);
          });
        } else {
          $('#age-chart').empty();
          $('#department-chart').empty();
          $('#age-chart').off('click');
          $('#department-chart').off('click');

          PatientService.summaries().then(function (summaries) {
            departmentChart(summaries);
          });
        }
      };
    });

    // Selected chart on page load
    $scope.selectedChart = 'age';
    $scope.toggleChart();

  });











