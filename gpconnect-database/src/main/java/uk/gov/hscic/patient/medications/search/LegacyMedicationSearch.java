package uk.gov.hscic.patient.medications.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.medication.model.MedicationListHTML;
import uk.gov.hscic.patient.medication.search.MedicationSearch;
import uk.gov.hscic.patient.medications.model.MedicationEntity;
import uk.gov.hscic.patient.medications.repo.MedicationRepository;

@Service
public class LegacyMedicationSearch extends AbstractLegacyService implements MedicationSearch {

    @Autowired
    private MedicationRepository medicationRepository;

    private final MedicationEntityToListTransformer transformer = new MedicationEntityToListTransformer();

    @Override
    public List<MedicationListHTML> findMedicationHTMLTables(final String patientId) {

        final MedicationEntity item = medicationRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
