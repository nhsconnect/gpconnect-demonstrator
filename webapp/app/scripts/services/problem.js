'use strict';

angular.module('gpConnect')
  .factory('Problem', function ($http, EnvConfig, fhirJWTFactory) {

    var findAllHTMLTables = function(patientId, source) {
        return $http.post(EnvConfig.restUrlPrefix+'/Patient/$gpc.getcarerecord',
            '{"resourceType" : "Parameters","parameter" : [{"name" : "patientNHSNumber","valueIdentifier" : { "value" : "'+patientId+'" }},{"name" : "recordSection","valueString" : "PRB"},{"name" : "timePeriod","valuePeriod" : { "start" : "2015", "end" : "2016" }}]}',
            {
                headers: {
                    'Ssp-From': EnvConfig.fromASID,
                    'Ssp-To': EnvConfig.toASID,
                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getcarerecord",
                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                }
            }
            );
    };

    return {
      findAllHTMLTables: findAllHTMLTables
    };

  });

