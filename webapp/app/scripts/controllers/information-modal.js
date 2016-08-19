'use strict';

angular.module('gpConnect')
        .controller('InformationModalCtrl', function ($scope, $modalInstance) {

            $scope.close = function () {
                $modalInstance.close('close');
            };

        });
