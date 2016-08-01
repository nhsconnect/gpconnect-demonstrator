'use strict';

angular.module('gpConnect')
  .factory('Order', function ($http, EnvConfig) {

    var findAllOrders = function (patientId) {
        return $http.get('/api/notfhir/orders/patient/' + patientId + '?recieved=true&sent=true');
    };
    
    return {
      findAllOrders: findAllOrders
    };

  });
