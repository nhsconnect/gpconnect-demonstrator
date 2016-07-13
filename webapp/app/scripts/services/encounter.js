'use strict';

angular.module('gpConnect')
  .factory('Encounter', function ($http, EnvConfig) {

    var findAllHTMLTables = function (patientId) {
      return $http.post(EnvConfig.restUrlPrefix+'/fhir/Patient/$getcarerecord',
        '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "Encounters"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}',
        {
            headers: {
                'Ssp-From': EnvConfig.fromASID,
                'Ssp-To': EnvConfig.toASID,
                'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord"
            }
        }
        );
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });
