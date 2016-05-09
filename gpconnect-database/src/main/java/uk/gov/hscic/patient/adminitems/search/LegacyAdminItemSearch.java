package uk.gov.hscic.patient.adminitems.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.repo.AdminItemRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegacyAdminItemSearch extends AbstractLegacyService implements AdminItemSearch {

    @Autowired
    private AdminItemRepository adminItemRepository;

    @Override
    public List<AdminItemListHTML> findAllAdminItemHTMLTables(final String patientId) {
        final List<AdminItemEntity> adminItems = adminItemRepository.findAll();

        return CollectionUtils.collect(adminItems, new AdminItemEntityToListTransformer(), new ArrayList<>());
    }
}
