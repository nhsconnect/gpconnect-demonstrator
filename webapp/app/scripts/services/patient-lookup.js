'use strict';

angular.module('openehrPocApp')
  .factory('PatientLookup', function ($http) {

    var bySearch = function (firstname, lastname) {
      return $http.post('/bySearch', { firstname: firstname, lastname: lastname }).then(function (result) {
        return result;
      });
    };

    return {
      bySearch: bySearch
    };

  });
