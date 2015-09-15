package org.rippleosi.patient.contacts.search;

import java.util.Collections;
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
        // TODO: Replace with notImplemented()
        ContactHeadline headline = new ContactHeadline();
        headline.setSourceId("1");
        headline.setSource("openehr");
        headline.setName("Sheila Nisbett");

        return Collections.singletonList(headline);
    }

    @Override
    public List<ContactSummary> findAllContacts(String patientId) {

        ContactSummary contactSummary = new ContactSummary();
        contactSummary.setSourceId("1");
        contactSummary.setSource("openehr");
        contactSummary.setName("Sheila Nisbett");
        contactSummary.setNextOfKin(true);
        contactSummary.setRelationship("Daughter");

        return Collections.singletonList(contactSummary);
    }

    @Override
    public ContactDetails findContact(String patientId, String contactId) {

        ContactDetails contactDetails = new ContactDetails();
        contactDetails.setSourceId("1");
        contactDetails.setSource("openehr");
        contactDetails.setName("Sheila Nisbett");
        contactDetails.setRelationship("Daughter");
        contactDetails.setRelationshipType("Informal carer");
        contactDetails.setRelationshipCode("at0039");
        contactDetails.setRelationshipTerminology("local");
        contactDetails.setNextOfKin(true);
        contactDetails.setContactInformation("mobile 055345 224567");
        contactDetails.setNotes("Has largely lost contact with her father");

        return contactDetails;
    }

    private ConfigurationException notImplemented() {
        return new ConfigurationException("Unable to find a configured " + ContactSearch.class.getSimpleName() + " instance");
    }
}
