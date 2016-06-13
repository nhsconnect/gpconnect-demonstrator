package uk.gov.hscic.medications.search;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import uk.gov.hscic.medication.search.MedicationSearch;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationRepository;

@Service
public class LegacyMedicationSearch extends AbstractLegacyService implements MedicationSearch {

    @Autowired
    private MedicationRepository medicationRepository;

    private final MedicationEntityToListTransformer transformer = new MedicationEntityToListTransformer();

    @Override
    public List<PatientMedicationHTML> findPatientMedicationHTML(final String patientId) {
        final PatientMedicationHtmlEntity item = medicationRepository.findOne(Long.parseLong(patientId));
        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
    
}
