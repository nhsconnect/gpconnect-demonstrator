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
  .factory('PatientService', function ($http, $q, Patient) {

    var loadPatients = function () {
      var patients = [];
      var deferred = $q.defer();

      $http.get('/patients.json', { cache: true }).then(function (result) {
        angular.forEach(result.data, function (patient) {
          patient = new Patient(patient);
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

    var summaries = function () {
      var deferred = $q.defer();

      loadPatients().then(function (patients) {
        var summaries = {};

        var ageSummaries = _.countBy(patients, function (patient) { return patient.ageRange; });

        summaries.age = _.map(ageSummaries, function (value, key) {
            return { series: key, value: value };
        });

        summaries.age = _.sortBy(summaries.age, function (value) {
          return value.age;
        });

        var departmentSummaries = _.countBy(patients, function (patient) { return patient.department; });
        departmentSummaries.All = patients.length;

        summaries.department = _.map(departmentSummaries, function (value, key) {
            return { series: key, value: value };
        });

        summaries.department = _.sortBy(summaries.department, function (value) {
          return value.department;
        });

        deferred.resolve(summaries);
      });

      return deferred.promise;
    };

    return {
      all: all,
      get: get,
      update: update,
      summaries: summaries
    };

  });
