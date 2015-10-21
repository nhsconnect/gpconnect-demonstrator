package org.rippleosi.search.patient.stats.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.common.model.PageableTableQuery;
import org.rippleosi.search.patient.stats.model.SearchTableResults;

public interface PatientStatsSearch extends Repository {

    SearchTableResults findAssociatedPatientData(PageableTableQuery tableQuery, List<PatientSummary> patientSummaries);
}
