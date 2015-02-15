'use strict';

angular.module('openehrPocApp')
  .factory('Allergy', function ($http) {

    var byPatient = function () {
      return $http.get('/dummy-data/allergies.json');
    };

    var get = function (allergyId) {
      return byPatient().then(function (result) {
        return _.findWhere(result.data, { id: allergyId });
      });
    };

    return {
      byPatient: byPatient,
      get: get
    };
  });
