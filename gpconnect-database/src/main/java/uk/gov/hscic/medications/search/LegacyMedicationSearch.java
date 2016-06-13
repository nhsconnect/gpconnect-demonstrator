package uk.gov.hscic.medications.search;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.medication.model.MedicationDetails;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import uk.gov.hscic.medication.search.MedicationSearch;
import uk.gov.hscic.medications.model.MedicationEntity;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationHtmlRepository;
import uk.gov.hscic.medications.repo.MedicationRepository;

@Service
public class LegacyMedicationSearch extends AbstractLegacyService implements MedicationSearch {

    @Autowired
    private MedicationHtmlRepository medicationHtmlRepository;

    @Autowired
    private MedicationRepository medicationRepository;
    
    private final MedicationEntityToHtmlObjectTransformer transformer = new MedicationEntityToHtmlObjectTransformer();
    private final MedicationEntityToMedicationTransformer medicationTransformer = new MedicationEntityToMedicationTransformer();

    @Override
    public List<PatientMedicationHTML> findPatientMedicationHTML(final String patientId) {
        final PatientMedicationHtmlEntity item = medicationHtmlRepository.findOne(Long.parseLong(patientId));
        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }

    @Override
    public MedicationDetails findMedicationByID(Long id) {
        final MedicationEntity item = medicationRepository.findOne(id);
        if(item == null){
            return null;
        } else {
            return medicationTransformer.transform(item);
        }
    }
    
}
