package uk.gov.hscic.patient.patientsummary.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryEntity;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryListHtml;
import uk.gov.hscic.patient.patientsummary.repo.PatientSummaryRepository;

@Service
public class PatientSummarySearch {
    private final PatientSummaryEntityToListTransformer transformer = new PatientSummaryEntityToListTransformer();

    @Autowired
    private PatientSummaryRepository patientSummaryRepository;

    public PatientSummaryListHtml findPatientSummaryListHtml(String patientId) {
        final PatientSummaryEntity item = patientSummaryRepository.findOne(Long.parseLong(patientId));

        return item == null
                ? null
                : transformer.transform(item);
    }
}
