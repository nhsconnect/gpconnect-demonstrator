package uk.gov.hscic.patient.adminitems.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.adminitems.model.AdminItemEntity;
import uk.gov.hscic.patient.adminitems.model.AdminItemListHTML;

public class AdminItemEntityToListTransformer implements Transformer<AdminItemEntity, AdminItemListHTML> {

    @Override
    public AdminItemListHTML transform(final AdminItemEntity adminItemEntity) {
        final AdminItemListHTML adminItemList = new AdminItemListHTML();

        adminItemList.setSourceId(String.valueOf(adminItemEntity.getId()));
        adminItemList.setSource(RepoSourceType.LEGACY.getSourceName());

        adminItemList.setProvider(adminItemEntity.getProvider());

        adminItemList.setLastUpdated(adminItemEntity.getLastUpdated());
        return adminItemList;
    }
}
