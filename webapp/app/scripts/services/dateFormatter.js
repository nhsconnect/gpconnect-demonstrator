'use strict';

angular.module('rippleDemonstrator')
  .factory('DateFormatter', function () {

    var clean = function (date) {
      return moment(date).format('YYYY-MM-DD');
    };

    return {
        clean:clean
    };

  });
