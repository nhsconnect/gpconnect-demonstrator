package uk.gov.hscic.patient.observations.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.observations.model.ObservationData;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.repo.ObservationRepository;

@Service
public class ObservationSearch {

    @Autowired
    private ObservationRepository observationRepository;

    public List<ObservationData> findAllObservationHTMLTables(final String patientId) {
        List<ObservationData> observationList = new ArrayList<>();

        for (ObservationEntity observationEntity : observationRepository.findBynhsNumber(patientId)) {
            ObservationData observationData = new ObservationData();
            observationData.setObservationDate(observationEntity.getObservationDate());
            observationData.setEntry(observationEntity.getEntry());
            observationData.setValue(observationEntity.getValue());
            observationData.setDetails(observationEntity.getDetails());
            observationList.add(observationData);
        }

        return observationList;
    }
}
