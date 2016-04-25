package uk.gov.hscic.patient.patientsummary.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHTML;

public interface PatientSummarySearch extends Repository {

    List<PatientSummaryListHTML> findAllPatientSummaryHTMLTables(String patientId);
}
