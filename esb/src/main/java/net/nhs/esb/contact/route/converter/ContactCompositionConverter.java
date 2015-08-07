package net.nhs.esb.contact.route.converter;

import java.util.List;
import java.util.Map;

import net.nhs.esb.contact.model.Contact;
import net.nhs.esb.contact.model.ContactComposition;
import net.nhs.esb.openehr.converter.BaseCompositionConverter;
import net.nhs.esb.openehr.model.CompositionResponseData;
import org.apache.camel.Converter;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

/**
 */
@Converter
@Component
public class ContactCompositionConverter extends BaseCompositionConverter<Contact> {

    private static final String CONTACT_UID = "relevant_contacts/_uid";
    private static final String CONTACT_DEFINITION = "relevant_contacts/relevant_contacts:0/relevant_contact:";

    @Converter
    public ContactComposition convertResponseToContactComposition(CompositionResponseData response) {

        Map<String, Object> rawComposition = response.getComposition();

        String compositionId = MapUtils.getString(rawComposition, CONTACT_UID);
        List<Contact> contactList = extractCompositionData(rawComposition);

        ContactComposition contactComposition = new ContactComposition();
        contactComposition.setCompositionId(compositionId);
        contactComposition.setContacts(contactList);

        return contactComposition;
    }

    @Override
    protected Contact create(Map<String, Object> rawComposition, String prefix) {

        Contact contact = new Contact();
        contact.setName(MapUtils.getString(rawComposition, prefix + "/personal_details/person_name/unstructured_name"));
        contact.setContactInformation(MapUtils.getString(rawComposition, prefix + "/personal_details/telecom_details/unstuctured_telcoms"));
        contact.setRelationship(MapUtils.getString(rawComposition, prefix + "/relationship"));
        contact.setRelationshipType(MapUtils.getString(rawComposition, prefix + "/relationship_category|value"));
        contact.setRelationshipCode(MapUtils.getString(rawComposition, prefix + "/relationship_category|code"));
        contact.setRelationshipTerminology(MapUtils.getString(rawComposition, prefix + "/relationship_category|terminology"));
        contact.setNextOfKin(MapUtils.getBoolean(rawComposition, prefix + "/is_next_of_kin"));
        contact.setNote(MapUtils.getString(rawComposition, prefix + "/note"));
        contact.setSource("openehr");

        return contact;
    }

    @Override
    protected String dataDefinitionPrefix() {
        return CONTACT_DEFINITION;
    }
}
