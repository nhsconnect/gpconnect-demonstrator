package uk.gov.hscic.patient.adminitems.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;
import uk.gov.hscic.patient.adminitems.repo.AdminItemRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LegacyAdminItemSearch extends AbstractLegacyService implements AdminItemSearch {

    @Autowired
    private AdminItemRepository adminItemRepository;

    private final AdminItemEntityToListTransformer transformer = new AdminItemEntityToListTransformer();;

    @Override
    public List<AdminItemListHTML> findAllAdminItemHTMLTables(final String patientId) {

        final AdminItemEntity item = adminItemRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
