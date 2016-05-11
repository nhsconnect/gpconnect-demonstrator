package uk.gov.hscic.patient.immunisations.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.immunisations.model.ImmunisationListHTML;
import uk.gov.hscic.patient.immunisations.model.ImmunisationEntity;
import uk.gov.hscic.patient.immunisations.repo.ImmunisationRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LegacyImmunisationSearch extends AbstractLegacyService implements ImmunisationSearch {

    @Autowired
    private ImmunisationRepository immunisationRepository;

    private final ImmunisationEntityToListTransformer transformer = new ImmunisationEntityToListTransformer();

    @Override
    public List<ImmunisationListHTML> findAllImmunisationHTMLTables(final String patientId) {

        final ImmunisationEntity item = immunisationRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
