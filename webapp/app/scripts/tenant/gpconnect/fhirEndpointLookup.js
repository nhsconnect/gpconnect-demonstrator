'use strict';

angular.module('gpConnect')
        .factory('FhirEndpointLookup', function ($http, $cacheFactory, ProviderRouting) {

            var returnEndpointDetails = {
                "restUrlPrefix": 	"",
                "fromASID": 		ProviderRouting.ASID,
                "toASID": 			""
            };

            var getEndpoint = function (odsCode, interactionId) {
                var endpointCache = $cacheFactory.get('endpointCache');
                if (endpointCache == undefined) {
                    endpointCache = $cacheFactory('endpointCache');
                }
                
                var key = '' + odsCode + interactionId;
                
                var endpointCacheResponse = endpointCache.get(key);

                if (endpointCacheResponse == undefined) {
                    var lookupReturn = $http.get('/api/ldap/endpointLookup?odsCode=' + odsCode + '&interactionId=' + interactionId).then(function (response) {
                        var lookupResult = response.data;
                        if (lookupResult.endpointURL && lookupResult.recievingSysASID) {
                            returnEndpointDetails.restUrlPrefix = ProviderRouting.spineProxy + lookupResult.endpointURL;
                            returnEndpointDetails.toASID = lookupResult.recievingSysASID;
                        }
                        return returnEndpointDetails;
                    });
                    endpointCache.put(key, lookupReturn);
                    return lookupReturn
                } else {
                    return endpointCacheResponse;
                }
            };

            return {
                getEndpoint: getEndpoint
            };
        });
