package uk.gov.hscic.medication.administration.search;


import java.util.List;
import uk.gov.hscic.common.repo.Repository;
import uk.gov.hscic.medication.administration.model.MedicationAdministrationDetail;

public interface MedicationAdministrationSearch extends Repository {
    
    MedicationAdministrationDetail findMedicationAdministrationByID(Long id);
    
    List<MedicationAdministrationDetail> findMedicationAdministrationForPatient(Long patientId);
    
}
