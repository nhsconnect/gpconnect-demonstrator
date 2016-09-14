'use strict';

angular.module('gpConnect')
        .factory('FhirEndpointLookup', function ($http, ProviderRouting) {

            var returnEndpointDetails = {
                "restUrlPrefix": "",
                "fromASID": ProviderRouting.ASID,
                "toASID": ""
            };

            var getEndpoint = function (odsCode, interactionId) {
                var lookupReturn = $http.get(ProviderRouting.defaultPractice().apiEndpointURL + '/ldap/endpointLookup?odsCode=' + odsCode + '&interactionId=' + interactionId).then(function (response) {
                    var lookupResult = response.data;
                    if (lookupResult.endpointURL && lookupResult.recievingSysASID) {
                        returnEndpointDetails.restUrlPrefix = ProviderRouting.spineProxy + lookupResult.endpointURL;
                        returnEndpointDetails.toASID = lookupResult.recievingSysASID;
                    }
                    return returnEndpointDetails;
                });
                return lookupReturn;
            };

            return {
                getEndpoint: getEndpoint
            };
        });
