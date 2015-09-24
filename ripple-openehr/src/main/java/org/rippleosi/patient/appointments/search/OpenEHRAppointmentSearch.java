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
package org.rippleosi.patient.appointments.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.rippleosi.patient.appointments.model.AppointmentSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRAppointmentSearch extends AbstractOpenEhrService implements AppointmentSearch {

    @Override
    public List<AppointmentSummary> findAllAppointments(String patientId) {
        AppointmentSummaryQueryStrategy query = new AppointmentSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public AppointmentDetails findAppointment(String patientId, String appointmentId) {
        AppointmentDetailsQueryStrategy query = new AppointmentDetailsQueryStrategy(patientId, appointmentId);

        return findData(query);
    }
}
