/*
 * Copyright 2016 HSCIC
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

'use strict';

angular.module('gpConnect')
  .controller('EncountersListCtrl', function ($scope, $location, $stateParams, $modal, $state, $sce, PatientService, usSpinnerService, Encounter) {

    $scope.currentPage = 1;

    $scope.pageChangeHandler = function (newPage) {
      $scope.currentPage = newPage;
    };

    if ($stateParams.page) {
      $scope.currentPage = $stateParams.page;
    }

    PatientService.get($stateParams.patientId).then(function (patient) {
      $scope.patient = patient;
    });

    Encounter.findAllHTMLTables($stateParams.patientId).then(function (result) {
      $scope.encounterTables = result.data;

      for (var i = 0; i < $scope.encounterTables.length; i++) {
        $scope.encounterTables[i].html = $sce.trustAsHtml($scope.encounterTables[i].html);
      }

      usSpinnerService.stop('encounterSummary-spinner');
    });
  });
