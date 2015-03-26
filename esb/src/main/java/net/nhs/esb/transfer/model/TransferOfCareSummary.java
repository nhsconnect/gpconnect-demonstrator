package net.nhs.esb.transfer.model;

import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.problem.model.ProblemComposition;

/**
 */
public class TransferOfCareSummary {

    private String compositionId;
    private AllergyComposition allergies;
    private ContactComposition contacts;
    private MedicationComposition medication;
    private ProblemComposition problems;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public AllergyComposition getAllergies() {
        return allergies;
    }

    public void setAllergies(AllergyComposition allergies) {
        this.allergies = allergies;
    }

    public ContactComposition getContacts() {
        return contacts;
    }

    public void setContacts(ContactComposition contacts) {
        this.contacts = contacts;
    }

    public MedicationComposition getMedication() {
        return medication;
    }

    public void setMedication(MedicationComposition medication) {
        this.medication = medication;
    }

    public ProblemComposition getProblems() {
        return problems;
    }

    public void setProblems(ProblemComposition problems) {
        this.problems = problems;
    }
}
