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
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.annotations.NamedQuery;
import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.details.repo.PatientRepository;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public PatientSummary findPatientSummary(String patientId) {
        PatientEntity patient = patientRepository.findByNhsNumber(patientId);
        return patientEntityToSummaryTransformer.transform(patient);
    }

    @Override
    public List<PatientSummary> findAllPatientsByDepartment(SettingTableQuery tableQuery) {
        // determine page number (zero indexed) and sort direction
        Integer pageNumber = Integer.valueOf(tableQuery.getPageNumber()) - 1;
        Sort.Direction sortDirection = Sort.Direction.fromString(tableQuery.getOrderType());

        // create the request for a page (sorted by NHS number)
        PageRequest pageRequest = new PageRequest(pageNumber, 15, sortDirection, "nhsNumber");

        // find and return the data
        List<PatientEntity> patients =
            patientRepository.findPatientsByDepartmentDepartmentIgnoreCase(tableQuery.getSearchString(),
                                                                           pageRequest);

        return CollectionUtils.collect(patients, patientEntityToSummaryTransformer, new ArrayList<>());
    }
}
