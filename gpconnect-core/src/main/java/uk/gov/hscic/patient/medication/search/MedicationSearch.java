package uk.gov.hscic.patient.medication.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;

public interface MedicationSearch extends Repository {

    List<MedicationListHTML> findMedicationHTMLTables(String patientId);
}
