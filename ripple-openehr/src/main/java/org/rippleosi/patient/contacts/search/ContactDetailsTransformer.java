package org.rippleosi.patient.contacts.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.contacts.model.ContactDetails;

/**
 */
public class ContactDetailsTransformer implements Transformer<Map<String, Object>, ContactDetails> {

    @Override
    public ContactDetails transform(Map<String, Object> input) {

        Boolean nextOfKin = MapUtils.getBoolean(input, "next_of_kin");

        ContactDetails contact = new ContactDetails();
        contact.setSource("openehr");
        contact.setSourceId(MapUtils.getString(input, "uid"));
        contact.setName(MapUtils.getString(input, "name"));
        contact.setNextOfKin(nextOfKin != null && nextOfKin.booleanValue());
        contact.setRelationship(MapUtils.getString(input, "relationship"));
        contact.setRelationshipType(MapUtils.getString(input, "relationship_type"));
        contact.setRelationshipCode(MapUtils.getString(input, "relationship_code"));
        contact.setRelationshipTerminology(MapUtils.getString(input, "relationship_terminology"));
        contact.setContactInformation(MapUtils.getString(input, "contact_information"));
        contact.setNotes(MapUtils.getString(input, "notes"));

        return contact;
    }
}
