'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsChartsCtrl
 * @description
 * # PatientsChartsCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('PatientsChartsCtrl', function ($scope, Patient) {
    Patient.all().then(function (patients) {
      $scope.patients = patients;
    });

    $scope.age = {
      ykeys: ['value'],
      xkey: 'age',
      data: [
        {age: '0-10', value: 5},
        {age: '11-18', value: 1},
        {age: '19-30', value: 1},
        {age: '31-60', value: 7},
        {age: '61-80', value: 2},
        {age: '>80', value: 6}
      ],
      config: {
        labels: ['Patients'],
        hideHover: true
      }
    };

    $scope.department = {
      ykeys: ['value'],
      xkey: 'age',
      data: [
        {age: 'All', value: 10},
        {age: 'Emergency', value: 1},
        {age: 'Inpatient', value: 5},
        {age: 'Outpatient', value: 4}
      ],
      config: {
        labels: ['Patients'],
        hideHover: true,
        barColors: ['#0BA462']
      }
    };

  });
