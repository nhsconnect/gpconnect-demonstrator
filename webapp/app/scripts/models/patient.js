'use strict';

angular.module('rippleDemonstrator')
  .factory('Patient', function ($window) {

    var patient = function (attributes) {
      var self = this;
      _.extend(this, attributes);

      self.age = function () {
        return $window.moment().diff(self.dateOfBirth, 'years');
      };

      self.ageRange = (function () {
        var age = self.age();

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
      })();
    };

    return patient;

  });
