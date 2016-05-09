package uk.gov.hscic.patient.clinicalitems.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemEntity;
import uk.gov.hscic.patient.clinicalitems.model.ClinicalItemListHTML;

public class ClinicalItemEntityToListTransformer implements Transformer<ClinicalItemEntity, ClinicalItemListHTML> {

    @Override
    public ClinicalItemListHTML transform(final ClinicalItemEntity clinicalItemEntity) {
        final ClinicalItemListHTML clinicalItemList = new ClinicalItemListHTML();

        clinicalItemList.setSourceId(String.valueOf(clinicalItemEntity.getId()));
        clinicalItemList.setSource(RepoSourceType.LEGACY.getSourceName());

        clinicalItemList.setProvider(clinicalItemEntity.getProvider());
        clinicalItemList.setHtml(clinicalItemEntity.getHtml());

        return clinicalItemList;
    }
}
