package uk.gov.hscic.patient.patientsummary.search;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.patient.patientsummary.model.PatientSummaryEntity;
import uk.gov.hscic.patient.patientsummary.repo.PatientSummaryRepository;

@Service
public class PatientSummarySearch {

    @Autowired
    private PatientSummaryRepository patientSummaryRepository;

    public String findPatientSummaryHtml(String patientId) {
        final PatientSummaryEntity patientSummaryEntity = patientSummaryRepository.findOne(Long.parseLong(patientId));

        return patientSummaryEntity == null
                ? null
                : patientSummaryEntity.getHtml();
    }
}
