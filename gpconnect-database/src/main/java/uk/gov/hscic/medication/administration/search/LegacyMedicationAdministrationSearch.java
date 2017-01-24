package uk.gov.hscic.medication.administration.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.medication.administration.model.MedicationAdministrationDetail;
import uk.gov.hscic.medication.administration.model.MedicationAdministrationEntity;
import uk.gov.hscic.medication.administration.repo.MedicationAdministrationRepository;

@Service
public class LegacyMedicationAdministrationSearch  extends AbstractLegacyService implements MedicationAdministrationSearch {

    @Autowired
    private MedicationAdministrationRepository medicationAdministrationRepository;

    private final MedicationAdministrationEntityToMedicationAdministrationDetailTransformer transformer = new MedicationAdministrationEntityToMedicationAdministrationDetailTransformer();

    @Override
    public MedicationAdministrationDetail findMedicationAdministrationByID(Long id) {
        MedicationAdministrationEntity item = medicationAdministrationRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }

    @Override
    public List<MedicationAdministrationDetail> findMedicationAdministrationForPatient(Long patientId) {
        List<MedicationAdministrationEntity> items = medicationAdministrationRepository.findByPatientId(patientId);
        ArrayList<MedicationAdministrationDetail> medicationAdministration = new ArrayList<>();
        for(MedicationAdministrationEntity entity : items){
            medicationAdministration.add(transformer.transform(entity));
        }
        return medicationAdministration;
    }
}
