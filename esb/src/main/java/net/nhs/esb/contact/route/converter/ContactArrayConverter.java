package net.nhs.esb.contact.route.converter;

import java.util.ArrayList;
import java.util.List;

import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactArray;
import net.nhs.esb.contact.route.model.ContactSearchResponse;
import net.nhs.esb.contact.route.model.ContactSearchResult;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ContactArrayConverter {

    @Converter
    public ContactArray convertAQLResponseToContactArray(ContactSearchResponse response) {

        List<Contact> contactList = new ArrayList<>();

        for (ContactSearchResult contactSearchResult : response.getResultSet()) {
            Contact contact = new Contact();
            contact.setName(contactSearchResult.getName());
            contact.setContactInformation(contactSearchResult.getContactInformation());
            contact.setRelationship(contactSearchResult.getRelationship());
            contact.setRelationshipType(contactSearchResult.getRelationshipType());
            contact.setNextOfKin(contactSearchResult.getNextOfKin());
            contact.setNote(contactSearchResult.getNote());

            contactList.add(contact);
        }

        ContactArray contactArray = new ContactArray();
        contactArray.setContacts(contactList);

        return contactArray;
    }
}
