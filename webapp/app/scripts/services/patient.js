'use strict';

angular.module('rippleDemonstrator')
  .factory('PatientService', function ($http, Patient) {

    var all = function () {
      return $http.get('/api/patients').then(function (result) {
        var patients = [];

        angular.forEach(result.data, function (patient) {
          patient = new Patient(patient);
          patients.push(patient);
        });

        return patients;
      });
    };

    var get = function (id) {
      return $http.get('/api/patients/' + id, {
        cache: true
      }).then(function (result) {
        return new Patient(result.data);
      });
    };

    var create = function (patient) {
      var sendPatient = {
        address1: patient.address1,
        address2: patient.address2,
        address3: patient.address3,
        address4: patient.address4,
        address5: patient.address5,
        born: patient.born,
        department: patient.department,
        firstname: patient.firstname,
        gPPractice: patient.gPPractice,
        gender: patient.gender,
        id: patient.id,
        lastname: patient.lastname,
        nhsNo: patient.nhsNo,
        phone: patient.phone,
        postCode: patient.postCode,
        title: patient.title
      };

      return $http.put('/api/patients', sendPatient);
    };

    var update = function (patient, updatedDiagnosis) {
      var diagnosis = _.findWhere(patient.diagnoses, {
        id: updatedDiagnosis.id
      });
      angular.extend(diagnosis, updatedDiagnosis);
    };

    var swapArrayElements = function (arr, indexA, indexB) {
      var temp = arr[indexA];
      arr[indexA] = arr[indexB];
      arr[indexB] = temp;
    };

    var summaries = function () {
      return all().then(function (patients) {
        var summaries = {};

        summaries.age = _.chain(patients)
          .filter(function (patient) {
            return !!patient.age;
          })
          .countBy(function (patient) {
            return patient.ageRange;
          })
          .map(function (value, key) {
            return {
              series: key,
              value: value
            };
          })

        .reverse()
          .value();

        swapArrayElements(summaries.age, 3, 4);

        summaries.department = _.chain(patients)
          .filter(function (patient) {
            return !!patient.department;
          })
          .countBy(function (patient) {
            return patient.department;
          })
          .map(function (value, key) {
            return {
              series: key,
              value: value
            };
          })
          .sortBy(function (value) {
            return value.department;
          })
          .value();

        return summaries;
      });
    };

    return {
      all: all,
      get: get,
      update: update,
      summaries: summaries,
      create: create
    };

  });
