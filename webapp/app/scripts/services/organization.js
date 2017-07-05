'use strict';

angular.module('gpConnect')
        .factory('Organization', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory) {

            var findOrganisation = function (patientId, orgId) {
                return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:organization").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.get(endpointLookupResult.restUrlPrefix + '/Organization/' + orgId,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:organization",
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "read", patientId),
                                    'Accept': "application/json+fhir"
                                }
                            });
                });
            };
            
            var searchForOrganisation = function (practiceOdsCode, patientId, searchOrgOdsCode) {
                return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:organization").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.get(endpointLookupResult.restUrlPrefix + '/Organization?identifier=http://fhir.nhs.net/Id/ods-organization-code%7C' + searchOrgOdsCode,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:organization",
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", searchOrgOdsCode),
                                    'Accept': "application/json+fhir"
                                }
                            });
                });
            };

            return {
                findOrganisation: findOrganisation,
                searchForOrganisation: searchForOrganisation
            };

        });
