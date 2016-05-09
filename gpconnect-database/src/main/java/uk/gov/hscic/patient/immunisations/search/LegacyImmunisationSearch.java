package uk.gov.hscic.patient.immunisations.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;
import uk.gov.hscic.patient.immunisations.repo.ImmunisationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class LegacyImmunisationSearch extends AbstractLegacyService implements ImmunisationSearch {

    @Autowired
    private ImmunisationRepository immunisationRepository;

    @Override
    public List<ImmunisationListHTML> findAllImmunisationHTMLTables(final String patientId) {
        final List<ImmunisationEntity> immunisations = immunisationRepository.findAll();

        return CollectionUtils.collect(immunisations, new ImmunisationEntityToListTransformer(), new ArrayList<>());
    }
}
