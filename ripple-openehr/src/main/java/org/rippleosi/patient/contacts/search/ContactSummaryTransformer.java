package org.rippleosi.patient.contacts.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.contacts.model.ContactSummary;

/**
 */
public class ContactSummaryTransformer implements Transformer<Map<String, Object>, ContactSummary> {

    @Override
    public ContactSummary transform(Map<String, Object> input) {

        Boolean nextOfKin = MapUtils.getBoolean(input, "next_of_kin");

        ContactSummary contact = new ContactSummary();
        contact.setSource("openehr");
        contact.setSourceId(MapUtils.getString(input, "uid"));
        contact.setName(MapUtils.getString(input, "name"));
        contact.setNextOfKin(nextOfKin != null && nextOfKin.booleanValue());
        contact.setRelationship(MapUtils.getString(input, "relationship"));

        return contact;
    }
}
