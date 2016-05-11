package uk.gov.hscic.patient.observations.search;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.repo.ObservationRepository;
import uk.gov.hscic.patient.observations.model.ObservationListHTML;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class LegacyObservationSearch extends AbstractLegacyService implements uk.gov.hscic.patient.observations.search.ObservationSearch {

    @Autowired
    private ObservationRepository observationRepository;

    private final ObservationEntityToListTransformer transformer = new ObservationEntityToListTransformer();

    @Override
    public List<ObservationListHTML> findAllObservationHTMLTables(final String patientId) {

        final ObservationEntity item = observationRepository.findOne(Long.parseLong(patientId));

        if(item == null){
            return null;
        } else {
            return Collections.singletonList(transformer.transform(item));
        }
    }
}
