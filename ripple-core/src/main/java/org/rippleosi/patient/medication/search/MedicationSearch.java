package org.rippleosi.patient.medication.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.model.MedicationSummary;

/**
 */
public interface MedicationSearch extends Repository {

    List<MedicationHeadline> findMedicationHeadlines(String patientId);

    List<MedicationSummary> findAllMedication(String patientId);

    MedicationDetails findMedication(String patientId, String medicationId);
}
