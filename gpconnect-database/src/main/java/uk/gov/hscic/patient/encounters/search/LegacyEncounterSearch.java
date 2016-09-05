package uk.gov.hscic.patient.encounters.search;

import java.util.Collections;
import java.util.Date;
import java.util.List;

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

    private final EncounterEntitiesToHTMLTransformer transformer = new EncounterEntitiesToHTMLTransformer();

    @Override
    public List<EncounterListHTML> findAllEncounterHTMLTables(final String patientId, Date fromDate, Date toDate) {

        List<EncounterEntity> items = null;

        if (fromDate != null && toDate != null) {
            items = encounterRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBefore(Long.valueOf(patientId), fromDate, toDate);
        } else if (fromDate != null) {
            items = encounterRepository.findBynhsNumberAndSectionDateAfter(Long.valueOf(patientId), fromDate);
        } else if (toDate != null) {
            items = encounterRepository.findBynhsNumberAndSectionDateBefore(Long.valueOf(patientId), toDate);
        } else {
            items = encounterRepository.findBynhsNumber(Long.valueOf(patientId));
        }

        if (items == null || items.size() <= 0) {
            return null;
        } else {
            return Collections.singletonList(transformer.transform(items));
        }
    }
}
