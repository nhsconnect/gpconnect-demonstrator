package uk.gov.hscic.patient.investigations.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.investigation.model.InvestigationListHTML;
import uk.gov.hscic.patient.investigation.search.InvestigationSearch;
import uk.gov.hscic.patient.investigations.model.InvestigationEntity;
import uk.gov.hscic.patient.investigations.repo.InvestigationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegacyInvestigationSearch extends AbstractLegacyService implements InvestigationSearch {

    @Autowired
    private InvestigationRepository investigationRepository;

    @Override
    public List<InvestigationListHTML> findAllInvestigationHTMLTables(final String patientId) {
        final List<InvestigationEntity> investigations = investigationRepository.findAll();

        return CollectionUtils.collect(investigations, new InvestigationEntityToListTransformer(), new ArrayList<>());
    }
}
