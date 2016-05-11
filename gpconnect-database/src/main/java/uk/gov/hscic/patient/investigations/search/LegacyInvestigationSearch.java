package uk.gov.hscic.patient.investigations.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.investigations.model.InvestigationListHTML;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LegacyInvestigationSearch extends AbstractLegacyService implements InvestigationSearch {

    @Autowired
    private InvestigationRepository investigationRepository;

    private final InvestigationEntityToListTransformer transformer = new InvestigationEntityToListTransformer();

    @Override
    public List<InvestigationListHTML> findAllInvestigationHTMLTables(final String patientId) {

        final InvestigationEntity item = investigationRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
