'use strict';

angular.module('rippleDemonstrator')
  .factory('UserService', function ($location, $http, claims, content) {

    var currentUser = {
      role: claims.role,
      email: claims.email,
      tenant: {
        id: claims.tenant_id,
        name: claims.tenant_name
      },
      firstName: claims.given_name,
      surname: claims.family_name,
      dateOfBirth: claims.date_of_birth,
      isAuthenticated: true,
      feature: claims.scope
    };

    var setCurrentUserFromQueryString = function () {
      var claims = $location.search();
      currentUser.email = claims.email || currentUser.email;
      currentUser.role = claims.role || currentUser.role;
    };

    var getCurrentUser = function () {
       return currentUser;
    };

    var getContent = function (key) {
      return content[key];
    };

    return {
      setCurrentUserFromQueryString: setCurrentUserFromQueryString,
      getCurrentUser: getCurrentUser,
      getContent: getContent
    };

  });
