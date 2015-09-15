package org.rippleosi.patient.contacts.rest;

import java.util.List;

import org.rippleosi.patient.contacts.model.ContactDetails;
import org.rippleosi.patient.contacts.model.ContactHeadline;
import org.rippleosi.patient.contacts.model.ContactSummary;
import org.rippleosi.patient.contacts.search.ContactSearch;
import org.rippleosi.patient.contacts.search.ContactSearchFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@RestController
@RequestMapping("/patients/{patientId}/contacts")
public class ContactsController {

    @Autowired
    private ContactSearchFactory contactSearchFactory;

    @RequestMapping(method = RequestMethod.GET)
    public List<ContactSummary> findAllContacts(@PathVariable("patientId") String patientId,
                                                @RequestParam(required = false) String source) {
        ContactSearch contactSearch = contactSearchFactory.select(source);

        return contactSearch.findAllContacts(patientId);
    }

    @RequestMapping(value = "/headlines", method = RequestMethod.GET)
    public List<ContactHeadline> findContactHeadlines(@PathVariable("patientId") String patientId,
                                                      @RequestParam(required = false) String source) {
        ContactSearch contactSearch = contactSearchFactory.select(source);

        return contactSearch.findContactHeadlines(patientId);
    }

    @RequestMapping(value = "/{contactId}", method = RequestMethod.GET)
    public ContactDetails findContact(@PathVariable("patientId") String patientId,
                                      @PathVariable("contactId") String contactId,
                                      @RequestParam(required = false) String source) {
        ContactSearch contactSearch = contactSearchFactory.select(source);

        return contactSearch.findContact(patientId, contactId);
    }
}
