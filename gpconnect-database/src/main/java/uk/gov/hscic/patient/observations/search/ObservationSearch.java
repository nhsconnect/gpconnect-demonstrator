package uk.gov.hscic.patient.observations.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.observations.model.ObservationEntity;
import uk.gov.hscic.patient.observations.repo.ObservationRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class ObservationSearch {
    @Autowired
    private ObservationRepository observationRepository;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public List<ObservationEntity> findObservations(final String nhsNumber, Date fromDate, Date toDate) {
        String fromDateString = fromDate != null ? format.format(fromDate) : null;
        String toDateString = toDate != null ? format.format(toDate) : null;
        Long nhsNumberLong = nhsNumber != null ? Long.parseLong(nhsNumber) : null;


        if (fromDateString != null && toDateString != null) {
            return observationRepository.findByNhsNumberBetweenDatesOrderByObservationDateDesc(nhsNumberLong, fromDateString, toDateString);
        } else if (fromDateString != null) {
            return observationRepository.findByNhsNumberAndAfterDateOrderByObservationDateDesc(nhsNumberLong, fromDateString);
        } else if (toDateString != null) {
            return observationRepository.findByNhsNumberAndBeforeDateOrderByObservationDateDesc(nhsNumberLong, toDateString);
        }

        return observationRepository.findBynhsNumberOrderByObservationDateDesc(nhsNumber);
    }
}
