package uk.gov.hscic.medication.dispense.search;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseDetail;
import uk.gov.hscic.medication.dispense.model.MedicationDispenseEntity;
import uk.gov.hscic.medication.dispense.repo.MedicationDispenseRepository;

@Service
public class MedicationDispenseSearch {
    private final MedicationDispenseEntityToMedicationDispenseDetailTransformer transformer = new MedicationDispenseEntityToMedicationDispenseDetailTransformer();

    @Autowired
    private MedicationDispenseRepository medicationDispenseRepository;

    public MedicationDispenseDetail findMedicationDispenseByID(Long id) {
        MedicationDispenseEntity item = medicationDispenseRepository.findOne(id);
        return item == null
                ? null
                : transformer.transform(item);
    }

    public List<MedicationDispenseDetail> findMedicationDispenseForPatient(Long patientId) {
        return medicationDispenseRepository.findByPatientId(patientId)
                .stream()
                .map(transformer::transform)
                .collect(Collectors.toList());
    }
}
