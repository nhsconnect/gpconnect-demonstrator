'use strict';

angular.module('gpConnect')
  .factory('Immunisation', function ($http) {

    var findAllHTMLTables = function (patientId) {
      return $http.post('/fhir/Patient/$getcarerecord', '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Immunisations"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}');
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });
