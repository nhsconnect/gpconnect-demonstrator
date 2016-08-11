'use strict';

angular.module('gpConnect')
    .factory('Organization', function ($http, EnvConfig, fhirJWTFactory) {

        var findOrganisation = function (patientId, orgId) {
            return $http.get('/fhir/Organization/' + orgId,
                    {
                        headers: {
                            'Ssp-From': EnvConfig.fromASID,
                            'Ssp-To': EnvConfig.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location",
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                        }
                    });
        };

        return {
            findOrganisation: findOrganisation
        };

    });
