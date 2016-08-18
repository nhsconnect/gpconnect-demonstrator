'use strict';

angular.module('gpConnect')
        .factory('FhirEndpointLookup', function ($http, $cacheFactory, EnvConfig) {

            var returnEndpointDetails = {
                "restUrlPrefix": EnvConfig.restUrlPrefix,
                "fromASID": EnvConfig.fromASID,
                "toASID": EnvConfig.toASID
            };

            var getEndpoint = function (odsCode, interactionId) {
                var endpointCache = $cacheFactory.get('endpointCache');
                if (endpointCache == undefined) {
                    endpointCache = $cacheFactory('endpointCache');
                }
                var endpointCacheResponse = endpointCache.get(odsCode + interactionId);

                if (endpointCacheResponse == undefined) {
                    return $http.get('/api/ldap/endpointLookup?odsCode=' + odsCode + '&interactionId=' + interactionId).then(function (response) {
                        var lookupResult = response.data;
                        if (lookupResult.endpointURL && lookupResult.recievingSysASID) {
                            returnEndpointDetails.restUrlPrefix = lookupResult.endpointURL;
                            returnEndpointDetails.toASID = lookupResult.recievingSysASID;
                            endpointCache.put(odsCode + interactionId, returnEndpointDetails);
                        }
                        return returnEndpointDetails;
                    });
                } else {
                    return endpointCacheResponse;
                }
            };

            return {
                getEndpoint: getEndpoint
            };
        });
