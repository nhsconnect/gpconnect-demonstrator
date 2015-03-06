package net.nhs.esb.medication.model;

import java.util.List;

/**
 */
public class MedicationComposition {

    private String compositionId;
    private List<Medication> medications;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}
