package uk.gov.hscic.patient.clinicalitems.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemData;
import uk.gov.hscic.patient.clinicalitems.repo.ClinicalItemRepository;
import uk.gov.hscic.patient.immunisations.model.ImmunisationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class LegacyClinicalItemSearch extends AbstractLegacyService implements ClinicalItemSearch {

    @Autowired
    private ClinicalItemRepository clinicalItemRepository;



    @Override
    public List<ClinicalItemData> findAllClinicalItemHTMLTables(final String patientId, Date fromDate,
            Date toDate) {

        List<ClinicalItemEntity> items = null;
        List<ClinicalItemData> clinicalItemList = new ArrayList<>();
        if (fromDate != null && toDate != null) {
            items = clinicalItemRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(
                   patientId, fromDate, toDate);
            clinicalItemList = sortItems(items);
        } else if (fromDate != null) {
            items = clinicalItemRepository
                    .findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(patientId, fromDate);
            clinicalItemList = sortItems(items);
        } else if (toDate != null) {
            items = clinicalItemRepository
                    .findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(patientId, toDate);
            clinicalItemList = sortItems(items);
        } else {
            items = clinicalItemRepository.findBynhsNumberOrderBySectionDateDesc(patientId);
            clinicalItemList = sortItems(items);
        }
      return clinicalItemList;
    }


private List<ClinicalItemData> sortItems(List<ClinicalItemEntity> items) {
    List<ClinicalItemData> clinicalItemList = new ArrayList<>();
    for (int i = 0; i < items.size(); i++) {
        ClinicalItemData clinicalItemData = new ClinicalItemData();
        clinicalItemData.setDate(items.get(i).getDate());
        clinicalItemData.setDetails(items.get(i).getDetails());
        clinicalItemData.setEntry(items.get(i).getEntry());

        clinicalItemList.add(clinicalItemData);
    }

        return clinicalItemList;
    }




}
