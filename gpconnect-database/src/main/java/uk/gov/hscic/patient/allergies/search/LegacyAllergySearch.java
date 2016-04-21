package uk.gov.hscic.patient.allergies.search;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
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

    @Override
    public List<AllergyListHTML> findAllAllergyHTMLTables(String patientId) {
        List<AllergyEntity> allergyLists = allergyRepository.findAll();

        return CollectionUtils.collect(allergyLists, new AllergyEntityToListTransformer(), new ArrayList<>());
    }
}
