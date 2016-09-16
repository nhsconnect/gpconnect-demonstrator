'use strict';

angular.module('gpConnect')
        .controller('AppointmentsSlotsCtrl', function ($stateParams, $scope, usSpinnerService, Appointment, appointmentSearchParams, $modalInstance, $modal, modal) {

            var numberOfSearches = 0;
            usSpinnerService.spin('appointmentSlots-spinner');

            $scope.ganttModel = [];
            $scope.ganttHeaderFormats = {
                day: 'DD MMMM YYYY',
                hour: 'HH:mm'
            };

            var internalGetScheduleModel = {}
            $scope.practicesSearchingAndFails = [];

            var startDate = moment(appointmentSearchParams.startDate).format('YYYY-MM-DDTHH:mm:ss');
            var endDate = moment(appointmentSearchParams.endDate).add(23, 'hours').add(59, 'minutes').add(59, 'seconds').format('YYYY-MM-DDTHH:mm:ss');

            var poputlateModel = function (practiceOdsCode, practiceName, startDate, endDate, primary) {
                var practiceState = {};
                practiceState.practiceName = practiceName;
                practiceState.practiceOdsCode = practiceOdsCode;
                practiceState.status = "Searching";
                $scope.practicesSearchingAndFails.push(practiceState);
                
                numberOfSearches++;
                usSpinnerService.spin('appointmentSlots-spinner');

                var responseSlots = [];             // Slots is an array to cycle through easily
                var responseSchedules = {};         // We will lookup schedule from slots so we can use the schedule reference as a key in the key value pair
                var responsePractitioners = {};     // We will lookup practitioner from the schedule using reference so we can use the reference as the key/name
                var responseLocations = {};         // We will lookup location from the schedule using reference so we can use the reference as the key/name

                Appointment.getScheduleOperation(practiceOdsCode, startDate, endDate, $stateParams.patientId).then(function (result) {

                    var getScheduleJson = result.data;

                    // go through response and build arrays of all returned data
                    $.each(getScheduleJson.entry, function (key, value) {
                        if (value.resource.resourceType == "Slot") {
                            var slot = {"scheduleRef": value.resource.schedule.reference, "startDateTime": value.resource.start, "endDateTime": value.resource.end, "type": value.resource.type.coding[0].display, "typeCode": value.resource.type.coding[0].code, "id": value.resource.id};
                            responseSlots.push(slot);
                        }
                        if (value.resource.resourceType == "Schedule") {
                            responseSchedules[value.fullUrl] = {"locationRef": value.resource.actor.reference, "practitionerRef": value.resource.modifierExtension[0].valueReference.reference};
                        }
                        if (value.resource.resourceType == "Practitioner") {
                            var prefix = "";
                            var given = "";
                            var family = "";
                            if (value.resource.name.prefix != undefined) {
                                prefix = value.resource.name.prefix[0];
                            }
                            if (value.resource.name.given != undefined) {
                                given = value.resource.name.given[0];
                            }
                            if (value.resource.name.family != undefined) {
                                family = value.resource.name.family[0];
                            }
                            var fullName = prefix + " " + given + " " + family;
                            responsePractitioners[value.fullUrl] = {
                                "fullName": fullName,
                                "id": value.resource.id
                            };
                        }
                        if (value.resource.resourceType == "Location") {
                            responseLocations[value.fullUrl] = {"name": value.resource.name, "id": value.resource.id};
                        }
                    });

                    if (responseSlots.length > 0) {
                        $.each(responseSlots, function (key, value) {
                            // Build slot object
                            var slot = {"startDateTime": new Date(value.startDateTime), "endDateTime": new Date(value.endDateTime), "type": value.type, "typeCode": value.typeCode, "id": value.id};
                            // Find the schedule for that slot from array
                            var schedule = responseSchedules[value.scheduleRef];
                            // Find Location and practitioner for that schedule
                            var practitionerName = responsePractitioners[schedule.practitionerRef].fullName;
                            var practitionerId = responsePractitioners[schedule.practitionerRef].id;
                            var locationName = responseLocations[schedule.locationRef].name;
                            var locationId = responseLocations[schedule.locationRef].id;

                            // Look at constructed object to see if the location exists already, is so add to existing or add new
                            if (internalGetScheduleModel.locations == undefined) {
                                var slots = [];
                                slots.push(slot);
                                var practitioners = [];
                                var practitionerModel = {"fullName": practitionerName, "slots": slots, "id": practitionerId};
                                practitioners.push(practitionerModel);
                                var dayBlockOfSlots = [];
                                var startDateTime = new Date(slot.startDateTime);
                                var dayBlockOfSlotsModel = {"dayOfWeek": getDayFromDate(slot.startDateTime), "date": startDateTime.setHours(0, 0, 0, 0), "freeSlots": 1, "practitioners": practitioners};
                                dayBlockOfSlots.push(dayBlockOfSlotsModel);
                                var locations = [];
                                var locationModel = {"name": locationName, "freeSlots": 1, "dayBlockOfSlots": dayBlockOfSlots, "id": locationId, "odsCode": practiceOdsCode, "practiceName": practiceName, "primary": primary};
                                locations.push(locationModel);
                                internalGetScheduleModel.locations = locations;
                            } else {
                                var locationIndex = -1;
                                // search locations for location
                                for (var i = 0; i < internalGetScheduleModel.locations.length; i++) {
                                    if (locationId == internalGetScheduleModel.locations[i].id && practiceOdsCode == internalGetScheduleModel.locations[i].odsCode) {
                                        locationIndex = i;
                                        break;
                                    }
                                }
                                ;
                                if (locationIndex == -1) {
                                    // if location is not found we need to build all the structure add it to the model
                                    var slots = [];
                                    slots.push(slot);
                                    var practitioners = [];
                                    var practitionerModel = {"fullName": practitionerName, "slots": slots, "id": practitionerId};
                                    practitioners.push(practitionerModel);
                                    var dayBlockOfSlots = [];
                                    var startDateTime = new Date(slot.startDateTime);
                                    var dayBlockOfSlotsModel = {"dayOfWeek": getDayFromDate(slot.startDateTime), "date": startDateTime.setHours(0, 0, 0, 0), "freeSlots": 1, "practitioners": practitioners};
                                    var locationModel = {"name": locationName, "freeSlots": 1, "dayBlockOfSlots": dayBlockOfSlots, "id": locationId, "odsCode": practiceOdsCode, "practiceName": practiceName, "primary": primary};
                                    internalGetScheduleModel.locations.push(locationModel);
                                } else {
                                    // else get the existing model and add the new slot to it.
                                    internalGetScheduleModel.locations[locationIndex].freeSlots++;  // Increment free slots
                                    //try and find the dayBlockOfSlots for same date
                                    var dayBlockOfSlotsIndex = -1;
                                    for (var i = 0; i < internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots.length; i++) {
                                        var startDateTime = new Date(slot.startDateTime);
                                        if (startDateTime.setHours(0, 0, 0, 0) == internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[i].date) {
                                            dayBlockOfSlotsIndex = i;
                                            break;
                                        }
                                    }
                                    ;
                                    if (dayBlockOfSlotsIndex == -1) {
                                        // If we did not find a block of slots we need to make one
                                        var slots = [];
                                        slots.push(slot);
                                        var practitioners = [];
                                        var practitionerModel = {"fullName": practitionerName, "slots": slots, "id": practitionerId};
                                        practitioners.push(practitionerModel);
                                        var dayBlockOfSlots = [];
                                        var startDateTime = new Date(slot.startDateTime);
                                        var dayBlockOfSlotsModel = {"dayOfWeek": getDayFromDate(slot.startDateTime), "date": startDateTime.setHours(0, 0, 0, 0), "freeSlots": 1, "practitioners": practitioners};
                                        internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots.push(dayBlockOfSlotsModel);
                                    } else {
                                        // If we have found a block of slots for the day we can see if the practitioner exists and is so add our new slot to them
                                        internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].freeSlots++;  // Increment free slots
                                        var practitionerIndex = -1;
                                        for (var i = 0; i < internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners.length; i++) {
                                            if (practitionerId == internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners[i].id) {
                                                practitionerIndex = i;
                                                break;
                                            }
                                        }
                                        ;
                                        if (practitionerIndex == -1) {
                                            // If we did not find a block of slots we need to make one
                                            var slots = [];
                                            slots.push(slot);
                                            var practitionerModel = {"fullName": practitionerName, "slots": slots, "id": practitionerId};
                                            internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners.push(practitionerModel);
                                        } else {
                                            internalGetScheduleModel.locations[locationIndex].dayBlockOfSlots[dayBlockOfSlotsIndex].practitioners[practitionerIndex].slots.push(slot);
                                        }
                                    }
                                }

                            }

                        });

                        $scope.scheduleModel = internalGetScheduleModel;
                        // select the default location
                        $scope.selectDefaultLocation(internalGetScheduleModel.locations);
                        setTimeout(function () {
                            $('.gantt-scrollable').scrollLeft(100);
                        }, 10);
                    }
                    numberOfSearches--;
                    var indexOfElement = $scope.practicesSearchingAndFails.indexOf(practiceState);
                    $scope.practicesSearchingAndFails.splice(indexOfElement,1);
                    practiceState.status = "Success";
                    if (numberOfSearches <= 0) {
                        usSpinnerService.stop('appointmentSlots-spinner');
                    }
                },function (result) {
                    practiceState.status = "Failed";
                    numberOfSearches--;
                    if (numberOfSearches <= 0) {
                        usSpinnerService.stop('appointmentSlots-spinner');
                    }
                });
            };

            if (appointmentSearchParams.primaryPractice.checked == true) {
                poputlateModel(appointmentSearchParams.primaryPractice.odsCode, appointmentSearchParams.primaryPractice.name, startDate, endDate, true);
            }
            $.each(appointmentSearchParams.federatedPractices, function (index, practice) {
                if (practice.checked == true) {
                    poputlateModel(practice.odsCode, practice.name, startDate, endDate, false);
                }
            });


            $scope.loadDaySlots = function (day) {
                $scope.ganttModel = [];
                var practitioners = day.practitioners;

                for (var i = 0; i < practitioners.length; i++) {
                    var practitionerSchedule = {
                        id: practitioners[i].id,
                        name: practitioners[i].fullName,
                        height: '3em',
                        sortable: false,
                        tasks: []
                    };

                    var slots = practitioners[i].slots;
                    for (var j = 0; j < slots.length; j++) {
                        var slot = {
                            name: slots[j].type,
                            typeCode: slots[j].typeCode,
                            color: '#99ccff',
                            from: slots[j].startDateTime,
                            to: slots[j].endDateTime,
                            id: slots[j].id
                        };

                        practitionerSchedule.tasks.push(slot);
                    }

                    $scope.ganttModel.push(practitionerSchedule);
                    $scope.displayFromDate = day.date;
                    $scope.displayToDate = day.date;
                }
            };

            $scope.onSelectLocation = function (location) {
                $scope.selectedLocation = location;
                // grab the default day and select it
                $scope.selectDefaultDay(location);
            };

            $scope.isLocationSelected = function (location) {
                return location === $scope.selectedLocation;
            };

            $scope.selectDefaultLocation = function (locations) {
                // grab the first day and select it
                if (locations.length > 0) {
                    var firstLocation = locations[0];
                    for(var peimaryLocationIndex = 0; peimaryLocationIndex < locations.length; peimaryLocationIndex++){
                        if(locations[peimaryLocationIndex].primary == true){
                            firstLocation = locations[peimaryLocationIndex];
                            peimaryLocationIndex = locations.length;
                        }
                    }
                    $scope.onSelectLocation(firstLocation);
                }
            };

            $scope.isDaySelected = function (day) {
                return day === $scope.selectedDay;
            };

            $scope.onSelectDay = function (day) {
                $scope.selectedDay = day;
                $scope.loadDaySlots(day);
            };

            $scope.selectDefaultDay = function (location) {
                // grab the first day and select it
                if (location.dayBlockOfSlots.length > 0) {
                    var firstDay = location.dayBlockOfSlots[0];
                    $scope.onSelectDay(firstDay);
                }
            };

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.getFormattedDate = function (day) {
                var date = new Date(day.date);
                var options = {weekday: 'short', year: '2-digit', month: 'short', day: 'numeric'};
                var formattedDate = date.toLocaleString('en-GB', options);

                return formattedDate
            };

            $scope.registerEventFunctions = function (eventFunctions) {
                eventFunctions.directives.on.new($scope, function (dName, dScope, dElement, dAttrs, dController) {
                    if (dName === 'ganttTask') {
                        dElement.bind('click', function (event) {
                            // If a task on the gantt chart is clicked then we open the appointment booking modal for that slot
                            $modalInstance.close();
                            $modal.open({
                                templateUrl: 'views/appointments/appointments-create-modal.html',
                                size: 'md',
                                controller: 'AppointmentsCreateModalCtrl',
                                resolve: {
                                    modal: function () {
                                        return {
                                            title: 'Book Appointment'
                                        };
                                    },
                                    appointmentBookingParams: function () {
                                        var appointmentBookingParameters = {};
                                        appointmentBookingParameters.location = $scope.selectedLocation;
                                        appointmentBookingParameters.slotId = dScope.task.model.id;
                                        appointmentBookingParameters.startTime = dScope.task.model.from;
                                        appointmentBookingParameters.endTime = dScope.task.model.to;
                                        appointmentBookingParameters.practitionerId = dScope.task.row.model.id;
                                        appointmentBookingParameters.practitionerFullName = dScope.task.row.model.name;
                                        appointmentBookingParameters.locationId = $scope.selectedLocation.id;
                                        appointmentBookingParameters.typeCode = dScope.task.model.typeCode;
                                        appointmentBookingParameters.type = dScope.task.model.name;
                                        return appointmentBookingParameters;
                                    }
                                }
                            });
                        });
                    }
                });

            };

        });

function getDayFromDate(date) {
    var weekday = new Array(7);
    weekday[0] = "Sunday";
    weekday[1] = "Monday";
    weekday[2] = "Tuesday";
    weekday[3] = "Wednesday";
    weekday[4] = "Thursday";
    weekday[5] = "Friday";
    weekday[6] = "Saturday";

    return weekday[date.getDay()];
}

