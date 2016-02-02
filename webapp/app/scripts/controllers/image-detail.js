/*
 *   Copyright 2016 Ripple OSI
 *
 *      Licensed under the Apache License, Version 2.0 (the "License");
 *      you may not use this file except in compliance with the License.
 *      You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *      Unless required by applicable law or agreed to in writing, software
 *      distributed under the License is distributed on an "AS IS" BASIS,
 *      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *      See the License for the specific language governing permissions and
 *      limitations under the License.
 */

'use strict';

angular.module('rippleDemonstrator')
  .controller('ImageDetailCtrl', function ($scope, $stateParams, SearchInput, $location, $modal, Helper, $state, usSpinnerService, PatientService, Image) {

    SearchInput.update();

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Image.getSeries($stateParams.patientId, $stateParams.studyId, $stateParams.source).then(function (result) {
      $scope.series = result.data;

      var seriesIds = $scope.series.seriesIds;
      $scope.instanceIds = [];

      for (var i = 0; i < seriesIds.length; i++) {
        findFirstInstanceId(seriesIds[i], i);
      }

      usSpinnerService.stop('imagesDetail-spinner');
    });

    var findFirstInstanceId = function (seriesId, index) {
      Image.getInstanceId($stateParams.patientId, seriesId, $stateParams.source).then(function (result) {
        $scope.instanceIds[index] = result.data.instanceId;
      });
    };

    $scope.openImage = function (imageId) {
      var modalInstance = $modal.open({
        templateUrl: 'views/dicom/image-modal.html',
        size: 'lg',
        controller: 'ImageModalCtrl',
        resolve: {
          modal: function () {
            return {
              title: 'View Dicom Image'
            };
          },
          dicomImageId: function () {
            return imageId;
          },
          patient: function () {
            return $scope.patient;
          }
        }
      })
    };

  });
