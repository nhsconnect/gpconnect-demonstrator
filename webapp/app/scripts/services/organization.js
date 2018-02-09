'use strict';

angular.module('gpConnect')
        .factory('Organization', ['$rootScope', '$http', 'FhirEndpointLookup', 'fhirJWTFactory', 'gpcResource', function ($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, gpcResource) {

            var findOrganisation = function (patientId, orgId) {
                return FhirEndpointLookup.getEndpoint($rootScope.patientOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:read:organization-1").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.get(endpointLookupResult.restUrlPrefix + '/Organization/' + orgId,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:read:organization-1",
                                    'Ssp-TraceID': fhirJWTFactory.guid(),
                                    'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "read", $rootScope.patientOdsCode),
                                    'Accept': "application/json+fhir"
                                }
                            });
                });
            };
            
            var searchForOrganisation = function (practiceOdsCode, patientId, searchOrgOdsCode) {
                return FhirEndpointLookup.getEndpoint(practiceOdsCode, "urn:nhs:names:services:gpconnect:fhir:rest:search:organization-1").then(function (response) {
                    var endpointLookupResult = response;
                    return $http.get(endpointLookupResult.restUrlPrefix + '/Organization?identifier='+gpcResource.getConst("ID_ODS_ORGANIZATION_CODE")+'%7C' + searchOrgOdsCode,
                            {
                                headers: {
                                    'Ssp-From': endpointLookupResult.fromASID,
                                    'Ssp-To': endpointLookupResult.toASID,
                                    'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:search:organization-1",
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

        }]);
