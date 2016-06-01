'use strict';

angular.module('gpConnect')
  .factory('Referral', function ($http) {

    var findAllSummaries = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/referrals');
    };

    var findDetails = function (patientId, referralId) {
      return $http.get('/api/patients/' + patientId + '/referrals/' + referralId);
    };

    var findAllHTMLTables = function (patientId, source) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Referrals"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    var create = function (patientId, referral) {
      return $http.post('/api/patients/' + patientId + '/referrals', referral);
    };

    var update = function (patientId, referral) {
      return $http.put('/api/patients/' + patientId + '/referrals', referral);
    };

    return {
      findAllSummaries: findAllSummaries,
      findDetails: findDetails,
      findAllHTMLTables: findAllHTMLTables,
      update: update,
      create: create
    };

  });
