'use strict';

angular.module('openehrPocApp')
  .factory('Contact', function ($http) {

    var byPatient = function () {
      return $http.get('/dummy-data/contacts.json');
    };

    var get = function (contactId) {
      return byPatient().then(function (result) {
        return _.findWhere(result.data, { id: contactId });
      });
    };

    return {
      byPatient: byPatient,
      get: get
    };
  });
