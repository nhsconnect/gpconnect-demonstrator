'use strict';

angular.module('gpConnect')
  .factory('OrderService', function ($rootScope,  $http, FhirEndpointLookup, fhirJWTFactory) {

    var findAllReceivedOrders = function (patientId) {
        return $http.get('/api/notfhir/orders/patient/' + patientId + '?recieved=true&sent=false');
    };

    var findAllSentOrders = function (patientId) {
        return $http.get('/api/notfhir/orders/patient/' + patientId + '?recieved=false&sent=true');
    };

    var sendOrder = function (patientId, fhirOrder, practiceOdsCode) {
        return FhirEndpointLookup.getEndpoint(practiceOdsCode,"urn:nhs:names:services:gpconnect:fhir:rest:create:order").then(function (response) {
        var endpointLookupResult = response;
            return $http.post(endpointLookupResult.restUrlPrefix+'/Order', fhirOrder,{
              headers: {
                  'Ssp-From': endpointLookupResult.fromASID,
                  'Ssp-To': endpointLookupResult.toASID,
                  'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:rest:create:order",
                  'Ssp-TraceID': fhirJWTFactory.guid(),
                  'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId)
              }
            });
        });
    };

    var saveOrder = function (order) {
        return $http.post('/api/notfhir/orders/order', order);
    };

    return {
      findAllReceivedOrders: findAllReceivedOrders,
      findAllSentOrders: findAllSentOrders,
      sendOrder: sendOrder,
      saveOrder: saveOrder
    };

  });
