'use strict';

angular.module('rippleDemonstrator')
  .factory('Report', function ($http) {

    var getTable = function (searchBody) {
      return $http.post('/api/search/reports/table', searchBody);
    };

    var getChart = function (searchString) {
      return $http.post('/api/search/reports/chart', searchString);
    };

    return {
      getChart: getChart,
      getTable: getTable
    };

  });
