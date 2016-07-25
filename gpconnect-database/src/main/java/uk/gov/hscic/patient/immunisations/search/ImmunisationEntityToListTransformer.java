package uk.gov.hscic.patient.immunisations.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;

public class ImmunisationEntityToListTransformer implements Transformer<ImmunisationEntity, ImmunisationListHTML> {

    @Override
    public ImmunisationListHTML transform(final ImmunisationEntity immunisationEntity) {
        final ImmunisationListHTML immunisationList = new ImmunisationListHTML();

        immunisationList.setSourceId(String.valueOf(immunisationEntity.getId()));
        immunisationList.setSource(RepoSourceType.LEGACY.getSourceName());

        immunisationList.setProvider(immunisationEntity.getProvider());
        immunisationList.setHtml(immunisationEntity.getHtml());

        immunisationList.setLastUpdated(immunisationEntity.getLastUpdated());
        return immunisationList;
    }
}
