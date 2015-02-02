/* jshint bitwise: false */

'use strict';

/**
 * @ngdoc function
 * @name openehrPocApp.factory:Patient
 * @description
 * # Patient
 * Controller of the openehrPocApp
 */
angular.module('openehrPocApp')
  .factory('Patient', function ($http, $q) {

    var loadPatients = function () {
      var patients = [];
      var deferred = $q.defer();

      $http.get('/patients.json', { cache: true }).then(function (result) {
        angular.forEach(result.data, function (patient) {
          patient.fullname = getFullname(patient);
          patient.address = getAddress(patient);
          patients.push(patient);
        });

        deferred.resolve(patients);
      });

      return deferred.promise;
    };

    var all = function () {
      return loadPatients();
    };

    var get = function (id) {
      var deferred = $q.defer();
      var patient;

      loadPatients().then(function (patients) {
        patient = _.findWhere(patients, {id: parseInt(id)});
        deferred.resolve(patient);
      });

      return deferred.promise;
    };

    var update = function(patient, updatedDiagnosis) {
      var diagnosis = _.findWhere(patient.diagnoses, {id: updatedDiagnosis.id});
      angular.extend(diagnosis, updatedDiagnosis);
    };

    var getFullname = function (patient) {
      return patient.lastname.toUpperCase() + ', ' + patient.firstname + (patient.title ? (' (' + patient.title + ')') : '');
    };

    var getAddress = function (patient) {
      return [patient.address1, patient.address2, patient.address4].join(', ');
    };

    return {
      all: all,
      get: get,
      update: update
    };

  });
