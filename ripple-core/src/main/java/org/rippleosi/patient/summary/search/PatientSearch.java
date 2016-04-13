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

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientQueryParams;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.patient.stats.model.PatientTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableQuery;

/**
 */
public interface PatientSearch extends Repository {

    List<PatientSummary> findAllPatients();

    List<PatientSummary> findAllMatchingPatients(List<String> nhsNumbers, ReportTableQuery tableQuery);

    PatientDetails findPatient(String patientId);

    List<PatientSummary> findPatientsBySearchString(PatientTableQuery tableQuery);

    Long countPatientsBySearchString(PatientTableQuery tableQuery);

    PatientSummary findPatientSummary(String patientId);

    List<PatientSummary> findAllPatientsByDepartment(SettingTableQuery tableQuery);

    Long findPatientCountByDepartment(String department);

    List<PatientSummary> findPatientsByQueryObject(PatientQueryParams patientQueryParams);
}
