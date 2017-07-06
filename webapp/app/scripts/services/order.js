'use strict';

angular.module('gpConnect').factory('OrderService', function($rootScope, $http, FhirEndpointLookup, fhirJWTFactory, ProviderRouting) {
    var findAllReceivedOrders = function (patientId) {
        return $http.get(ProviderRouting.defaultPractice().apiEndpointURL + '/notfhir/orders/patient/' + patientId + '?recieved=true&sent=false');
    };

    var findAllSentOrders = function (patientId) {
        return $http.get(ProviderRouting.defaultPractice().apiEndpointURL + '/notfhir/orders/patient/' + patientId + '?recieved=false&sent=true');
    };

    var sendOrder = function (patientId, fhirOrder, practiceOdsCode) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:create:order").then(function(response) {
            var endpointLookupResult = response;
            
            return $http.post(
                    endpointLookupResult.restUrlPrefix+'/Order',
                    fhirOrder,
                    {
                        headers: {
                            'Ssp-From': endpointLookupResult.fromASID,
                            'Ssp-To': endpointLookupResult.toASID,
                            'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:order",
                            'Ssp-TraceID': fhirJWTFactory.guid(),
                            'Authorization': "Bearer " + fhirJWTFactory.getJWT("organization", "write", practiceOdsCode),
                            'Accept': "application/json+fhir",
                            'Content-Type': "application/json+fhir"
                        }
                    }
            );
        });
    };

    var saveOrder = function(order) {
        return $http.post(ProviderRouting.defaultPractice().apiEndpointURL + '/notfhir/orders/order', order);
    };

    return {
        findAllReceivedOrders: findAllReceivedOrders,
        findAllSentOrders: findAllSentOrders,
        sendOrder: sendOrder,
        saveOrder: saveOrder
    };
});