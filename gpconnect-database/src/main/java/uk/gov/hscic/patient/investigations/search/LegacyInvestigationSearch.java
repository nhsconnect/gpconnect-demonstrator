package uk.gov.hscic.patient.investigations.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
import uk.gov.hscic.patient.investigations.model.InvestigationListHtml;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;

@Service
public class LegacyInvestigationSearch extends AbstractLegacyService implements InvestigationSearch {
    private final InvestigationEntityToListTransformer transformer = new InvestigationEntityToListTransformer();

    @Autowired
    private InvestigationRepository investigationRepository;

    @Override
    public InvestigationListHtml findInvestigationListHtml(final String patientId) {
        final InvestigationEntity item = investigationRepository.findOne(Long.parseLong(patientId));

        return item == null
                ? null
                : transformer.transform(item);
    }
}
