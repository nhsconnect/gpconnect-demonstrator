package org.rippleosi.patient.medication.search;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.medication.model.MedicationDetails;
import org.rippleosi.patient.medication.model.MedicationHeadline;
import org.rippleosi.patient.medication.model.MedicationSummary;

/**
 */
public class NotConfiguredMedicationSearch implements MedicationSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<MedicationHeadline> findMedicationHeadlines(String patientId) {
        // TODO Replace this code with notImplemented()

        MedicationHeadline headline = new MedicationHeadline();
        headline.setSourceId("1");
        headline.setSource("openehr");
        headline.setName("Atorvastatin");

        return Collections.singletonList(headline);
    }

    @Override
    public List<MedicationSummary> findAllMedication(String patientId) {

        MedicationSummary summary = new MedicationSummary();
        summary.setSourceId("1");
        summary.setSource("openehr");
        summary.setName("Atorvastatin");
        summary.setDoseAmount("400mg");

        return Collections.singletonList(summary);
    }

    @Override
    public MedicationDetails findMedication(String patientId, String medicationId) {

        MedicationDetails medicationDetails = new MedicationDetails();
        medicationDetails.setSourceId("1");
        medicationDetails.setSource("openehr");
        medicationDetails.setName("Atorvastatin");
        medicationDetails.setDoseAmount("400mg");
        medicationDetails.setDoseDirections("2 puffs as required for wheeze");
        medicationDetails.setDoseTiming("Morning before breakfast");
        medicationDetails.setRoute("inhalation");
        medicationDetails.setStartDateTime(new Date());
        medicationDetails.setTerminology("SNOMED-CT");

        return medicationDetails;
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + MedicationSearch.class.getSimpleName() + " instance");
    }
}
