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
  .factory('Image', function ($http) {

    var allStudies = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/dicom/' + 'studies');
    };

    var getAllSeriesInStudy = function (patientId, studyId, source) {
      return $http.get('/api/patients/' + patientId + '/dicom/' + 'studies/' + studyId + '/series' + '?source=' + source);
    };

    var getSeriesDetails = function (patientId, seriesId, source) {
      return $http.get('/api/patients/' + patientId + '/dicom/' + 'series/' + seriesId + '?source=' + source);
    };

    var getInstanceId = function (patientId, seriesId, source) {
      return $http.get('/api/patients/' + patientId + '/dicom/' + 'series/' + seriesId + '/instance' + '?source=' + source);
    };

    var getInstance = function (patientId, instanceId, source) {
      return $http.get('/api/patients/' + patientId + '/dicom/' + 'instances/' + instanceId + '?source=' + source)
    };

    return {
      allStudies: allStudies,
      getAllSeriesInStudy: getAllSeriesInStudy,
      getSeriesDetails: getSeriesDetails,
      getInstanceId: getInstanceId,
      getInstance: getInstance
    };

  });
