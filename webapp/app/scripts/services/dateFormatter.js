'use strict';

angular.module('openehrPocApp')
  .factory('DateFormatter', function () {

    var clean = function (date) {
<<<<<<< HEAD
      return moment(date).format('YYYY-MM-DD')+'T00:00:00Z';
=======
      return moment(date).format('YYYY-MM-DD');
>>>>>>> Changes after Lisa's feedback
    };


    return {
        clean:clean
    };
  });
