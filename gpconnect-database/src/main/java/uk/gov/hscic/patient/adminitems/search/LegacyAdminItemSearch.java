package uk.gov.hscic.patient.adminitems.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.adminitems.model.AdminItemData;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;
import uk.gov.hscic.patient.adminitems.repo.AdminItemRepository;

@Service
public class LegacyAdminItemSearch extends AbstractLegacyService implements AdminItemSearch {

    @Autowired
    private AdminItemRepository adminItemRepository;

    @Override
    public List<AdminItemData> findAllAdminItemHTMLTables(final String patientId, Date fromDate, Date toDate) {

        List<AdminItemEntity> items = null;
        List<AdminItemData> adminDataList = new ArrayList<>();
        if (fromDate != null && toDate != null) {
            items = adminItemRepository.findBynhsNumberAndSectionDateAfterAndSectionDateBeforeOrderBySectionDateDesc(
                    patientId, fromDate, toDate);
            adminDataList = sortItems(items);
        } else if (fromDate != null) {
            items = adminItemRepository.findBynhsNumberAndSectionDateAfterOrderBySectionDateDesc(patientId, fromDate);
            adminDataList = sortItems(items);
        } else if (toDate != null) {
            items = adminItemRepository.findBynhsNumberAndSectionDateBeforeOrderBySectionDateDesc(patientId, toDate);
            adminDataList = sortItems(items);
        } else {
            items = adminItemRepository.findBynhsNumberOrderBySectionDateDesc(patientId);
            adminDataList = sortItems(items);
        }
       return adminDataList;
        }
    
    
    
    
    private List<AdminItemData> sortItems(List<AdminItemEntity> items) {
        List<AdminItemData> adminItemList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            AdminItemData adminItemData = new AdminItemData();
            adminItemData.setAdminDate(items.get(i).getAdminDate());
            adminItemData.setDetails(items.get(i).getDetails());
            adminItemData.setEntry(items.get(i).getEntry());

            adminItemList.add(adminItemData);
        }

            return adminItemList;
        }
    
    
    
    
    
    
    
    
}
