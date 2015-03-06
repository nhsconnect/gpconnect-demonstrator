package net.nhs.esb.contact.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.Transformer;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ContactCompositionConverter extends BaseCompositionConverter {

    @Converter
    public ContactComposition convertResponseToContactComposition(CompositionResponseData response) {

        Map<String,Object> rawComposition = getProperty(response.getComposition(), "relevant_contacts");

        ContactTransformer transformer = new ContactTransformer();

        String compositionId = extractCompositionId(rawComposition);
        List<Contact> contactList = extractCompositionData(rawComposition, "relevant_contacts[0].relevant_contact", transformer);

        ContactComposition contactComposition = new ContactComposition();
        contactComposition.setCompositionId(compositionId);
        contactComposition.setContacts(contactList);

        return contactComposition;
    }

    private class ContactTransformer implements Transformer<Map<String, Object>, Contact> {

        @Override
        public Contact transform(Map<String, Object> input) {

            Map<String,Object> personalDetails = getProperty(input, "personal_details[0]");

            String name = getProperty(personalDetails, "person_name[0].unstructured_name[0]");
            String contactInformation = getProperty(personalDetails, "telecom_details[0].unstuctured_telcoms[0]");
            String relationship = getProperty(input, "relationship[0]");
            String relationshipType = getProperty(input, "relationship_category[0].|value");
            String note = getProperty(input, "note[0]");
            Boolean nextOfKin = getProperty(input, "is_next_of_kin[0]");

            Contact contact = new Contact();
            contact.setName(name);
            contact.setContactInformation(contactInformation);
            contact.setRelationship(relationship);
            contact.setRelationshipType(relationshipType);
            contact.setNextOfKin(nextOfKin);
            contact.setNote(note);

            return contact;
        }
    }
}
