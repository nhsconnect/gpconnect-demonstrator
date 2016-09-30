'use strict';

angular.module('gpConnect')
        .controller('GenericErrorModalCtrl', function ($scope, $modalInstance, errorInfo) {
            
            $scope.errorInfo = errorInfo;
    
            $scope.close = function () {
                $modalInstance.close('close');
            };
            
        });
