'use strict';

angular.module('rippleDemonstrator')
  .factory('UserService', function ($location, claims) {

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

    var setCurrentUserFromQueryString = function () {
      var claims = $location.search();
      console.log(claims);
      currentUser.email = claims.email || currentUser.email;
      currentUser.role = claims.role || currentUser.role;
    };

    var getCurrentUser = function () {
      console.log(currentUser);
      return currentUser;
    };

    return {
      getCurrentUser: getCurrentUser,
      setCurrentUserFromQueryString: setCurrentUserFromQueryString
    };

  });
