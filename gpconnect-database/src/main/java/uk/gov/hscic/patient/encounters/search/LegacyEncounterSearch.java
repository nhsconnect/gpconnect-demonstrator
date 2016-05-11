package uk.gov.hscic.patient.encounters.search;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.model.EncounterListHTML;
import uk.gov.hscic.patient.encounters.repo.EncounterRepository;

@Service
public class LegacyEncounterSearch extends AbstractLegacyService implements EncounterSearch {

    @Autowired
    private EncounterRepository encounterRepository;

    private final EncounterEntityToListTransformer transformer = new EncounterEntityToListTransformer();

    @Override
    public List<EncounterListHTML> findAllEncounterHTMLTables(final String patientId) {

        final EncounterEntity item = encounterRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
