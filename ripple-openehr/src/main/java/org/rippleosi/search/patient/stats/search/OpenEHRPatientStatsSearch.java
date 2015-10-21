package org.rippleosi.search.patient.stats.search;

import java.util.List;

import org.rippleosi.common.service.AbstractC4HReportingService;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.PageableTableQuery;
import org.rippleosi.search.patient.stats.model.SearchTableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRPatientStatsSearch extends AbstractC4HReportingService implements PatientStatsSearch {

    @Autowired
    PatientStatsQueryStrategy queryStrategy;

    @Override
    public SearchTableResults findAssociatedPatientData(PageableTableQuery tableQuery,
                                                        List<PatientSummary> patientSummaries) {
        queryStrategy.setTableQuery(tableQuery);
        queryStrategy.setPatientSummaries(patientSummaries);
        return findTableData(queryStrategy);
    }
}
