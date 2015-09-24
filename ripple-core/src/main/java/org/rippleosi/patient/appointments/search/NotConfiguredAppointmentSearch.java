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

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.appointments.model.AppointmentDetails;
import org.rippleosi.patient.appointments.model.AppointmentSummary;

/**
 */
public class NotConfiguredAppointmentSearch implements AppointmentSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<AppointmentSummary> findAllAppointments(String patientId) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }

    @Override
    public AppointmentDetails findAppointment(String patientId, String appointmentId) {
        throw ConfigurationException.unimplementedTransaction(AppointmentSearch.class);
    }
}
