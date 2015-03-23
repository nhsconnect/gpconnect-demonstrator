package net.nhs.esb.transfer.model;

import java.util.List;

import net.nhs.esb.allergy.model.Allergy;
import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.medication.model.Medication;
import net.nhs.esb.problem.model.Problem;

/**
 */
public class TransferOfCare {

    private TransferDetail transferDetail;
    private List<Allergy> allergies;
    private List<Contact> contacts;
    private List<Medication> medication;
    private List<Problem> problems;

    public TransferDetail getTransferDetail() {
        return transferDetail;
    }

    public void setTransferDetail(TransferDetail transferDetail) {
        this.transferDetail = transferDetail;
    }

    public List<Allergy> getAllergies() {
        return allergies;
    }

    public void setAllergies(List<Allergy> allergies) {
        this.allergies = allergies;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }

    public List<Medication> getMedication() {
        return medication;
    }

    public void setMedication(List<Medication> medication) {
        this.medication = medication;
    }

    public List<Problem> getProblems() {
        return problems;
    }

    public void setProblems(List<Problem> problems) {
        this.problems = problems;
    }
}
