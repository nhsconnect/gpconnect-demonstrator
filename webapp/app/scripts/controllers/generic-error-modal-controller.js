'use strict';

angular.module('gpConnect')
        .controller('GenericErrorModalCtrl', function ($scope, $modalInstance, errorInfo) {
            
            $scope.errorInfo = errorInfo;
            if($scope.errorInfo.statusText.length <= 0){
               $scope.errorInfo.statusText = "No error type returned."; 
            }
            
            $scope.close = function () {
                $modalInstance.close('close');
            };
            
        });
