'use strict';

angular.module('gpConnect')
  .factory('Referral', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/referrals');
    };

    var findDetails = function (patientId, compositionId) {
      return $http.get('/api/patients/' + patientId + '/referrals/' + compositionId);
    };
    
    var findAllHTMLTables = function (patientId, source) {
      return $http.get('/api/patients/' + patientId + '/referrals/htmlTables?source=' + source);
    };

    var create = function (patientId, composition) {
      return $http.post('/api/patients/' + patientId + '/referrals', composition);
    };

    var update = function (patientId, composition) {
      return $http.put('/api/patients/' + patientId + '/referrals', composition);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      update: update,
      create: create
    };

  });
