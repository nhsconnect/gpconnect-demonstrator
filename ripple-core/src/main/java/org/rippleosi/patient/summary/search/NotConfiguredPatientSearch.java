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
package org.rippleosi.patient.summary.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientQueryParams;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.patient.stats.model.PatientTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableQuery;

/**
 */
public class NotConfiguredPatientSearch implements PatientSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<PatientSummary> findAllPatients() {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public List<PatientSummary> findAllMatchingPatients(List<String> nhsNumbers, ReportTableQuery tableQuery) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public PatientDetails findPatient(String patientId) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public List<PatientSummary> findPatientsBySearchString(PatientTableQuery tableQuery) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public Long countPatientsBySearchString(PatientTableQuery tableQuery) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public PatientSummary findPatientSummary(String patientId) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public List<PatientSummary> findAllPatientsByDepartment(SettingTableQuery tableQuery) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public Long findPatientCountByDepartment(String department) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }

    @Override
    public List<PatientSummary> findPatientsByQueryObject(PatientQueryParams patientQueryParams) {
        throw ConfigurationException.unimplementedTransaction(PatientSearch.class);
    }
}
