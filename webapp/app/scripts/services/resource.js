'use strict';

angular.module('gpConnect')
  .factory('gpcResource', function (gpcConst) {

    var getConst = function (key) {
      return gpcConst[key];
    };

    return {
        getConst: getConst
    };

  });
