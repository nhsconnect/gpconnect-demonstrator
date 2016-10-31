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
                    var percistentTestData = ProviderRouting.getPersistentData;
                    
                    if (lookupResult.endpointURL && lookupResult.recievingSysASID) {
                        if(percistentTestData.testingOdsCode == undefined || percistentTestData.testingOdsCode.length <= 0){
                            if(percistentTestData.testingFhirUrl != undefined && percistentTestData.testingFhirUrl.length > 0){
                                lookupResult.endpointURL = percistentTestData.testingFhirUrl;
                            }
                        }
                        returnEndpointDetails.restUrlPrefix = ProviderRouting.spineProxy + lookupResult.endpointURL;
                        returnEndpointDetails.toASID = lookupResult.recievingSysASID;
                        
                        // Test Configuration Overwrite
                        if(percistentTestData.toASID != undefined && percistentTestData.toASID.length > 0){
                            returnEndpointDetails.toASID = percistentTestData.toASID;
                        }
                        if(percistentTestData.fromASID != undefined && percistentTestData.fromASID.length > 0){
                            returnEndpointDetails.fromASID = percistentTestData.fromASID;
                        }
                    }
                    return returnEndpointDetails;
                });
                return lookupReturn;
            };

            return {
                getEndpoint: getEndpoint
            };
        });
