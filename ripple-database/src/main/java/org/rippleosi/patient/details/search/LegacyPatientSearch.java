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

import com.mysema.query.BooleanBuilder;
import com.mysema.util.ArrayUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.rippleosi.common.util.DateFormatter;
import org.rippleosi.patient.details.model.PatientEntity;
import org.rippleosi.patient.details.model.QPatientEntity;
import org.rippleosi.patient.details.repo.PatientRepository;
import org.rippleosi.patient.summary.model.PatientDetails;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.patient.summary.search.PatientSearch;
import org.rippleosi.search.common.model.PageableTableQuery;
import org.rippleosi.search.patient.stats.model.PatientTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableQuery;
import org.rippleosi.search.setting.table.model.SettingTableQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
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
    public List<PatientSummary> findAllMatchingPatients(List<String> nhsNumbers, ReportTableQuery tableQuery) {
        if (nhsNumbers.isEmpty()) {
            return null;
        }

        // build the query predicate
        QPatientEntity blueprint = QPatientEntity.patientEntity;
        BooleanBuilder predicate = new BooleanBuilder();

        for (String nhsNumber : nhsNumbers) {
            predicate.or(blueprint.nhsNumber.eq(nhsNumber));
        }

        Iterable<PatientEntity> patients = patientRepository.findAll(predicate, generatePageRequest(tableQuery));
        return CollectionUtils.collect(patients, patientEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public PatientDetails findPatient(String patientId) {
        PatientEntity patient = patientRepository.findByNhsNumber(patientId);
        return patientEntityToDetailsTransformer.transform(patient);
    }

    @Override
    public List<PatientSummary> findPatientsByQuery(PatientTableQuery tableQuery) {
        BooleanBuilder predicate = generateSearchByPatientReportTablePredicate(tableQuery);

        if (predicate == null) {
            return new ArrayList<>();
        }

        Page<PatientEntity> patients = patientRepository.findAll(predicate, generatePageRequest(tableQuery));
        return CollectionUtils.collect(patients, patientEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public Long countPatientsByQuery(PatientTableQuery tableQuery) {
        BooleanBuilder predicate = generateSearchByPatientReportTablePredicate(tableQuery);
        return predicate == null ? 0 : patientRepository.count(predicate);
    }

    private BooleanBuilder generateSearchByPatientReportTablePredicate(PatientTableQuery tableQuery) {
        // extract data from search string
        Object[] searchParams = parseSearchParams(tableQuery.getSearchString());
        int totalParams = searchParams.length;

        // build the query predicate
        QPatientEntity blueprint = QPatientEntity.patientEntity;
        BooleanBuilder predicate = new BooleanBuilder();

        // quit processing if no params
        if (totalParams == 0) {
            return null;
        }

        if (totalParams == 1) {
            Object param = searchParams[0];

            // firstly, check whether this param a date
            if (param instanceof Date) {
                predicate.and(blueprint.dateOfBirth.eq((Date) param));
            }
            // if not, it's either the first or last name
            else {
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) param),
                                   blueprint.lastName.equalsIgnoreCase((String) param));
            }
        }
        else if (totalParams == 2) {
            Object firstParam = searchParams[0];
            Object secondParam = searchParams[1];

            // again, start with a date param, then test first and last name
            if (firstParam instanceof Date) {
                predicate.and(blueprint.dateOfBirth.eq((Date) firstParam));
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) secondParam),
                                   blueprint.lastName.equalsIgnoreCase((String) secondParam));
            }
            else if (secondParam instanceof Date) {
                predicate.and(blueprint.dateOfBirth.eq((Date) secondParam));
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) firstParam),
                                   blueprint.lastName.equalsIgnoreCase((String) firstParam));
            }
            // if neither are a date, then both are strings
            else {
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) firstParam),
                                   blueprint.firstName.equalsIgnoreCase((String) secondParam));
                predicate.andAnyOf(blueprint.lastName.equalsIgnoreCase((String) firstParam),
                                   blueprint.lastName.equalsIgnoreCase((String) secondParam));
            }
        }
        else if (totalParams > 2) {
            Object firstParam = searchParams[0];
            Object secondParam = searchParams[1];
            Object thirdParam = searchParams[2];

            if (firstParam instanceof Date) {
                predicate.and(blueprint.dateOfBirth.eq((Date) firstParam));
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) secondParam),
                                   blueprint.firstName.equalsIgnoreCase((String) thirdParam));
                predicate.andAnyOf(blueprint.lastName.equalsIgnoreCase((String) secondParam),
                                   blueprint.lastName.equalsIgnoreCase((String) thirdParam));
            }
            else if (secondParam instanceof Date) {
                predicate.and(blueprint.dateOfBirth.eq((Date) secondParam));
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) firstParam),
                                   blueprint.firstName.equalsIgnoreCase((String) thirdParam));
                predicate.andAnyOf(blueprint.lastName.equalsIgnoreCase((String) firstParam),
                                   blueprint.lastName.equalsIgnoreCase((String) thirdParam));
            }
            else if (thirdParam instanceof Date) {
                predicate.and(blueprint.dateOfBirth.eq((Date) thirdParam));
                predicate.andAnyOf(blueprint.firstName.equalsIgnoreCase((String) firstParam),
                                   blueprint.firstName.equalsIgnoreCase((String) secondParam));
                predicate.andAnyOf(blueprint.lastName.equalsIgnoreCase((String) firstParam),
                                   blueprint.lastName.equalsIgnoreCase((String) secondParam));
            }
            // if none are a date, then there are too many parameters
            else {
                predicate = null;
            }
        }

        return predicate;
    }

    private Object[] parseSearchParams(String searchString) {
        String[] split = StringUtils.split(searchString);
        Object[] searchParams = ArrayUtils.combine(split.length, split);

        for (int i = 0; i < searchParams.length; i++) {
            Date dateParam = DateFormatter.toDate((String) searchParams[i]);

            if (dateParam != null) {
                searchParams[i] = dateParam;
            }
        }

        return searchParams;
    }

    @Override
    public PatientSummary findPatientSummary(String patientId) {
        PatientEntity patient = patientRepository.findByNhsNumber(patientId);
        return patientEntityToSummaryTransformer.transform(patient);
    }

    @Override
    public List<PatientSummary> findAllPatientsByDepartment(SettingTableQuery tableQuery) {
        List<PatientEntity> patients =
            patientRepository.findPatientsByDepartmentDepartmentIgnoreCase(tableQuery.getSearchString(),
                                                                           generatePageRequest(tableQuery));

        return CollectionUtils.collect(patients, patientEntityToSummaryTransformer, new ArrayList<>());
    }

    @Override
    public Long findPatientCountByDepartment(String department) {
        return patientRepository.countByDepartmentDepartmentIgnoreCase(department);
    }

    private PageRequest generatePageRequest(PageableTableQuery tableQuery) {
        // determine page number (zero indexed) and sort direction
        Integer pageNumber = Integer.valueOf(tableQuery.getPageNumber()) - 1;
        Direction sortDirection = Direction.fromString(tableQuery.getOrderType());

        // create the request for a page (sorted by NHS number)
        return new PageRequest(pageNumber, 15, sortDirection, "nhsNumber");
    }
}
