package uk.gov.hscic.patient.investigations.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;

@Service
public class InvestigationSearch {

    @Autowired
    private InvestigationRepository investigationRepository;

    public String findInvestigationHtml(final String patientId) {
        final InvestigationEntity investigationEntity = investigationRepository.findOne(Long.parseLong(patientId));

        return investigationEntity == null
                ? null
                : investigationEntity.getHtml();
    }
}
