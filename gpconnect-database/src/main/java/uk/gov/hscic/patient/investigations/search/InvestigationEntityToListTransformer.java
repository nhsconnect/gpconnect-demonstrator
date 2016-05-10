package uk.gov.hscic.patient.investigations.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.investigations.model.InvestigationListHTML;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;

public class InvestigationEntityToListTransformer implements Transformer<InvestigationEntity, InvestigationListHTML> {

    @Override
    public InvestigationListHTML transform(final InvestigationEntity investigationEntity) {
        final InvestigationListHTML investigationList = new InvestigationListHTML();

        investigationList.setSourceId(String.valueOf(investigationEntity.getId()));
        investigationList.setSource(RepoSourceType.LEGACY.getSourceName());

        investigationList.setProvider(investigationEntity.getProvider());
        investigationList.setHtml(investigationEntity.getHtml());

        return investigationList;
    }
}
