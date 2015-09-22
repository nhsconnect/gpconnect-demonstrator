package org.rippleosi.patient.contacts.search;

import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.Transformer;
import org.rippleosi.patient.contacts.model.ContactHeadline;

/**
 */
public class ContactHeadlineTransformer implements Transformer<Map<String, Object>, ContactHeadline> {

    @Override
    public ContactHeadline transform(Map<String, Object> input) {

        ContactHeadline contact = new ContactHeadline();
        contact.setSource("openehr");
        contact.setSourceId(MapUtils.getString(input, "uid"));
        contact.setName(MapUtils.getString(input, "name"));

        return contact;
    }
}
