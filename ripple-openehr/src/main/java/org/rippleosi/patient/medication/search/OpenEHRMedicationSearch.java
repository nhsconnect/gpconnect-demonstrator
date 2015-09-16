package org.rippleosi.patient.medication.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.model.MedicationSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRMedicationSearch extends AbstractOpenEhrService implements MedicationSearch {

    @Override
    public List<MedicationHeadline> findMedicationHeadlines(String patientId) {
        MedicationHeadlineQueryStrategy query = new MedicationHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public List<MedicationSummary> findAllMedication(String patientId) {
        MedicationSummaryQueryStrategy query = new MedicationSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public MedicationDetails findMedication(String patientId, String medicationId) {
        MedicationDetailsQueryStrategy query = new MedicationDetailsQueryStrategy(patientId, medicationId);

        return findData(query);
    }
}
