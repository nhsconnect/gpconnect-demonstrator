package uk.gov.hscic.medications.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationHtmlRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class MedicationSearch {
    @Autowired
    private MedicationHtmlRepository repository;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public final static String PAST = "Past";
    public final static String CRRENT = "Current";
    public final static String REPEAT = "Repeat";

    public List<PatientMedicationHtmlEntity> findMedications(final String nhsNumber, Date fromDate, Date toDate, String currRepPast) {
        String fromDateString = fromDate != null ? format.format(fromDate) : null;
        String toDateString = toDate != null ? format.format(toDate) : null;

        if (fromDateString != null && toDateString != null) {
            return repository.findByNhsNumberAndBetweenDates(nhsNumber, fromDateString, toDateString, currRepPast);
        } else if (fromDateString != null) {
            return repository.findByNhsNumberAfterDate(nhsNumber, fromDateString, currRepPast);
        } else if (toDateString != null) {
            return repository.findByNhsNumberAndBeforeDate(nhsNumber, toDateString, currRepPast);
        }

        return repository.findBynhsNumber(nhsNumber, currRepPast);
    }

}
