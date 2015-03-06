package net.nhs.esb.contact.model;

import java.util.List;

/**
 */
public class ContactComposition {

    private String compositionId;
    private List<Contact> contacts;

    public String getCompositionId() {
        return compositionId;
    }

    public void setCompositionId(String compositionId) {
        this.compositionId = compositionId;
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public void setContacts(List<Contact> contacts) {
        this.contacts = contacts;
    }
}
