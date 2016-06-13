package uk.gov.hscic.medication.search;

import java.util.List;

import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.medication.model.MedicationDetails;
import uk.gov.hscic.medication.model.PatientMedicationHTML;

public interface MedicationSearch extends Repository {

    List<PatientMedicationHTML> findPatientMedicationHTML(String patientId);
    
    MedicationDetails findMedicationByID(Long id);
    
}
