package uk.gov.hscic.patient.keyindicators.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.keyindicator.model.KeyIndicatorListHTML;
import uk.gov.hscic.patient.keyindicators.model.KeyIndicatorEntity;

public class KeyIndicatorEntityToListTransformer implements Transformer<KeyIndicatorEntity, KeyIndicatorListHTML> {

    @Override
    public KeyIndicatorListHTML transform(final KeyIndicatorEntity keyIndicatorEntity) {
        final KeyIndicatorListHTML keyIndicatorList = new KeyIndicatorListHTML();

        keyIndicatorList.setSourceId(String.valueOf(keyIndicatorEntity.getId()));
        keyIndicatorList.setSource(RepoSourceType.LEGACY.getSourceName());

        keyIndicatorList.setProvider(keyIndicatorEntity.getProvider());
        keyIndicatorList.setHtml(keyIndicatorEntity.getHtml());

        return keyIndicatorList;
    }
}
