package net.nhs.esb.contact.route.converter;

import java.util.HashMap;
import java.util.Map;

import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.contact.model.ContactUpdate;
import org.apache.camel.Converter;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ContactUpdateConverter {

    @Converter
    public ContactUpdate convertCompositionToContactUpdate(ContactComposition composition) {

        Map<String,String> content = new HashMap<>();

        content.put("ctx/language", "en");
        content.put("ctx/territory", "GB");

        int index = 0;
        for (Contact contact : composition.getContacts()) {

            String prefix = "relevant_contacts/relevant_contacts:0/relevant_contact:" + index;

            String nextOfKin = contact.getNextOfKin() == null ? null : contact.getNextOfKin().toString();

            content.put(prefix + "/personal_details/person_name/unstructured_name", contact.getName());
            content.put(prefix + "/personal_details/telecom_details/unstuctured_telcoms", contact.getContactInformation());
            content.put(prefix + "/relationship", contact.getRelationship());
            content.put(prefix + "/relationship_category|value", contact.getRelationshipType());
            content.put(prefix + "/relationship_category|code", contact.getRelationshipCode());
            content.put(prefix + "/relationship_category|terminology", contact.getRelationshipTerminology());
            content.put(prefix + "/is_next_of_kin", nextOfKin);
            content.put(prefix + "/note", contact.getNote());
            
            index++;
        }

        return new ContactUpdate(content);
    }

}
