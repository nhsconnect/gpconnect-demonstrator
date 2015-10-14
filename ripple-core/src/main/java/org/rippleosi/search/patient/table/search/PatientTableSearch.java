package org.rippleosi.search.patient.table.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.summary.model.PatientSummary;
import org.rippleosi.search.reports.table.model.ReportTableResults;

public interface PatientTableSearch extends Repository {

    ReportTableResults findAssociatedPatientData(List<PatientSummary> patientSummaries);
}