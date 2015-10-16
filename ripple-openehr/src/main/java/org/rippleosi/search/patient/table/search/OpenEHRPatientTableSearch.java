package org.rippleosi.search.patient.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractC4HReportingService;
import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.patient.table.model.PatientTableQuery;
import org.rippleosi.search.reports.table.model.ReportTableResults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRPatientTableSearch extends AbstractC4HReportingService implements PatientTableSearch {

    @Autowired
    PatientTableQueryStrategy queryStrategy;

    @Override
    public ReportTableResults findAssociatedPatientData(PatientTableQuery tableQuery,
                                                        List<PatientSummary> patientSummaries) {
        queryStrategy.setTableQuery(tableQuery);
        queryStrategy.setPatientSummaries(patientSummaries);
        return findTableData(queryStrategy);
    }
}
