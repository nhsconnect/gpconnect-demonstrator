package uk.gov.hscic.medications.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import uk.gov.hscic.common.service.AbstractLegacyService;
import uk.gov.hscic.medication.model.MedicationDetails;
import uk.gov.hscic.medication.model.PatientMedicationHTML;
import uk.gov.hscic.medication.search.MedicationSearch;
import uk.gov.hscic.medications.model.MedicationEntity;
import uk.gov.hscic.medications.model.PatientMedicationHtmlEntity;
import uk.gov.hscic.medications.repo.MedicationHtmlRepository;
import uk.gov.hscic.medications.repo.MedicationRepository;

@Service
public class LegacyMedicationSearch extends AbstractLegacyService implements MedicationSearch {

    @Autowired
    private MedicationHtmlRepository medicationHtmlRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    private final MedicationEntityToMedicationTransformer medicationTransformer = new MedicationEntityToMedicationTransformer();

    @Override
    public List<PatientMedicationHTML> findPatientMedicationHTML(final String patientId) {
        List<PatientMedicationHtmlEntity> items = medicationHtmlRepository.findBynhsNumber(patientId);
        List<PatientMedicationHTML> medicationList = new ArrayList<>();
        for (int i = 0; i < items.size(); i++) {
            PatientMedicationHTML medicationData = new PatientMedicationHTML();
            medicationData.setCurrentRepeatPast(items.get(i).getCurrentRepeatPast());
            medicationData.setStartDate(items.get(i).getStartDate());
            medicationData.setMedicationItem(items.get(i).getMedicationItem());
            medicationData.setScheduledEnd(items.get(i).getScheduledEnd());
            medicationData.setDaysDuration(items.get(i).getDaysDuration());
            medicationData.setDetails(items.get(i).getDetails());
            medicationData.setLastIssued(items.get(i).getLastIssued());
            medicationData.setReviewDate(items.get(i).getReviewDate());
            medicationData.setNumberIssued(items.get(i).getNumberIssued());
            medicationData.setMaxIssued(items.get(i).getMaxIssued());
            medicationData.setTypeMed(items.get(i).getTypeMed());
            medicationList.add(medicationData);
        }

        return medicationList;

    }

    @Override
    public MedicationDetails findMedicationByID(Long id) {
        final MedicationEntity item = medicationRepository.findOne(id);
        if (item == null) {
            return null;
        } else {
            return medicationTransformer.transform(item);
        }
    }

}
