package org.rippleosi.search.patient.table.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.reports.table.model.ReportTableResults;
import org.springframework.stereotype.Service;

@Service
public class OpenEHRPatientTableSearch extends AbstractOpenEhrService implements PatientTableSearch {

    @Override
    public ReportTableResults findAssociatedPatientData(List<PatientSummary> patientSummaries) {
        PatientTableQueryStrategy queryStrategy = new PatientTableQueryStrategy(patientSummaries);
        return queryStrategy.transform(null);

        // TODO - delete the invocation of transform() and uncomment the line below
//        return findData(queryStrategy);
    }
}
