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
  .factory('Patient', function () {

    var patients = [
      {id: 746}
    ];

    var basePatient = {
      title: 'Mr',
      firstname: 'John',
      lastname: 'Smith',
      address: '40 High Street, Leeds, West Yorkshire, LS11 2AB',
      phone: '01876876228',
      born: '1950-11-01T00:00:00+00:00',
      gender: 'Male',
      nhsNo: 9999999468,
      gpDetails: 'SMITH, Doc (Mrs)',
      pasNo: 1238446,
    };

    angular.forEach(patients, function (value) {
      angular.extend(value, basePatient);
    });

    var all = function () {
      return patients;
    };

    var get = function (id) {
      var patient = _.findWhere(patients, {id: parseInt(id)});

      patient.fullname = function () {
        return patient.lastname.toUpperCase() + ', ' + patient.firstname + (patient.title ? (' (' + patient.title + ')') : '');
      };

      return patient;
    };

    var update = function(patient, updatedDiagnosis) {
      var diagnosis = _.findWhere(patient.diagnoses, {id: updatedDiagnosis.id});
      angular.extend(diagnosis, updatedDiagnosis);
    };

    return {
      all: all,
      get: get,
      update: update
    };

  });
