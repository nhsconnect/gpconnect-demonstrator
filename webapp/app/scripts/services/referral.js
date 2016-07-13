'use strict';

angular.module('gpConnect')
  .factory('Referral', function ($http) {

    var findAllHTMLTables = function (patientId, source) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Referrals"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });
