package uk.gov.hscic.patient.encounters.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.encounters.model.EncounterData;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.repo.EncounterRepository;

@Service
public class EncounterSearch {

    @Autowired
    private EncounterRepository encounterRepository;

    public List<EncounterData> findAllEncounterHTMLTables(final String patientId, Date fromDate, Date toDate) {
        if (fromDate != null && toDate != null) {
            return sortItems(encounterRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(patientId, fromDate, toDate));
        } else if (fromDate != null) {
            return sortItems(encounterRepository.findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(patientId, fromDate));
        } else if (toDate != null) {
            return sortItems(encounterRepository.findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(patientId, toDate));
        }

        return sortItems(encounterRepository.findBynhsNumberOrderBySectionDateDesc(patientId));
    }

    private List<EncounterData> sortItems(List<EncounterEntity> encounterEntities) {
        List<EncounterData> encounterItemList = new ArrayList<>();

        for (EncounterEntity encounterEntity : encounterEntities) {
            EncounterData encounterData = new EncounterData();
            encounterData.setEncounterDate(encounterEntity.getEncounterDate());
            encounterData.setTitle(encounterEntity.getTitle());
            encounterData.setDetails(encounterEntity.getDetails());

            encounterItemList.add(encounterData);
        }

        return encounterItemList;
    }
}
