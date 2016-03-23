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

import java.util.Date;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.appointments.model.AppointmentSummary;

/**
 */
public class AppointmentSummaryTransformer implements Transformer<Map<String, Object>, AppointmentSummary> {

    @Override
    public AppointmentSummary transform(Map<String, Object> input) {

        Date appointmentDate = DateFormatter.toDateOnly(MapUtils.getString(input, "appointment_date"));
        Date appointmentTime = DateFormatter.toTimeOnly(MapUtils.getString(input, "appointment_date"));

        AppointmentSummary appointment = new AppointmentSummary();
        appointment.setSource("Marand");
        appointment.setSourceId(MapUtils.getString(input, "uid"));
        appointment.setServiceTeam(MapUtils.getString(input, "service_team"));
        appointment.setDateOfAppointment(appointmentDate);
        appointment.setTimeOfAppointment(appointmentTime);

        return appointment;
    }
}
