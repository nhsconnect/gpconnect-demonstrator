'use strict';

angular.module('gpConnect')
        .controller('AppointmentsSlotsCtrl', function ($scope, usSpinnerService, Appointment, appointmentSearchParams) {

            var appointmentSearch = appointmentSearchParams;

            Appointment.getScheduleOperation("R1A15", "Z33435", "2015-03-22 10:00:00", "2017-12-22 17:59:59").then(function (result) {
                var getScheduleJson = result.data;

                // go through response and build arrays of all returned data
                var responseSlots = [];             // Slots is an array to cycle through easily
                var responseSchedules = {};         // We will lookup schedule from slots so we can use the schedule reference as a key in the key value pair
                var responsePractitioners = {};     // We will lookup practitioner from the schedule using reference so we can use the reference as the key/name
                var responseLocations = {};         // We will lookup location from the schedule using reference so we can use the reference as the key/name

                $.each(getScheduleJson.entry, function (key, value) {
                    if (value.resource.resourceType == "Slot"){
                        var slot = { "scheduleRef" : value.resource.schedule.reference, "startDateTime" : value.resource.start, "endDateTime" : value.resource.end, "type" : value.resource.type.text };
                        responseSlots.push(slot);
                    }
                    if (value.resource.resourceType == "Schedule") {
                        responseSchedules[value.fullUrl] = { "locationRef" : value.resource.actor.reference, "practitionerRef" : value.resource.modifierExtension[0].valueReference.reference };
                    }
                    if (value.resource.resourceType == "Practitioner"){
                        var prefix = "";
                        var given = "";
                        var family = "";
                        if(value.resource.name.prefix != undefined){ prefix = value.resource.name.prefix[0]; }
                        if(value.resource.name.given != undefined){ given = value.resource.name.given[0]; }
                        if(value.resource.name.family != undefined){ family = value.resource.name.family[0]; }
                        var fullName = prefix + " " + given + " " + family;
                        responsePractitioners[value.fullUrl] = {
                            "fullName" : fullName
                        };
                    }
                    if (value.resource.resourceType == "Location"){
                        responseLocations[value.fullUrl] = { "name" : value.resource.name };
                    }
                });

                var internalGetScheduleModel = {};

                $.each(responseSlots, function (key, value) {
                    // Build slot object
                    var slot = { "startDateTime" : new Date(value.startDateTime), "endDateTime" : new Date(value.endDateTime), "type" : value.type };
                    // Find the schedule for that slot from array
                    var schedule = responseSchedules[value.scheduleRef];
                    // Find Location and practitioner for that schedule
                    var practitionerName = responsePractitioners[schedule.practitionerRef].fullName;
                    var locationName = responseLocations[schedule.locationRef].name;

                    // Look at constructed object to see if the location exists already, is so add to existing or add new
                    if(internalGetScheduleModel.locations == undefined){
                        var slots = [];
                        slots.push(slot);
                        var practitioners = [];
                        var practitionerModel = { "fullName" : practitionerName, "slots" : slots };
                        practitioners.push(practitionerModel);
                        var dayBlockOfSlots = [];
                        var dayBlockOfSlotsModel = { "dayOfWeek" : getDayFromDate(slot.startDateTime), "date" : slot.startDateTime.setHours(0, 0, 0, 0), "freeSlots" : 1, "practitioners" : practitioners };
                        dayBlockOfSlots.push(dayBlockOfSlotsModel);
                        var locations = [];
                        var locationModel = { "name" : locationName, "freeSlots" : 1, "dayBlockOfSlots" : dayBlockOfSlots };
                        locations.push(locationModel);
                        internalGetScheduleModel.locations = locations;
                    } else {
                        var locationIndex = -1;
                        // search locations for location
                        for(var i = 0; i < internalGetScheduleModel.locations.length; i++) {
                            if(locationName == internalGetScheduleModel.locations[i].name){
                                locationIndex = i; break;
                            }
                        };
                        if (locationIndex == -1){
                            // if location is not found we need to build all the structure add it to the model
                            var slots = [];
                            slots.push(slot);
                            var practitioners = [];
                            var practitionerModel = { "fullName" : practitionerName, "slots" : slots };
                            practitioners.push(practitionerModel);
                            var dayBlockOfSlots = [];
                            var dayBlockOfSlotsModel = { "dayOfWeek" : getDayFromDate(slot.startDateTime), "date" : slot.startDateTime.setHours(0, 0, 0, 0), "freeSlots" : 1, "practitioners" : practitioners };
                            var locationModel = { "name" : locationName, "freeSlots" : 1, "dayBlockOfSlots" : dayBlockOfSlots };
                            internalGetScheduleModel.locations.push(locationModel);
                        }else{
                            // else get the existing model and add the new slot to it.
                            internalGetScheduleModel.locations[locationIndex].freeSlots++;  // Increment free slots
                            //try and find the dayBlockOfSlots for same date
                            var dayBlockOfSlotsIndex = -1;
                            for(var i = 0; i < internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots.length; i++) {
                                if(slot.startDateTime.setHours(0, 0, 0, 0) == internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[i].date){
                                    dayBlockOfSlotsIndex = i; break;
                                }
                            };
                            if(dayBlockOfSlotsIndex == -1){
                                // If we did not find a block of slots we need to make one
                                var slots = [];
                                slots.push(slot);
                                var practitioners = [];
                                var practitionerModel = { "fullName" : practitionerName, "slots" : slots };
                                practitioners.push(practitionerModel);
                                var dayBlockOfSlots = [];
                                var dayBlockOfSlotsModel = { "dayOfWeek" : getDayFromDate(slot.startDateTime), "date" : slot.startDateTime.setHours(0, 0, 0, 0), "freeSlots" : 1, "practitioners" : practitioners };
                                internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots.push(dayBlockOfSlotsModel);
                            } else {
                                // If we have found a block of slots for the day we can see if the practitioner exists and is so add our new slot to them
                                internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].freeSlots++;  // Increment free slots
                                var practitionerIndex = -1;
                                for(var i = 0; i < internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners.length; i++) {
                                    if(practitionerName == internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners[i].fullName){
                                        practitionerIndex = i; break;
                                    }
                                };
                                if(practitionerIndex == -1){
                                    // If we did not find a block of slots we need to make one
                                    var slots = [];
                                    slots.push(slot);
                                    var practitionerModel = { "fullName" : practitionerName, "slots" : slots };
                                    internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners.push(practitionerModel);
                                } else {
                                    internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners[practitionerIndex].slots.push(slot);
                                }
                            }
                        }

                    }

                });

                $scope.scheduleModel = internalGetScheduleModel;
                console.log(internalGetScheduleModel);
            });
        });

function getDayFromDate(date){
    var weekday = new Array(7);
    weekday[0]=  "Sunday";
    weekday[1] = "Monday";
    weekday[2] = "Tuesday";
    weekday[3] = "Wednesday";
    weekday[4] = "Thursday";
    weekday[5] = "Friday";
    weekday[6] = "Saturday";

    return weekday[date.getDay()];
}
