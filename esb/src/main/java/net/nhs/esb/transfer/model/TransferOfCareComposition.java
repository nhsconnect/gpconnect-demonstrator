package net.nhs.esb.transfer.model;

import net.nhs.esb.allergy.model.AllergyComposition;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.medication.model.MedicationComposition;
import net.nhs.esb.problem.model.ProblemComposition;

/**
 */
public class TransferOfCareComposition {

    private TransferOfCare transferOfCare;
    private AllergyComposition allergies;
    private ContactComposition contacts;
    private MedicationComposition medication;
    private ProblemComposition problems;

    public TransferOfCare getTransferOfCare() {
        return transferOfCare;
    }

    public void setTransferOfCare(TransferOfCare transferOfCare) {
        this.transferOfCare = transferOfCare;
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
