package uk.gov.hscic.patient.observations.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.patient.observations.model.ObservationData;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.repo.ObservationRepository;

@Service
public class LegacyObservationSearch extends AbstractLegacyService
        implements uk.gov.hscic.patient.observations.search.ObservationSearch {

    @Autowired
    private ObservationRepository observationRepository;

    @Override
    public List<ObservationData> findAllObservationHTMLTables(final String patientId) {

        List<ObservationEntity> items = observationRepository.findBynhsNumber(patientId);
        List<ObservationData> observationList = new ArrayList<>();
        
        for(int i = 0 ; i < items.size(); i++ )
        {
            ObservationData observationData = new ObservationData();
            observationData.setObservationDate(items.get(i).getObservationDate());
            observationData.setEntry(items.get(i).getEntry());
            observationData.setValue(items.get(i).getValue());
            observationData.setDetails(items.get(i).getDetails());
            observationList.add(observationData);
        }
        
        return observationList;
        
    }
}
