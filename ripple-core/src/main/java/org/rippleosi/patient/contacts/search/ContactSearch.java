package org.rippleosi.patient.contacts.search;

import java.util.List;

import org.rippleosi.common.repo.Repository;
import org.rippleosi.patient.contacts.model.ContactDetails;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.model.ContactSummary;

/**
 */
public interface ContactSearch extends Repository {

    List<ContactHeadline> findContactHeadlines(String patientId);

    List<ContactSummary> findAllContacts(String patientId);

    ContactDetails findContact(String patientId, String contactId);
}
