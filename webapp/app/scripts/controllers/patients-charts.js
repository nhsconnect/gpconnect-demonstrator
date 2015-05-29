'use strict';

angular.module('openehrPocApp')
  .controller('PatientsChartsCtrl', function ($scope, $window, $state, PatientService, $modal, $stateParams) {

    var openModal = function (){
    $modal.open({
      templateUrl: 'views/confirmation.html',
      size: 'md',
      controller: function ($scope) {

        $scope.cancel = function () {
          $scope.$dismiss();
          $window.location.href="https://ripple-identity-uat.answerappcloud.com/signout";
        };

        $scope.ok = function () {
          $scope.$close(true);
        };

      }
    });
    };

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
        barColors: ['#7E28CD'],
        ymin: 0,
        ymax: 40
      }).on('click', function (i, row) {
        openModal();                     // Need a callback
        $state.go('patients-list', { ageRange: row.series });
      })
    };

    var departmentChart = function (summaries) {
      $window.Morris.Bar({
        element: 'department-chart',
        data: summaries.department,
        ykeys: ['value'],
        xkey: 'series',
        labels: ['Patients'],
        hideHover: true,
        barColors: ['#25A174'],
        ymin: 0,
        ymax: 40
      }).on('click', function (i, row) {
        openModal();
        if (row.series === 'All') {
          row.series = null;
        }
        $state.go('patients-list', { department: row.series });
      });
    };

    // Chart Toggle
    $('.chart-inner select').change(function(){

      // Get the target
      var target = $(this).val();

      // Show / Hide loop through all charts witihn this section
      $(this).closest('.chart-inner').find('.chart').each(function(){
        console.log( $(this).attr('id') + ' == ' + target + '(' + ($(this).attr('id') == target) + ')' );
        if( $(this).attr('id') == target ){
          $(this).show();
        } else {
          $(this).hide();
        }
      });

    });


  });











