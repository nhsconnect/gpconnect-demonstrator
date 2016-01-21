'use strict';

angular.module('rippleDemonstrator')
  .factory('Helper', function () {


    var updateId = function (sourceId) {
      var sourceArr = sourceId.split('::');
      var newVersionNumber = parseInt(sourceArr[2]) + 1;
      var newId = sourceArr[0] + '::' + sourceArr[1] + '::' + newVersionNumber;
      return newId;
    };

    return {
      updateId: updateId
    };

  });
