package org.rippleosi.patient.contacts.search;

import java.util.List;

import org.rippleosi.common.service.AbstractOpenEhrService;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.model.ContactSummary;
import org.springframework.stereotype.Service;

/**
 */
@Service
public class OpenEHRContactSearch extends AbstractOpenEhrService implements ContactSearch {

    @Override
    public List<ContactHeadline> findContactHeadlines(String patientId) {
        ContactHeadlineQueryStrategy query = new ContactHeadlineQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public List<ContactSummary> findAllContacts(String patientId) {
        ContactSummaryQueryStrategy query = new ContactSummaryQueryStrategy(patientId);

        return findData(query);
    }

    @Override
    public ContactDetails findContact(String patientId, String contactId) {
        ContactDetailsQueryStrategy query = new ContactDetailsQueryStrategy(patientId, contactId);

        return findData(query);
    }
}
