package uk.gov.hscic.medications.search;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hscic.medication.model.MedicationDetails;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import uk.gov.hscic.medications.model.MedicationEntity;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationHtmlRepository;
import uk.gov.hscic.medications.repo.MedicationRepository;

@Service
public class MedicationSearch {
    private final MedicationEntityToMedicationTransformer medicationTransformer = new MedicationEntityToMedicationTransformer();

    @Autowired
    private MedicationHtmlRepository medicationHtmlRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    public List<PatientMedicationHTML> findPatientMedicationHTML(final String patientId) {
        List<PatientMedicationHTML> medicationList = new ArrayList<>();

        for (PatientMedicationHtmlEntity patientMedicationHtmlEntity : medicationHtmlRepository.findBynhsNumber(patientId)) {
            PatientMedicationHTML medicationData = new PatientMedicationHTML();
            medicationData.setCurrentRepeatPast(patientMedicationHtmlEntity.getCurrentRepeatPast());
            medicationData.setStartDate(patientMedicationHtmlEntity.getStartDate());
            medicationData.setMedicationItem(patientMedicationHtmlEntity.getMedicationItem());
            medicationData.setScheduledEnd(patientMedicationHtmlEntity.getScheduledEnd());
            medicationData.setDaysDuration(patientMedicationHtmlEntity.getDaysDuration());
            medicationData.setDetails(patientMedicationHtmlEntity.getDetails());
            medicationData.setLastIssued(patientMedicationHtmlEntity.getLastIssued());
            medicationData.setReviewDate(patientMedicationHtmlEntity.getReviewDate());
            medicationData.setNumberIssued(patientMedicationHtmlEntity.getNumberIssued());
            medicationData.setMaxIssued(patientMedicationHtmlEntity.getMaxIssued());
            medicationData.setTypeMed(patientMedicationHtmlEntity.getTypeMed());
            medicationList.add(medicationData);
        }

        return medicationList;
    }

    public MedicationDetails findMedicationByID(Long id) {
        final MedicationEntity item = medicationRepository.findOne(id);

        return item == null
                ? null
                : medicationTransformer.transform(item);
    }
}
