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
package org.rippleosi.patient.details.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.details.repo.PatientRepository;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.search.patient.table.model.AbstractPageableTableQuery;
import org.rippleosi.search.patient.table.model.PatientTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LegacyPatientSearch implements PatientSearch {

    @Value("${legacy.datasource.priority:900}")
    private int priority;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientEntityToDetailsTransformer patientEntityToDetailsTransformer;

    @Autowired
    private PatientEntityToSummaryTransformer patientEntityToSummaryTransformer;

    @Override
    public String getSource() {
        return "legacy";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public List<PatientSummary> findAllPatients() {
        Sort sort = new Sort("nhsNumber");
        Iterable<PatientEntity> patients = patientRepository.findAll(sort);
        return CollectionUtils.collect(patients, patientEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public PatientDetails findPatient(String patientId) {
        PatientEntity patient = patientRepository.findByNhsNumber(patientId);
        return patientEntityToDetailsTransformer.transform(patient);
    }

    @Override
    public List<PatientSummary> findPatientsByQuery(PatientTableQuery tableQuery) {
        List<PatientEntity> patients =
            patientRepository.findDistinctByFirstNameOrLastNameOrDateOfBirthAllIgnoreCase(tableQuery.getFirstName(),
                                                                                          tableQuery.getLastName(),
                                                                                          tableQuery.getDateOfBirth(),
                                                                                          generatePageRequest(tableQuery));

        List<PatientEntity> filtered = filterPatients(patients, tableQuery);
        return CollectionUtils.collect(filtered, patientEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public Integer countPatientsByQuery(PatientTableQuery tableQuery) {
        List<PatientEntity> patients =
            patientRepository.findDistinctByFirstNameOrLastNameOrDateOfBirthAllIgnoreCase(tableQuery.getFirstName(),
                                                                                          tableQuery.getLastName(),
                                                                                          tableQuery.getDateOfBirth());
        List<PatientEntity> filtered = filterPatients(patients, tableQuery);
        return filtered.size();
    }

    private List<PatientEntity> filterPatients(List<PatientEntity> patients, PatientTableQuery tableQuery) {
        String firstName = tableQuery.getFirstName();
        String lastName = tableQuery.getLastName();
        Date dateOfBirth = tableQuery.getDateOfBirth();

        List<PatientEntity> filtered = new ArrayList<>();

        for (PatientEntity patient : patients) {
            boolean firstNameEqual = true;
            boolean lastNameEqual = true;
            boolean dateOfBirthEqual = true;

            if (firstName != null && !firstName.equals("")) {
                firstNameEqual = patient.getFirstName().equals(firstName);
            }
            if (lastName != null && !lastName.equals("")) {
                lastNameEqual = patient.getLastName().equals(lastName);
            }
            if (dateOfBirth != null) {
                dateOfBirthEqual = DateUtils.isSameDay(patient.getDateOfBirth(), dateOfBirth);
            }

            if (firstNameEqual && lastNameEqual && dateOfBirthEqual) {
                filtered.add(patient);
            }
        }

        return filtered;
    }

    @Override
    public PatientSummary findPatientSummary(String patientId) {
        PatientEntity patient = patientRepository.findByNhsNumber(patientId);
        return patientEntityToSummaryTransformer.transform(patient);
    }

    @Override
    public Integer findPatientCountByDepartment(String department) {
        return patientRepository.countByDepartmentDepartmentIgnoreCase(department);
    }

    @Override
    public List<PatientSummary> findAllPatientsByDepartment(SettingTableQuery tableQuery) {
        List<PatientEntity> patients =
            patientRepository.findPatientsByDepartmentDepartmentIgnoreCase(tableQuery.getSearchString(),
                                                                           generatePageRequest(tableQuery));

        return CollectionUtils.collect(patients, patientEntityToSummaryTransformer, new ArrayList<>());
    }

    private PageRequest generatePageRequest(AbstractPageableTableQuery tableQuery) {
        // determine page number (zero indexed) and sort direction
        Integer pageNumber = Integer.valueOf(tableQuery.getPageNumber()) - 1;
        Direction sortDirection = Direction.fromString(tableQuery.getOrderType());

        // create the request for a page (sorted by NHS number)
        return new PageRequest(pageNumber, 15, sortDirection, "nhsNumber");
    }
}
