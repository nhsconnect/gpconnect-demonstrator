'use strict';

angular.module('gpConnect')
    .controller('AppointmentsSlotsCtrl', function ($scope, usSpinnerService, Appointment) {

        Appointment.getScheduleOperation($stateParams.organizationODSCode, $stateParams.siteODSCode, $stateParams.startDateTime, $stateParams.endDateTime).then(function (result) {
            var getScheduleJson = result.data;
        });
    });
