package uk.gov.hscic.medication.dispense.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseDetail;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseEntity;
import uk.gov.hscic.medication.dispense.repo.MedicationDispenseRepository;

@Service
public class LegacyMedicationDispenseSearch  extends AbstractLegacyService implements MedicationDispenseSearch {

    @Autowired
    private MedicationDispenseRepository medicationDispenseRepository;
    
    private final MedicationDispenseEntityToMedicationDispenseDetailTransformer transformer = new MedicationDispenseEntityToMedicationDispenseDetailTransformer();

    @Override
    public MedicationDispenseDetail findMedicationDispenseByID(Long id) {
        MedicationDispenseEntity item = medicationDispenseRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return transformer.transform(item);
        }
    }

    @Override
    public List<MedicationDispenseDetail> findMedicationDispenseForPatient(Long patientId) {
        List<MedicationDispenseEntity> items = medicationDispenseRepository.findByPatientId(patientId);
        ArrayList<MedicationDispenseDetail> medicationDispense = new ArrayList();
        for(MedicationDispenseEntity entity : items){
            medicationDispense.add(transformer.transform(entity));
        }
        return medicationDispense;
    }
}
