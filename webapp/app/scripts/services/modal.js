'use strict';

angular.module('gpConnect').factory("sharedService",["$q", "$modal",'$rootScope','$http', 'FhirEndpointLookup', '$cacheFactory', 'fhirJWTFactory', 'ProviderRouting', 'gpcResource', function (
    $q,
    $modal,
    $rootScope, 
    $http,
    FhirEndpointLookup,   
    $cacheFactory,
    fhirJWTFactory, 
    ProviderRouting,
    gpcResource
)
{
    var _showConfirmDialog = function (title, message)
    {
        var defer = $q.defer();
        
        var modalInstance = $modal.open({
            animation: true,
            size: "sm",
            templateUrl: 'ConfirmationBox.html',
            controller: function ($scope, $modalInstance)
            {
                $scope.title = title;
                $scope.message = message;
                
                $scope.ok = function ()
                {
                    modalInstance.close();
                    defer.resolve();
                };
                
                $scope.cancel = function ()
                {
                    $modalInstance.dismiss();
                    defer.reject();
                };
            }
        });
        
        return defer.promise;
    };
    
    return {
        
        showConfirmDialog: _showConfirmDialog
    };
    
}]);
