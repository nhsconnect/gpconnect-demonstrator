'use strict';

angular.module('openehrPocApp')
  .factory('Medication', function ($http) {

    var byPatient = function () {
      return $http.get('/dummy-data/medications.json');
    };

    var get = function (medicationId) {
      return byPatient().then(function (result) {
        return _.findWhere(result.data, { id: medicationId });
      });
    };

    return {
      byPatient: byPatient,
      get: get
    };
  });
