package uk.gov.hscic.patient.adminitems.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.repo.AdminItemRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class LegacyAdminItemSearch extends AbstractLegacyService implements AdminItemSearch {

    @Autowired
    private AdminItemRepository adminItemRepository;

    private final AdminItemEntityToHTMLTransformer transformer = new AdminItemEntityToHTMLTransformer();

    @Override
    public List<AdminItemListHTML> findAllAdminItemHTMLTables(final String patientId,Date fromDate,
            Date toDate) {
        
        List<AdminItemEntity> items = null;
        if (fromDate != null && toDate != null) {
            items = adminItemRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(
                    Long.valueOf(patientId), fromDate, toDate);
        } else if (fromDate != null) {
            items = adminItemRepository
                    .findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(Long.valueOf(patientId), fromDate);
        } else if (toDate != null) {
            items = adminItemRepository
                    .findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(Long.valueOf(patientId), toDate);
        } else {
            items = adminItemRepository.findBynhsNumberOrderBySectionDateDesc(Long.valueOf(patientId));
        }
        if (items  == null || items.size() <= 0) {
            return null;
        } else {
            return Collections.singletonList(transformer.transform(items));
        }
    }
}
