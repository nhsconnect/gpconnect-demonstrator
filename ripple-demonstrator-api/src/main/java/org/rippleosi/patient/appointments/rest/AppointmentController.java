/*
 * Copyright 2015 Ripple OSI
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.rippleosi.patient.appointments.rest;

import java.util.List;

import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.rippleosi.patient.appointments.model.AppointmentSummary;
import org.rippleosi.patient.appointments.search.AppointmentSearch;
import org.rippleosi.patient.appointments.search.AppointmentSearchFactory;
import org.rippleosi.patient.appointments.store.AppointmentStore;
import org.rippleosi.patient.appointments.store.AppointmentStoreFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentSearchFactory appointmentSearchFactory;

    @Autowired
    private AppointmentStoreFactory appointmentStoreFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<AppointmentSummary> findAllAppointments(@PathVariable("patientId") String patientId,
                                                        @RequestParam(required = false) String source) {
        AppointmentSearch appointmentSearch = appointmentSearchFactory.select(source);

        return appointmentSearch.findAllAppointments(patientId);
    }

    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public AppointmentDetails findAppointment(@PathVariable("patientId") String patientId,
                                              @PathVariable("orderId") String appointmentId,
                                              @RequestParam(required = false) String source) {
        AppointmentSearch appointmentSearch = appointmentSearchFactory.select(source);

        return appointmentSearch.findAppointment(patientId, appointmentId);
    }

    @RequestMapping(method = RequestMethod.POST)
    public void createAppointment(@PathVariable("patientId") String patientId,
                                  @RequestParam(required = false) String source,
                                  @RequestBody AppointmentDetails appointment) {
        AppointmentStore appointmentStore = appointmentStoreFactory.select(source);

        appointmentStore.create(patientId, appointment);
    }

    @RequestMapping(method = RequestMethod.PUT)
    public void updateAppointment(@PathVariable("patientId") String patientId,
                                  @RequestParam(required = false) String source,
                                  @RequestBody AppointmentDetails appointment) {
        AppointmentStore appointmentStore = appointmentStoreFactory.select(source);

        appointmentStore.update(patientId, appointment);
    }
}
