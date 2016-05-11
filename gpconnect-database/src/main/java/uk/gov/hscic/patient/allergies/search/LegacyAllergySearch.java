package uk.gov.hscic.patient.allergies.search;

import java.util.Collections;
import java.util.List;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.allergies.model.AllergyEntity;
import uk.gov.hscic.patient.allergies.model.AllergyListHTML;
import uk.gov.hscic.patient.allergies.repo.AllergyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegacyAllergySearch extends AbstractLegacyService implements AllergySearch {

    @Autowired
    private AllergyRepository allergyRepository;

    private final AllergyEntityToListTransformer transformer = new AllergyEntityToListTransformer();

    @Override
    public List<AllergyListHTML> findAllAllergyHTMLTables(final String patientId) {

        final AllergyEntity item = allergyRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
