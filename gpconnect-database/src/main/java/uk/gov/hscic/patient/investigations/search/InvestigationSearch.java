package uk.gov.hscic.patient.investigations.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
import uk.gov.hscic.patient.investigations.model.InvestigationListHtml;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;

@Service
public class InvestigationSearch {
    private final InvestigationEntityToListTransformer transformer = new InvestigationEntityToListTransformer();

    @Autowired
    private InvestigationRepository investigationRepository;

    public InvestigationListHtml findInvestigationListHtml(final String patientId) {
        final InvestigationEntity item = investigationRepository.findOne(Long.parseLong(patientId));

        return item == null
                ? null
                : transformer.transform(item);
    }
}
