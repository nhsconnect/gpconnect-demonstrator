package uk.gov.hscic.patient.investigations.search;

import org.apache.commons.collections4.Transformer;
import uk.gov.hscic.common.types.RepoSourceType;
import uk.gov.hscic.patient.investigations.model.InvestigationListHtml;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;

public class InvestigationEntityToListTransformer implements Transformer<InvestigationEntity, InvestigationListHtml> {

    @Override
    public InvestigationListHtml transform(final InvestigationEntity investigationEntity) {
        final InvestigationListHtml investigationList = new InvestigationListHtml();

        investigationList.setSourceId(String.valueOf(investigationEntity.getId()));
        investigationList.setSource(RepoSourceType.LEGACY.getSourceName());

        investigationList.setProvider(investigationEntity.getProvider());
        investigationList.setHtml(investigationEntity.getHtml());

        investigationList.setLastUpdated(investigationEntity.getLastUpdated());
        return investigationList;
    }
}
