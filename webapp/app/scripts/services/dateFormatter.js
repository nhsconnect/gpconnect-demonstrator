'use strict';

angular.module('openehrPocApp')
  .factory('DateFormatter', function () {

    var clean = function (date) {
      return moment(date).format('YYYY-MM-DD')+'T00:00:00Z';
    };


    return {
        clean:clean
    };
  });
