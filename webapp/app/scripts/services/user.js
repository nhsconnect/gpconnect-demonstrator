'use strict';

angular.module('rippleDemonstrator')
  .factory('UserService', function ($http, claims) {

    var currentUser = {
      role: claims.role,
      email: claims.email,
      tenant: {
        id: claims.tenant_id,
        name: claims.tenant_name
      },
      firstName: claims.given_name,
      surname: claims.family_name,
      isAuthenticated: true,
      feature: claims.scope
    };

    var getCurrentUser = function () {
      console.log(currentUser);
      return currentUser;
    };

    return {
      getCurrentUser: getCurrentUser
    };

  });
