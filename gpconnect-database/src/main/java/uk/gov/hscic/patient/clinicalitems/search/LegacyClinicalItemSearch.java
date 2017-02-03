package uk.gov.hscic.patient.clinicalitems.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;
import uk.gov.hscic.patient.clinicalitems.repo.ClinicalItemRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class LegacyClinicalItemSearch extends AbstractLegacyService implements ClinicalItemSearch {

    @Autowired
    private ClinicalItemRepository clinicalItemRepository;

    private final ClinicalItemEntityToHTMLTransformer transformer = new ClinicalItemEntityToHTMLTransformer();

    @Override
    public List<ClinicalItemListHTML> findAllClinicalItemHTMLTables(final String patientId, Date fromDate,
            Date toDate) {

        List<ClinicalItemEntity> items = null;
        if (fromDate != null && toDate != null) {
            items = clinicalItemRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(
                    Long.valueOf(patientId), fromDate, toDate);
        } else if (fromDate != null) {
            items = clinicalItemRepository
                    .findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long.valueOf(patientId), fromDate);
        } else if (toDate != null) {
            items = clinicalItemRepository
                    .findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), toDate);
        } else {
            items = clinicalItemRepository.findBynhsNumberOrderBySectionDateDesc(Long.valueOf(patientId));
        }
        if (items  == null || items.size() <= 0) {
            return null;
        } else {
            return Collections.singletonList(transformer.transform(items));
        }
    }
}
