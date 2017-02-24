package uk.gov.hscic.patient.encounters.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.encounters.model.EncounterData;
import uk.gov.hscic.patient.encounters.model.EncounterEntity;
import uk.gov.hscic.patient.encounters.repo.EncounterRepository;

@Service
public class LegacyEncounterSearch extends AbstractLegacyService implements EncounterSearch {

    @Autowired
    private EncounterRepository encounterRepository;

    @Override
    public List<EncounterData> findAllEncounterHTMLTables(final String patientId, Date fromDate, Date toDate) {

        List<EncounterEntity> items = null;
        List<EncounterData> encounterDataList = new ArrayList<>();

        if (fromDate != null && toDate != null) {
            items = encounterRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(
                    patientId, fromDate, toDate);
            encounterDataList = sortItems(items);
        } else if (fromDate != null) {
            items = encounterRepository.findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(patientId, fromDate);
            encounterDataList = sortItems(items);
        } else if (toDate != null) {
            items = encounterRepository.findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(patientId, toDate);
            encounterDataList = sortItems(items);
        } else {
            items = encounterRepository.findBynhsNumberOrderBySectionDateDesc(patientId);
            encounterDataList = sortItems(items);
        }
        return encounterDataList;

    }

    private List<EncounterData> sortItems(List<EncounterEntity> items) {
        List<EncounterData> encounterItemList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            EncounterData encounterData = new EncounterData();
            encounterData.setEncounterDate(items.get(i).getEncounterDate());
            encounterData.setTitle(items.get(i).getTitle());
            encounterData.setDetails(items.get(i).getDetails());

            encounterItemList.add(encounterData);
        }

        return encounterItemList;
    }
}
