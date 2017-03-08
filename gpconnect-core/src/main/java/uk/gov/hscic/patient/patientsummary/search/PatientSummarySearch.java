package uk.gov.hscic.patient.patientsummary.search;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHtml;

public interface PatientSummarySearch extends Repository {
    PatientSummaryListHtml findPatientSummaryListHtml(String patientId);
}
