'use strict';

angular.module('rippleDemonstrator')
  .factory('SmspLookup', function ($http, Patient) {

    var byName = function (firstname, lastname) {
      return $http.post('/api/smsp-search', { firstname: firstname, lastname: lastname }).then(function (result) {
        var patients = [];

        angular.forEach(result.data, function (patient) {
          patient = new Patient(patient);
          patients.push(patient);
        });

        return patients;
      });
    };

    return {
      byName: byName
    };

  });
