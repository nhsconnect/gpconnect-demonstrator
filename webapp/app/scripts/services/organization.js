'use strict';

angular.module('gpConnect')
        .factory('Organization', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory) {

            var findOrganisation = function (patientId, orgId) {
                return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:location").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.get(endpointLookupResult.restUrlPrefix + '/Organization/' + orgId,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:location",
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId)
                                }
                            });
                });
            };

            return {
                findOrganisation: findOrganisation
            };

        });
