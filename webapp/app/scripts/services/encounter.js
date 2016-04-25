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
  .factory('Encounter', function ($http) {

    var findAllEncounters = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/encounters');
    };

    var findAllHTMLTables = function (patientId) {
      return $http.get('/api/patients/' + patientId + '/encounters/htmlTables');
    };

    var findEncounter = function (patientId, encounterId) {
      return $http.get('/api/patients/' + patientId + '/encounters/' + encounterId);
    };

    var createEncounter = function (patientId, encounter) {
      return $http.post('/api/patients/' + patientId + '/encounters', encounter);
    };

    var updateEncounter = function (patientId, encounter) {
      return $http.put('/api/patients/' + patientId + '/encounters', encounter);
    };

    return {
      findAllEncounters: findAllEncounters,
      findAllHTMLTables: findAllHTMLTables,
      findEncounter: findEncounter,
      createEncounter: createEncounter,
      updateEncounter: updateEncounter
    };
  });
