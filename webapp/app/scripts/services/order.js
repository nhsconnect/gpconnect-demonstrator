'use strict';

angular.module('gpConnect')
  .factory('Order', function ($http, EnvConfig, fhirJWTFactory) {

    var findAllOrders = function (patientId) {
        return $http.get('/api/notfhir/orders/patient/' + patientId + '?recieved=false&sent=true');
    };
    
    var sendOrder = function (patientId, fhirOrder) {
        return $http.post('/fhir/Order', fhirOrder,{
          headers: {
              'Ssp-From': EnvConfig.fromASID,
              'Ssp-To': EnvConfig.toASID,
              'Ssp-InteractionID': "urn:nhs:names:services:gpconnect:fhir:operation:gpc.getschedule",
              'Authorization': "Bearer " + fhirJWTFactory.getJWT("patient", "write", patientId)
          }
        });
    };
    
    var saveOrder = function (order) {
        return $http.post('/api/notfhir/orders/order', order);
    };
    
    return {
      findAllOrders: findAllOrders,
      sendOrder: sendOrder,
      saveOrder: saveOrder
    };

  });
