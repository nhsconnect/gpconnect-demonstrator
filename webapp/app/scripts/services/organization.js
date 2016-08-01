'use strict';

angular.module('gpConnect')
  .factory('Organization', function ($http, EnvConfig) {

    var findOrganisation = function (orgId) {
        return $http.get('/fhir/Organization/' + orgId);
    };
    
    return {
      findOrganisation: findOrganisation
    };

  });
