package uk.gov.hscic.patient.observations.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;

public class ObservationEntityToListTransformer implements Transformer<ObservationEntity, ObservationListHTML> {

    @Override
    public ObservationListHTML transform(final ObservationEntity observationEntity) {
        final ObservationListHTML observationList = new ObservationListHTML();

        observationList.setSourceId(String.valueOf(observationEntity.getId()));
        observationList.setSource(RepoSourceType.LEGACY.getSourceName());

        observationList.setProvider(observationEntity.getProvider());
        observationList.setHtml(observationEntity.getHtml());

        observationList.setLastUpdated(observationEntity.getLastUpdated());
        return observationList;
    }
}
