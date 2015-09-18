package org.rippleosi.patient.contacts.search;

import java.util.List;

import org.rippleosi.common.exception.ConfigurationException;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.model.ContactSummary;

/**
 */
public class NotConfiguredContactSearch implements ContactSearch {

    @Override
    public String getSource() {
        return "not configured";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public List<ContactHeadline> findContactHeadlines(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ContactSearch.class);
    }

    @Override
    public List<ContactSummary> findAllContacts(String patientId) {
        throw ConfigurationException.unimplementedTransaction(ContactSearch.class);
    }

    @Override
    public ContactDetails findContact(String patientId, String contactId) {
        throw ConfigurationException.unimplementedTransaction(ContactSearch.class);
    }
}
