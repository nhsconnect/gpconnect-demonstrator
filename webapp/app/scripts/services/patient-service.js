'use strict';

angular.module('openehrPocApp')
  .factory('PatientService', function ($http, Patient) {

    var all = function () {
      return $http.get('/dummy-data/patients.json', { cache: true }).then(function (result) {
        var patients = [];

        angular.forEach(result.data, function (patient) {
          patient = new Patient(patient);
          patients.push(patient);
        });

        return patients;
      });
    };

    var get = function (id) {
      return all().then(function (patients) {
        return _.findWhere(patients, { id: parseInt(id) });
      });
    };

    var update = function (patient, updatedDiagnosis) {
      var diagnosis = _.findWhere(patient.diagnoses, { id: updatedDiagnosis.id });
      angular.extend(diagnosis, updatedDiagnosis);
    };

    var summaries = function () {
      return all().then(function (patients) {
        var summaries = {};

        summaries.age = _.chain(patients)
          .countBy(function (patient) { return patient.ageRange; })
          .map(function (value, key) { return { series: key, value: value }; })
          .sortBy(function (value) { return value.ageRange; })
          .reverse()
          .value();

        summaries.department = _.chain(patients)
          .countBy(function (patient) { return patient.department; })
          .map(function (value, key) { return { series: key, value: value }; })
          .sortBy(function (value) { return value.department; })
          .value();

        return summaries;
      });
    };

    return {
      all: all,
      get: get,
      update: update,
      summaries: summaries
    };

  });
