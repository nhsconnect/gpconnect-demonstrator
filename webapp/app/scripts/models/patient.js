'use strict';

angular.module('openehrPocApp')
  .factory('Patient', function ($window) {

    var Patient = function (attributes) {
      _.extend(this, attributes);

      this.fullname = getFullname(this);
      this.address  = getAddress(this);
      this.age      = getAge(this);
      this.ageRange = getAgeRange(this);
    };

    var getFullname = function (patient) {
      return patient.lastname.toUpperCase() + ', ' + patient.firstname + (patient.title ? (' (' + patient.title + ')') : '');
    };

    var getAddress = function (patient) {
      return [patient.address1, patient.address2, patient.address4].join(', ');
    };

    var getAge = function (patient) {
      return $window.moment().diff(patient.born, 'years');
    };

    var getAgeRange = function (patient) {
      var age = patient.age;

      switch (true) {
        case (age >= 0 && age <= 10):
            return '0-10';
        case (age >= 11 && age <= 18):
            return '11-18';
        case (age >= 19 && age <= 30):
            return '19-30';
        case (age >= 31 && age <= 60):
            return '31-60';
        case (age >= 60 && age <= 80):
            return '60-80';
        case (age > 80):
            return '>80';
        default:
            return;
      }
    };

    return Patient;
  });
