'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.controller:PatientsChartsCtrl
 * @description
 * # PatientsChartsCtrl
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .controller('PatientsChartsCtrl', function ($scope, $window, $state, Patient) {

    Patient.all().then(function (patients) {
      $scope.patients = patients;

      angular.forEach($scope.patients, function (patient) {
        patient.age = $window.moment().diff(patient.born, 'years');
      });

      var ageSummaries = {
        '0-10': 0,
        '10-18': 0,
        '19-30': 0,
        '31-60': 0,
        '61-80': 0,
        '>80': 0
      };

      _.each($scope.patients, function (patient) {
        var age = patient.age;
        var department = patient.department;

        if (age >= 0 && age <= 10)
        {
          ageSummaries['0-10']++;
        }

        if (age >= 11 && age <= 18)
        {
          ageSummaries['10-18']++;
        }

        if (age >= 19 && age <= 30)
        {
          ageSummaries['19-30']++;
        }

        if (age >= 31 && age <= 60)
        {
          ageSummaries['31-60']++;
        }

        if (age >= 60 && age <= 80)
        {
          ageSummaries['61-80']++;
        }

        if (age > 80)
        {
          ageSummaries['>80']++;
        }
      });

      var departmentSummaries = _.countBy($scope.patients, function (patient) { return patient.department; })
      departmentSummaries['All'] = $scope.patients.length;

      var ageData = [];
      var departmentData = [];

      angular.forEach(ageSummaries, function (value, key) {
        ageData.push({age: key, value: value});
      });

      angular.forEach(departmentSummaries, function (value, key) {
        departmentData.push({department: key, value: value});
      });

      $window.Morris.Bar({
        element: 'age-chart',
        data: ageData,
        ykeys: ['value'],
        xkey: 'age',
        labels: ['Patients'],
        hideHover: true
      }).on('click', function(i, row){
        $state.go('patients-list', { ageRange: row.age });
      });

      $window.Morris.Bar({
        element: 'department-chart',
        data: departmentData,
        ykeys: ['value'],
        xkey: 'department',
        labels: ['Patients'],
        hideHover: true,
        barColors: ['#0BA462']
      }).on('click', function(i, row){
        $state.go('patients-list', { department: row.department });
      });
    });

  });
